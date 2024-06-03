package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.entity.BookEntity;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    Page<BookEntity> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM books b WHERE b.title = :title AND b.author_name = :author", nativeQuery = true)
    Page<BookEntity> findBooksByConds(Pageable pageable, String title, String author);
}
