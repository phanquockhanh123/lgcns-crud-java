package org.example.socialmediaspring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.dto.book_transactions.BookTransactionDto;
import org.example.socialmediaspring.dto.book_transactions.BookTransactionRequest;
import org.example.socialmediaspring.entity.BookTransaction;
import org.example.socialmediaspring.repository.BookRepository;
import org.example.socialmediaspring.repository.BookTransactionRepository;
import org.example.socialmediaspring.service.BookTransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookTransactionServiceImpl implements BookTransactionService {
    private final BookTransactionRepository bookTransactionRepository;

    @Override
    public BookTransaction borrowBook(BookTransactionRequest request) {
        BookTransaction bt = new BookTransaction();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        long  amount = (Timestamp.valueOf(request.getEndDate()).getTime() - Timestamp.valueOf(request.getEndDate()).getTime()) / 3600000;
        bt.setBookId(request.getBookId());
        bt.setUserId(request.getUserId());
        bt.setQuantity(request.getQuantity());
        bt.setStartDate(LocalDateTime.parse(request.getStartDate(), formatter));
        bt.setEndDate(LocalDateTime.parse(request.getEndDate(), formatter));
        bt.setAmount((int) (amount * request.getPrice()));
        bt.setBonus(0);
        bt.setStatus(0);

        bookTransactionRepository.save(bt);

        return bookTransactionRepository.save(bt);
    }
}
