package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.dto.book.BookCategoryDto;
import org.example.socialmediaspring.dto.book_transactions.BookTransSearchRequest;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.BookTransaction;
import org.example.socialmediaspring.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookTransactionRepository extends JpaRepository<BookTransaction, Integer> {
    @Query(value = "SELECT * FROM book_transactions b where b.book_transaction_id in (:bookTransId) ", nativeQuery = true)
    List<BookTransaction> findByBookTransIds(List<String> bookTransId);

    @Modifying
    @Transactional
    @Query("UPDATE BookTransaction bt SET bt.status = 1 WHERE bt.status = 0 AND bt.transactionId in (:transactionId) ")
    void updateTransactionStatus(List<UUID> transactionId);

        @Query(value = "SELECT new org.example.socialmediaspring.dto.book_transactions.BookTransactionDto(bt.id, b.title, u.email, u.phone, bt.transactionId, bt.status, bt.quantity, bt.amount, bt.bonus, bt.startDate, bt.endDate, bt.returnDate) " +
                " FROM BookTransaction bt " +
                " JOIN Book b " +
                " ON bt.bookId = b.id " +
                " JOIN User u " +
                " ON bt.userId = u.id " +
            " WHERE (:status is null OR bt.status = :status) " +
            " AND (:userIds is null OR bt.userId in (:userIds)) " +
            " AND (:tranIds is null OR bt.transactionId in (:tranIds)) ")
    Page<BookTransaction> searchBookTransByConds(Pageable pageable, Integer status, List<String> tranIds, List<Integer> userIds);

}
