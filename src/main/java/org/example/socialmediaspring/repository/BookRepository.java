package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.dto.BookCategoryDto;
import org.example.socialmediaspring.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Page<Book> findAll(Pageable pageable);

    Boolean existsByTitle(String title);

    Optional<Book> findBookByTitle(String title);

    @Query(value = "SELECT * FROM books b " +
            " WHERE (:title is null OR b.title LIKE :title) " +
            " AND (:author is null OR b.author LIKE :author)", nativeQuery = true)
    Page<Book> findBooksByConds(Pageable pageable, String title, String author);

    @Query(value = "SELECT new org.example.socialmediaspring.dto.BookCategoryDto(b.id, b.title, b.author, b.isbn, b.price, c.name, b.created)  FROM Book b " +
            " LEFT JOIN Category c " +
            " ON b.categoryId = c.id " +
            " WHERE (:title is null OR b.title LIKE CONCAT('%', :title, '%')) " +
            " AND (COALESCE(:cateIds, NULL) IS NULL OR b.categoryId in (:cateIds)) " +
            " AND (:author is null OR b.author LIKE CONCAT('%', :author, '%'))")
    Page<BookCategoryDto> searchBooksByConds(Pageable pageable, String title, String author, List<Integer> cateIds);

}
