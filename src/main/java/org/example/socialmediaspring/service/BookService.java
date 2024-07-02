package org.example.socialmediaspring.service;

import org.example.socialmediaspring.common.PageNewResponse;
import org.example.socialmediaspring.dto.book.*;
import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {
    BookCategoryDto saveBook(CUBookRequest bookRequest, MultipartFile file) throws IOException;

    BookCategoryDto updateBook(Integer id, CUBookRequest request, MultipartFile file) throws IOException;

    BookResponse getBookById(Integer id);

    void deleteBook(Integer id);

    PageNewResponse<BookResponse> searchAllBooks(SearchBookRequest request);

    String deleteBooksByIds(IdsRequest id);

    String bulkBookService(MultipartFile file) throws  IOException;

    List<Category> getCategoriesByBookId(Integer id);

    PageNewResponse<BookBestSellerRes> getBooksReport(SearchBookRequest request);

}
