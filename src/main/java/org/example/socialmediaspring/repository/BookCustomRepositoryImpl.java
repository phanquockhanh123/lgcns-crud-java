package org.example.socialmediaspring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.dto.book.SearchBookRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransactionDto;
import org.example.socialmediaspring.dto.book_transactions.SearchBookTransactionDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository{
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Pair<Long, List<BookResponse>> getBooksByConds(SearchBookRequest searchReq, Pageable pageable) {
        log.info("start getListBooksByConds ...");
        var mapSqlAndParameter = this.buildCondition(searchReq);
        var conditions = mapSqlAndParameter.getFirst();
        var parameters = mapSqlAndParameter.getSecond();

        StringBuilder sql = this.buildMainQuery();
        sql.append(conditions);
        jakarta.persistence.Query query = em.createQuery(sql.toString(), BookResponse.class)
                .setFirstResult(pageable.getPageSize() * pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize());
        this.setParameters(parameters, query);

        var listBooks = query.getResultList();

        StringBuilder sqlCount = this.buildCountQuery();
        sqlCount.append(conditions);
        jakarta.persistence.Query queryCount = em.createQuery(sqlCount.toString());
        this.setParameters(parameters, queryCount);
        Long count = ((Number) queryCount.getSingleResult()).longValue();

        log.info("end getListBooksByConds ...");
        return Pair.of(count, listBooks);
    }

    private StringBuilder buildMainQuery() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
                .append("new org.example.socialmediaspring.dto.book.BookResponse( ")
                .append("b.id, ")
                .append("LISTAGG(c.name, ',') WITHIN GROUP (ORDER BY c.name) AS categoryNames, ")
                .append("b.title, ")
                .append("b.author, ")
                .append("b.isbn, ")
                .append("b.description, ")
                .append("b.price, ")
                .append("b.yearOfPublish, ")
                .append("b.quantity, ")
                .append("b.quantityAvail, ")
                .append("b.created, ")
                .append("b.modified ")
                .append(") ")
                .append("FROM Book b ")
                .append("INNER JOIN BookCategory bc ON b.id = bc.bookId ")
                .append("INNER JOIN Category c ON c.id = bc.categoryId ");
        return sql;
    }

    private StringBuilder buildCountQuery() {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(id) ")
                .append(" from Book b ");
        return sql;
    }

    private Pair<StringBuilder, Map<String,Object>> buildCondition(SearchBookRequest searchReq) {
        Map<String,Object> mapParameter = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" where 1 = 1 ");

        if (Objects.nonNull(searchReq.getAuthor()) && !searchReq.getAuthor().isEmpty()) {
            String author = searchReq.getAuthor();
            sql.append("and b.author LIKE :author ");
            mapParameter.put("author", "%" + author + "%");
        }
        if (Objects.nonNull(searchReq.getTitle()) && !searchReq.getTitle().isEmpty()) {
            String title = searchReq.getTitle();
            sql.append("and b.title LIKE :title ");
            mapParameter.put("title", "%" + title + "%");
        }
//        if (Objects.nonNull(searchReq.getCateIds())) {
//            Set<Integer> cateIds = searchReq.getCateIds();
//            sql.append("and bc.categoryId IN (:cateIds) ");
//            mapParameter.put("cateIds", cateIds);
//        }
        if (Objects.nonNull(searchReq.getYearFrom())) {
            Integer yearFrom = searchReq.getYearFrom();
            sql.append("and b.yearOfPublish >= :yearFrom ");
            mapParameter.put("yearFrom", yearFrom);
        }
        if (Objects.nonNull(searchReq.getYearTo())) {
            Integer yearTo = searchReq.getYearTo();
            sql.append("and b.yearOfPublish <= :yearTo ");
            mapParameter.put("yearTo", yearTo);
        }

        sql.append(" GROUP BY b.id ORDER BY b.created DESC");

        return Pair.of(sql, mapParameter);
    }
    private void setParameters(Map<String,Object> mapParameter, Query query) {
        for (var entry : mapParameter.entrySet()) {
            query.setParameter(entry.getKey(),entry.getValue());
        }
    }

}
