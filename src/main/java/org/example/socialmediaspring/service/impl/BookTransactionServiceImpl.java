package org.example.socialmediaspring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.dto.book_transactions.BookTransIdsRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransSearchRequest;
import org.example.socialmediaspring.dto.book_transactions.BookTransactionDto;
import org.example.socialmediaspring.dto.book_transactions.BookTransactionRequest;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.BookTransaction;
import org.example.socialmediaspring.entity.User;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.repository.BookRepository;
import org.example.socialmediaspring.repository.BookTransactionRepository;
import org.example.socialmediaspring.repository.UserRepository;
import org.example.socialmediaspring.service.BookTransactionService;
import org.example.socialmediaspring.utils.DateTimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookTransactionServiceImpl implements BookTransactionService {
    private final BookTransactionRepository bookTransactionRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    @Override
    public BookTransaction borrowBook(BookTransactionRequest request, Principal connectedUser) {

        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // get user info

        User userInfo = userRepository.findUsersByUsername(currentUser.getUsername());
        // check quantity avail book id > 0
        Book book = bookRepository.findBookById(request.getBookId());

        if (book.getQuantityAvail() <= 0) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "So luong sach khong du cung cap");
        }
        // check startDate less than endDate
        if (request.getStartDate() > request.getEndDate()) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "Thoi gian khong hop le");
        }
        BookTransaction bt = new BookTransaction();

        Long durationInHours = (request.getEndDate() - request.getStartDate()) / 3600000;

        bt.setBookId(request.getBookId());
        bt.setUserId(Math.toIntExact(userInfo.getId()));
        bt.setQuantity(request.getQuantity());
        bt.setStartDate(DateTimeUtils.convertTimestampToLocalDateTime(request.getStartDate()));
        bt.setEndDate(DateTimeUtils.convertTimestampToLocalDateTime(request.getEndDate()));
        bt.setAmount((int) (durationInHours * book.getPrice() * request.getQuantity()));
        bt.setBonus(0);
        bt.setStatus(0);
        bt.setTransactionId(UUID.randomUUID());

        // handle case quantity - 1 books table
        book.setQuantityAvail(book.getQuantityAvail() - 1);

        bookRepository.save(book);

        return bookTransactionRepository.save(bt);
    }

    @Override
    public BookTransaction returnBook(BookTransIdsRequest request) {

        //  check status == 2 => user have to pay bonus and amount
        // set status = 1,return_date

        List<BookTransaction> bookTransactions = bookTransactionRepository.findByBookTransIds(request.getBookTransIds());

        if (bookTransactions.isEmpty()) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "Khong co giao dich can thanh toan");
        }

        for (BookTransaction bookTransaction : bookTransactions) {

        }
        return null;
    }

    @Override
    public PageResponse<BookTransaction> getBookTransByConds(int page, int size, Integer status, List<String> tranIds) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());

        Page<BookTransaction> bookTrans = bookTransactionRepository.searchBooksByConds(pageable, status, tranIds);

        System.out.println("Result books: {}" + bookTrans);
        List<BookTransaction> booksTransResponse = bookTrans.stream().toList();
        return new PageResponse<>(
                booksTransResponse,
                bookTrans.getNumber(),
                bookTrans.getSize(),
                bookTrans.getTotalElements(),
                bookTrans.getTotalPages(),
                bookTrans.isFirst(),
                bookTrans.isLast());
    }


}
