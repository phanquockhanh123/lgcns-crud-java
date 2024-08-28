package org.example.socialmediaspring.repository;

import com.google.api.client.json.Json;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.dto.product.SearchProductDto;
import org.example.socialmediaspring.dto.product.SearchProductRequest;
import org.example.socialmediaspring.utils.JsonUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository{
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Pair<Long, List<SearchProductDto>> getProductsByConds(SearchProductRequest searchReq, Pageable pageable) {

        log.info("start getListProductsByConds ...");
        var mapSqlAndParameter = this.buildCondition(searchReq);
        var conditions = mapSqlAndParameter.getFirst();
        var parameters = mapSqlAndParameter.getSecond();

        StringBuilder sql = this.buildMainQuery();

        sql.append(conditions);
        sql.append(" ORDER BY p.id DESC ");
        jakarta.persistence.Query query = em.createQuery(sql.toString(), SearchProductDto.class)
                .setFirstResult(pageable.getPageSize() * pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize());

        this.setParameters(parameters, query);

        log.info("Result list products ...{}", query);
        var listProducts = query.getResultList();

        StringBuilder sqlCount = this.buildCountQuery();
        sqlCount.append(conditions);
        jakarta.persistence.Query queryCount = em.createQuery(sqlCount.toString());
        this.setParameters(parameters, queryCount);
        Long count = ((Number) queryCount.getSingleResult()).longValue();

        log.info("end getListProductsByConds ...");
        return Pair.of(count, listProducts);
    }

    private Pair<StringBuilder, Map<String,Object>> buildCondition(SearchProductRequest searchReq) {
        Map<String,Object> mapParameter = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" where 1 = 1 ");

        if (Objects.nonNull(searchReq.getTitle()) && !searchReq.getTitle().isEmpty()) {
            String title = searchReq.getTitle();
            sql.append("and p.title LIKE :title ");
            mapParameter.put("title", "%" + title + "%");
        }
        if (Objects.nonNull(searchReq.getCategory())) {
            Integer categoryId = searchReq.getCategory();
            sql.append("and p.categoryId = :categoryId ");
            mapParameter.put("categoryId", categoryId);
        }

        if (Objects.nonNull(searchReq.getPriceFrom())) {
            Integer priceFrom = searchReq.getPriceFrom();
            sql.append("and p.price >= :priceFrom ");
            mapParameter.put("priceFrom", priceFrom);
        }
        if (Objects.nonNull(searchReq.getPriceTo())) {
            Integer priceTo = searchReq.getPriceTo();
            sql.append("and p.price <= :priceTo ");
            mapParameter.put("priceTo", priceTo);
        }

        return Pair.of(sql, mapParameter);
    }
    private void setParameters(Map<String,Object> mapParameter, Query query) {
        for (var entry : mapParameter.entrySet()) {
            query.setParameter(entry.getKey(),entry.getValue());
        }
    }

    private StringBuilder buildMainQuery() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
                .append("new org.example.socialmediaspring.dto.product.SearchProductDto( ")
                .append("p.id, ")
                .append("p.title, ")
                .append("p.description, ")
                .append("p.price, ")
                .append("p.discountPercentage, ")
                .append("p.rating, ")
                .append("p.stock, ")
                .append("p.brand, ")
                .append("p.sku, ")
                .append("p.weight, ")
                .append("p.warrantyInformation, ")
                .append("p.shippingInformation, ")
                .append("p.availabilityStatus, ")
                .append("p.returnPolicy, ")
                .append("p.minimumOrderQuantity, ")
                .append("c.id, ")
                .append("c.name ")
                .append(") ")
                .append("from Product p ")
                .append("INNER JOIN Category c ON c.id = p.categoryId ");
        return sql;
    }

    private StringBuilder buildCountQuery() {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(p.id) ")
                .append(" from Product p ")
                .append("INNER JOIN Category c ON c.id = p.categoryId ");
        return sql;
    }
}
