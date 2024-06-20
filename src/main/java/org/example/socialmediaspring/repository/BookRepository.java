package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.dto.book.BookCategoryDto;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.User;
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

//    @Query(value = "SELECT new org.example.socialmediaspring.dto.book.BookCategoryDto(b.id, b.title, b.author, b.isbn, b.price, b.quantity, b.quantityAvail, b.yearOfPublish, b.created, b.modified)  FROM Book b " +
//            " JOIN BookCategory bc " +
//            " ON bc.bookId = b.id " +
//            " JOIN Category c " +
//            " ON bc.categoryId = c.id " +
//            " WHERE (:title is null OR b.title LIKE CONCAT('%', :title, '%')) " +
//            " AND (:yearFrom is null OR b.yearOfPublish >= :yearFrom) " +
//            " AND (:yearTo is null OR b.yearOfPublish <= :yearTo) " +
//            " AND (:author is null OR b.author LIKE CONCAT('%', :author, '%'))")
//    Page<BookCategoryDto> searchBooksByConds(Pageable pageable, String title, String author, List<Integer> cateIds, Integer yearFrom, Integer yearTo);

    @Query(value = "SELECT new org.example.socialmediaspring.dto.book.BookResponse(b.id,LISTAGG(c.name, ',') WITHIN GROUP (ORDER BY c.name) AS categoryNames," +
        " b.title, b.author, b.isbn, b.description, b.price, b.yearOfPublish, b.quantity," +
        " b.quantityAvail, b.created, b.modified)  FROM Book b " +
        " INNER JOIN BookCategory bc " +
        " ON bc.bookId = b.id " +
        " INNER JOIN Category c " +
        " ON bc.categoryId = c.id " +
        " WHERE (:bookId is null OR b.id = :bookId) ")
    BookResponse findBooksInfoById(Integer bookId);

    @Query("SELECT MAX(CAST(SUBSTRING(b.title, 16) AS int)) FROM Book b WHERE b.title LIKE 'title-book-bulk-%'")
    Integer findMaxTitleNumber();

    @Query(value = "SELECT * FROM books b where b.id = :bookId ", nativeQuery = true)
    Book findBookById(Integer bookId);


}
