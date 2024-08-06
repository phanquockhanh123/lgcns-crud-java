package org.example.socialmediaspring.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.IsbnGenerator;
import org.example.socialmediaspring.common.PageNewResponse;
import org.example.socialmediaspring.constant.Common;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.dto.book.BookCategoryDto;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.dto.common.LongIdsRequest;
import org.example.socialmediaspring.dto.product.*;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.BookCategory;
import org.example.socialmediaspring.entity.Category;
import org.example.socialmediaspring.entity.Product;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.mapper.ProductMapper;
import org.example.socialmediaspring.repository.CategoryRepository;
import org.example.socialmediaspring.repository.ProductCustomRepository;
import org.example.socialmediaspring.repository.ProductCustomRepositoryImpl;
import org.example.socialmediaspring.repository.ProductRepository;
import org.example.socialmediaspring.service.ProductService;
import org.example.socialmediaspring.utils.FileUploadUtil;
import org.example.socialmediaspring.utils.JsonUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private final ProductCustomRepositoryImpl productCustomRepositoryImpl;

    private final ProductMapper productMapper;

    private final IsbnGenerator isbnGenerator;

    @Override
    public CreateProductDto saveProduct(CUProductRequest productDto) {
        if (productDto.getThumbnail() == null || productDto.getThumbnail().isEmpty()) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "No file choose");
        }

        if (productRepository.existsByTitle(productDto.getTitle())) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "Product title  existed");
        }

        // check category exists in DB
        Category category = categoryRepository.findById(productDto.getCategory())
                .orElseThrow(() -> new BizException(ErrorCodeConst.INVALID_INPUT,"Category not found"));

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(productDto.getThumbnail().getOriginalFilename()));
        Product product = new Product();
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountPercentage(productDto.getDiscountPercentage());
        product.setRating(productDto.getRating());
        product.setStock(productDto.getStock());
        product.setBrand(productDto.getBrand());
        product.setSku(isbnGenerator.generateISBN());
        product.setWeight(productDto.getWeight());
        product.setWarrantyInformation(productDto.getWarrantyInformation());
        product.setShippingInformation(productDto.getShippingInformation());
        product.setAvailabilityStatus(productDto.getAvailabilityStatus());
        product.setReturnPolicy(productDto.getReturnPolicy());
        product.setMinimumOrderQuantity(productDto.getMinimumOrderQuantity());
        product.setThumbnail(fileName);
        product.setCategoryId(productDto.getCategory());

        Product newProduct = productRepository.save(product);

        String uploadDir = "src/main/resources/static/public/book-images/" + product.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, productDto.getThumbnail());

        return productMapper.toProductDto(newProduct);

    }
    @Override
    public PageNewResponse<SearchProductDto> findAllProducts(SearchProductRequest searchReq) {
        log.info("Start search product with conds body: {}", JsonUtils.objToString(searchReq));

        PageRequest pageable = Common.getPageRequest(searchReq.getPage() - 1, searchReq.getLimit(), null);


        Pair<Long, List<SearchProductDto>> productsData = productCustomRepositoryImpl.getProductsByConds(searchReq, pageable);
        Long countProducts = productsData.getFirst();
        List<SearchProductDto> listProducts = productsData.getSecond();

        Page<SearchProductDto> pageProductData = new PageImpl<>(listProducts, pageable, countProducts);



        PageNewResponse<SearchProductDto> ib = PageNewResponse.<SearchProductDto>builder()
                .data(listProducts)
                .build();

        if (Objects.nonNull(searchReq.getGetTotalCount()) && Boolean.TRUE.equals(searchReq.getGetTotalCount())) {
            ib.setPagination(this.buildPagination(pageProductData.getSize(), pageProductData.getTotalPages(),
                    pageProductData.getNumber() + 1, pageProductData.getTotalElements()));
        }

        log.info("end ...");
        return ib;
    }

    @Override
    public String deleteProductsByIds(LongIdsRequest ids) {
        // check all ids not exists
        // check list category ids exists db
        for (Long productId : ids.getIds()) {
            Boolean existsProduct = productRepository.existsById(productId);
            if (!existsProduct) {
                throw new BizException(ErrorCodeConst.VALIDATE_VIOLATION, "Product id " + productId + " does not exist.");
            }
        }

        productRepository.deleteAllById(ids.getIds());

        StringBuilder message = new StringBuilder();
        message.append("Delete products ids success");

        return message.toString();
    }

    @Override
    public CreateProductDto updateProduct(Long id,CUProductRequest request) {
        // check product exists in db
        Product existsProduct = productRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCodeConst.INVALID_INPUT, "Product not found with id " + id));

        // check category exists in DB
        Category category = categoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new BizException(ErrorCodeConst.INVALID_INPUT,"Category not found"));

        // check another title same exists
        productRepository.findProductByTitle(request.getTitle()).ifPresent(product -> {
            if (!product.getId().equals(id)) {
                throw new BizException(ErrorCodeConst.INVALID_INPUT,"Product with title " + request.getTitle() + " already exists");
            }
        });

        // check null filePath
        if (request.getThumbnail() != null && !request.getThumbnail().isEmpty()) {
            String fileName = StringUtils.cleanPath(request.getThumbnail().getOriginalFilename());

            existsProduct.setThumbnail(fileName);

            String uploadDir = "src/main/resources/static/public/book-images/" + existsProduct.getId();

            FileUploadUtil.saveFile(uploadDir, fileName, request.getThumbnail());

        }

        existsProduct.setTitle(request.getTitle());
        existsProduct.setDescription(request.getDescription());
        existsProduct.setPrice(request.getPrice());
        existsProduct.setDiscountPercentage(request.getDiscountPercentage());
        existsProduct.setRating(request.getRating());
        existsProduct.setStock(request.getStock());
        existsProduct.setBrand(request.getBrand());
        existsProduct.setWeight(request.getWeight());
        existsProduct.setWarrantyInformation(request.getWarrantyInformation());
        existsProduct.setShippingInformation(request.getShippingInformation());
        existsProduct.setAvailabilityStatus(request.getAvailabilityStatus());
        existsProduct.setReturnPolicy(request.getReturnPolicy());
        existsProduct.setMinimumOrderQuantity(request.getMinimumOrderQuantity());
        existsProduct.setCategoryId(request.getCategory());
        Product savedProduct =  productRepository.save(existsProduct);
        return productMapper.toProductDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not existed");
        }

        return productRepository.getProductDetail(id);
    }

    private Map<String, Long> buildPagination(Integer limit, Integer totalPage, Integer currentPage, Long totalRecord){
        log.info("start buildPagination ...");

        Map<String, Long> pagination = new HashMap<>();
        pagination.put("limit", Long.valueOf(limit));
        pagination.put("total_page", Long.valueOf(totalPage));
        pagination.put("current_page", Long.valueOf(currentPage));
        pagination.put("total_record", totalRecord);

        log.info("end buildPagination ...");
        return pagination;
    }

}
