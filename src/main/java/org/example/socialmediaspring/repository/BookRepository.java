package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Page<Book> findAll(Pageable pageable);

    @Query(value = "SELECT b.id, b.title, b.author, b.price, b.description, b.isbn, c.name FROM books b " +
            " LEFT JOIN categories c " +
            " ON b.category_id = c.id " +
            " WHERE (:title is null OR b.title = :title) " +
            " AND (:author is null OR b.author = :author)", nativeQuery = true)
    Page<Book> findBooksByConds(Pageable pageable, String title, String author);
}
