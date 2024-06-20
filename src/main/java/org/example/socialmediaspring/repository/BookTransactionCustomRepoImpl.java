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
import org.example.socialmediaspring.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookTransactionCustomRepoImpl implements  BookTransactionCustomRepository{
    @PersistenceContext
    private final EntityManager em;

    @Autowired
    UserRepository userRepository;

    @Override
    public Pair<Long, List<BookTransactionDto>> searchBookTransByConds(SearchBookTransactionDto searchReq, Pageable pageable, Principal connectedUser) {
        log.info("start getListBooksByConds ...");
        var mapSqlAndParameter = this.buildCondition(searchReq, connectedUser);
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
        sql.append("SELECT new org.example.socialmediaspring.dto.book_transactions.BookTransactionDto(")
                .append("bt.id, ")
                .append("b.title, ")
                .append("b.author, ")
                .append("b.isbn, ")
                .append("b.price, ")
                .append("u.email, ")
                .append("u.phone, ")
                .append("bt.transactionId, ")
                .append("bt.status, ")
                .append("bt.quantity, ")
                .append("bt.amount, ")
                .append("bt.bonus, ")
                .append("bt.startDate, ")
                .append("bt.endDate, ")
                .append("bt.returnDate) ")
                .append("FROM BookTransaction bt INNER JOIN Book b ON bt.bookId = b.id ")
                .append("INNER JOIN User u ON bt.userId = u.id ");
        ;
        return sql;
    }

    private StringBuilder buildCountQuery() {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) ")
                .append("FROM BookTransaction bt INNER JOIN Book b ON bt.bookId = b.id ")
                .append("INNER JOIN User u ON bt.userId = u.id ");
        return sql;
    }

    private Pair<StringBuilder, Map<String,Object>> buildCondition(SearchBookTransactionDto searchReq,Principal connectedUser) {
        Map<String,Object> mapParameter = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" where 1 = 1 ");

        if (Objects.nonNull(searchReq.getUserId()) && searchReq.getUserId()) {
            UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userRepository.findUsersByUsername(currentUser.getUsername());

            Long userId = user.getId();
            sql.append("and bt.userId = :userId ");
            mapParameter.put("userId", userId);
        }
        if (Objects.nonNull(searchReq.getStatus())) {
            Integer status = searchReq.getStatus();
            sql.append("and bt.status = :status ");
            mapParameter.put("status", status);
        }

        sql.append("ORDER BY bt.id DESC ");

        return Pair.of(sql, mapParameter);
    }
    private void setParameters(Map<String,Object> mapParameter, Query query) {
        for (var entry : mapParameter.entrySet()) {
            query.setParameter(entry.getKey(),entry.getValue());
        }
    }
}
