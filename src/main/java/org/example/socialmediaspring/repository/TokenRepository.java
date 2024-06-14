package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.entity.Token;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(Integer id);

    Optional<Token> findByToken(String token);

    @EntityGraph(attributePaths = {"user"})
    @Modifying
    @Query("DELETE FROM Token t WHERE t.user.id in (:userId)")
    void deleteByUserId(List<Long> userId);
}
