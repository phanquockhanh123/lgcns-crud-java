package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.dto.book.BookCategoryDto;
import org.example.socialmediaspring.dto.book_transactions.BookTransSearchRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransactionDto;
import org.example.socialmediaspring.dto.book_transactions.NoticeMailExpiredTimeDto;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.BookTransaction;
import org.example.socialmediaspring.entity.Category;
import org.example.socialmediaspring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookTransactionRepository extends JpaRepository<BookTransaction, Integer> {
    @Query(value = "SELECT * FROM book_transactions bt where bt.book_transaction_id = :transId and bt.status = 0", nativeQuery = true)
    BookTransaction findByBookTransIds(String transId);

    @Modifying
    @Transactional
    @Query("UPDATE BookTransaction bt SET bt.status = 1 WHERE bt.status = 0 AND bt.transactionId = :transactionId  ")
    void updateTransactionStatus(UUID transactionId);

//    @Query(value = "SELECT new org.example.socialmediaspring.dto.book_transactions.BookTransactionDto(bt.id, b.title, u.email, u.phone, bt.transactionId, bt.status, bt.quantity, bt.amount, bt.bonus, bt.startDate, bt.endDate, bt.returnDate) " +
//            " FROM BookTransaction bt " +
//            " JOIN Book b " +
//            " ON bt.bookId = b.id " +
//            " JOIN User u " +
//            " ON bt.userId = u.id " +
//        " WHERE (:status is null OR bt.status = :status) " +
//        " AND (:userIds is null OR bt.userId in (:userIds)) ")
//    Page<BookTransactionDto> searchBookTransByConds(Pageable pageable, Integer status, List<Integer> userIds);

    @Query(value = "SELECT new org.example.socialmediaspring.dto.book_transactions.NoticeMailExpiredTimeDto(" +
            " bt.id, " +
            " u.firstName, " +
            " u.lastName, " +
            " u.email, " +
            " u.phone, " +
            " bt.startDate, " +
            " bt.endDate, " +
            " bt.quantity, " +
            " bt.amount, " +
            " b.isbn, " +
            " b.title, " +
            " b.author, " +
            " b.price) " +
            " FROM BookTransaction bt " +
            " INNER JOIN User u " +
            " ON bt.userId = u.id " +
            " INNER JOIN Book b " +
            " ON bt.bookId = b.id " +
            " WHERE bt.status = 0 AND bt.endDate >= :yesterday ")
    List<NoticeMailExpiredTimeDto> getInfoUserExpiredTime(LocalDateTime yesterday);

}
