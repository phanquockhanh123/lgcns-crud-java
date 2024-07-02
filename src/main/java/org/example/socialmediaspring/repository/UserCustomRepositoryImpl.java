package org.example.socialmediaspring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.dto.book.SearchBookRequest;
import org.example.socialmediaspring.dto.user.SearchUserRequest;
import org.example.socialmediaspring.dto.user.UserDto;
import org.example.socialmediaspring.entity.Role;
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
public class UserCustomRepositoryImpl implements UserCustomRepository{
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Pair<Long, List<UserDto>> getUsersByConds(SearchUserRequest searchReq, Pageable pageable) {
        log.info("start get list users ...");
        var mapSqlAndParameter = this.buildCondition(searchReq);
        var conditions = mapSqlAndParameter.getFirst();
        var parameters = mapSqlAndParameter.getSecond();

        StringBuilder sql = this.buildMainQuery();
        sql.append(conditions);
        sql.append(" ORDER BY u.id DESC ");
        jakarta.persistence.Query query = em.createQuery(sql.toString(), UserDto.class)
                .setFirstResult(pageable.getPageSize() * pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize());
        this.setParameters(parameters, query);

        var listUsers = query.getResultList();

        StringBuilder sqlCount = this.buildCountQuery();
        sqlCount.append(conditions);
        jakarta.persistence.Query queryCount = em.createQuery(sqlCount.toString());
        this.setParameters(parameters, queryCount);
        Long count = ((Number) queryCount.getSingleResult()).longValue();

        log.info("end get list users ...");
        return Pair.of(count, listUsers);
    }

    private StringBuilder buildMainQuery() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
                .append("new org.example.socialmediaspring.dto.user.UserDto( ")
                .append("u.id, ")
                .append("u.firstName, ")
                .append("u.lastName, ")
                .append("u.userName, ")
                .append("u.email, ")
                .append("u.role, ")
                .append("u.address, ")
                .append("u.created, ")
                .append("u.modified ")
                .append(") ")
                .append("FROM User u ");
        return sql;
    }

    private StringBuilder buildCountQuery() {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) ")
                .append(" from User u ");
        return sql;
    }

    private Pair<StringBuilder, Map<String,Object>> buildCondition(SearchUserRequest searchReq) {
        Map<String,Object> mapParameter = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        sql.append(" where 1 = 1 ");

        if (Objects.nonNull(searchReq.getEmail()) && !searchReq.getEmail().isEmpty()) {
            String email = searchReq.getEmail();
            sql.append("and u.email LIKE :email ");
            mapParameter.put("email", "%" + email + "%");
        }
        if (Objects.nonNull(searchReq.getRole())) {
            Role role = searchReq.getRole();
            sql.append("and u.role = :role ");
            mapParameter.put("role", role);
        }

        if (Objects.nonNull(searchReq.getId())) {
            Integer id = searchReq.getId();
            sql.append("and u.id = :id ");
            mapParameter.put("id", id);
        }

        return Pair.of(sql, mapParameter);
    }
    private void setParameters(Map<String,Object> mapParameter, Query query) {
        for (var entry : mapParameter.entrySet()) {
            query.setParameter(entry.getKey(),entry.getValue());
        }
    }
}
