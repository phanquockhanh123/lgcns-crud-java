package org.example.socialmediaspring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.dto.book.BookBestSellerRes;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.dto.book.FilterBookReportResquest;
import org.example.socialmediaspring.dto.book.SearchBookRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        sql.append(" GROUP BY b.id ORDER BY b.id DESC ");
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
                .append("b.filePath, ")
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
        sql.append("select count(DISTINCT b.id) ")
                .append(" from Book b ")
                .append("INNER JOIN BookCategory bc ON b.id = bc.bookId ")
                .append("INNER JOIN Category c ON c.id = bc.categoryId ");
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

        return Pair.of(sql, mapParameter);
    }
    private void setParameters(Map<String,Object> mapParameter, Query query) {
        for (var entry : mapParameter.entrySet()) {
            query.setParameter(entry.getKey(),entry.getValue());
        }
    }

    @Override
    public Pair<Long, List<BookBestSellerRes>> getBooksReport(FilterBookReportResquest searchReq, Pageable pageable) {
        log.info("start getBooksReport ...");
        var mapSqlAndParameter = this.buildReportCondition(searchReq);
        var conditions = mapSqlAndParameter.getFirst();
        var parameters = mapSqlAndParameter.getSecond();

        StringBuilder sql = this.buildBooksReportQuery();
        sql.append(conditions);

        if (Objects.nonNull(searchReq.getSortField()) && Objects.nonNull(searchReq.getSortOrder())) {
            String sortOrder = searchReq.getSortOrder();
            String sortField = searchReq.getSortField();

            sql.append(" GROUP BY b.id ORDER BY ").append(sortField).append(" ").append(sortOrder);
        } else {
            sql.append(" GROUP BY b.id ORDER BY totalMoney DESC");
        }

        jakarta.persistence.Query query = em.createQuery(sql.toString(), BookResponse.class)
                .setFirstResult(pageable.getPageSize() * pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize());
        this.setParameters(parameters, query);

        var listBooks = query.getResultList();

        StringBuilder sqlCount = this.buildCountBookReportQuery();
        sqlCount.append(conditions);
        jakarta.persistence.Query queryCount = em.createQuery(sqlCount.toString());
        this.setParameters(parameters, queryCount);
        Long count = ((Number) queryCount.getSingleResult()).longValue();

        log.info("end getBooksReport ...");
        return Pair.of(count, listBooks);
    }

    private StringBuilder buildBooksReportQuery() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
                .append("new org.example.socialmediaspring.dto.book.BookBestSellerRes( ")
                .append("b.id, ")
                .append("b.title, ")
                .append("b.author, ")
                .append("b.isbn, ")
                .append("b.quantity, ")
                .append("b.quantityAvail, ")
                .append("b.yearOfPublish, ")
                .append("b.price, ")
                .append("COALESCE(COUNT(bt.id), 0) as totalSale, ")
                .append("COALESCE(SUM(bt.amount + bt.bonus), 0) as totalMoney ")
                .append(") ")
                .append("FROM Book b ")
                .append("LEFT JOIN BookTransaction bt ON b.id = bt.bookId ");
        return sql;
    }
    private StringBuilder buildCountBookReportQuery() {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(DISTINCT b.id) ")
                .append(" from Book b ")
                .append("LEFT JOIN BookTransaction bt ON b.id = bt.bookId ");
        return sql;
    }

    private Pair<StringBuilder, Map<String,Object>> buildReportCondition(FilterBookReportResquest searchReq) {
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

        return Pair.of(sql, mapParameter);
    }

}
