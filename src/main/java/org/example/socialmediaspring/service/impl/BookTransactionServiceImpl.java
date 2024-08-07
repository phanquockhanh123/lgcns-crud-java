package org.example.socialmediaspring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.PageNewResponse;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.constant.Common;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.dto.book_transactions.*;
import org.example.socialmediaspring.dto.emails.EmailDetails;
import org.example.socialmediaspring.dto.notifications.BookBorrowedNotification;
import org.example.socialmediaspring.dto.user.BestCustomerRes;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.BookCategory;
import org.example.socialmediaspring.entity.BookTransaction;
import org.example.socialmediaspring.entity.User;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.listener.WebSockerHandler;
import org.example.socialmediaspring.repository.BookRepository;
import org.example.socialmediaspring.repository.BookTransactionCustomRepository;
import org.example.socialmediaspring.repository.BookTransactionRepository;
import org.example.socialmediaspring.repository.UserRepository;
import org.example.socialmediaspring.service.BookTransactionService;
import org.example.socialmediaspring.service.EmailService;
import org.example.socialmediaspring.utils.DateTimeUtils;
import org.example.socialmediaspring.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.*;
import org.springframework.data.util.Pair;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketHandler;

import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookTransactionServiceImpl implements BookTransactionService {
    private final BookTransactionRepository bookTransactionRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    private final BookTransactionCustomRepository bookTransactionCustomRepository;

    @Autowired
    EmailService emailService;

    private final WebSockerHandler webSocketHandler;


    @Override
    public BookTransaction borrowBook(BookTransactionRequest request, Principal connectedUser) {

        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // get user info

        User userInfo = userRepository.findUsersByUsername(currentUser.getUsername());
        // check quantity avail book id > 0
        Book book = bookRepository.findBookById(request.getBookId());

        // check book exists
        if (book == null) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "Book no exists");
        }

        if (book.getQuantityAvail() <= 0) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "So luong sach khong du cung cap");
        }
        // check startDate less than endDate
        Long startDate = DateTimeUtils.convertToTimestamp(request.getStartDate());
        Long endDate = DateTimeUtils.convertToTimestamp(request.getEndDate());
        if (startDate > endDate) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "Thoi gian khong hop le");
        }
        BookTransaction bt = new BookTransaction();

        Long durationInHours = (endDate - startDate) /  86400000;

        bt.setBookId(request.getBookId());
        bt.setUserId(Math.toIntExact(userInfo.getId()));
        bt.setQuantity(request.getQuantity());
        bt.setStartDate(request.getStartDate());
        bt.setEndDate(request.getEndDate());
        bt.setAmount((int) (durationInHours * book.getPrice() * request.getQuantity()));
        bt.setBonus(0);
        bt.setStatus(0);
        bt.setTransactionId(UUID.randomUUID());

        // handle case quantity - quantity borrow books table
        book.setQuantityAvail(book.getQuantityAvail() - request.getQuantity());

        Book bookUpdate = bookRepository.save(book);

        BookTransaction bookTrans = bookTransactionRepository.save(bt);

        // push notification
        String notification = "Book " + bookUpdate.getTitle() + " by " + userInfo.getFirstName() + " " + userInfo.getLastName() + " at  time " + DateTimeUtils.localDateTime(LocalDateTime.now());

        log.info("Borrow book success! with notification: " + notification);

        try {
            webSocketHandler.sendNotification(notification);
        } catch (IOException e) {
            e.printStackTrace();
            log.info(String.valueOf(e));
        }


        return bookTrans;
    }

    @Override
    public BookTransactionDetailDto returnBook(Integer id) {

        //  check status == 2 => user have to pay bonus and amount
        // set status = 1,return_date

        BookTransaction existsBookTrans = bookTransactionRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCodeConst.INVALID_INPUT, "Book  not found with id " + id));

        existsBookTrans.setStatus(1);
        existsBookTrans.setReturnDate(LocalDateTime.now());

        // check returnDate > endDate
        if (existsBookTrans.getReturnDate().isAfter(existsBookTrans.getEndDate())) {
            Long  endDate = DateTimeUtils.convertToTimestamp(existsBookTrans.getEndDate());
            Long  returnDate = DateTimeUtils.convertToTimestamp(existsBookTrans.getReturnDate());

            Book book = bookRepository.findBookById(existsBookTrans.getBookId());

            Long expiredDay = (returnDate - endDate) /  86400000;
            System.out.println("Expired day: " + expiredDay);

            existsBookTrans.setBonus((int) (expiredDay * existsBookTrans.getQuantity() * book.getPrice()));
        }

        BookTransaction bk = bookTransactionRepository.save(existsBookTrans);

        // return quantity available table books
        Book book = bookRepository.findBookById(bk.getBookId());
        System.out.println("quantity return" + existsBookTrans.getQuantity());
        System.out.println("quantity avail" + book.getQuantityAvail());
        book.setQuantityAvail(book.getQuantityAvail() + existsBookTrans.getQuantity());

        bookRepository.save(book);

        return BookTransactionDetailDto.builder()
                .bookId(bk.getBookId())
                .transactionId(bk.getTransactionId())
                .userId(bk.getUserId())
                .startDate(bk.getStartDate())
                .endDate(bk.getEndDate())
                .returnDate(bk.getReturnDate())
                .bonus(bk.getBonus())
                .amount(bk.getAmount())
                .status(bk.getStatus())
                .quantity(bk.getQuantity())
                .build();
    }

    @Override
    public PageNewResponse<BookTransactionDto> getBookTransByConds(SearchBookTransactionDto searchReq,  Principal connectedUser) {

        log.info("start search book transactions. body: {}", JsonUtils.objToString(searchReq));
        PageRequest pageable = Common.getPageRequest(searchReq.getPage() - 1, searchReq.getLimit(), null);

        Pair<Long, List<BookTransactionDto>> bookTransData = bookTransactionCustomRepository.searchBookTransByConds(searchReq, pageable, connectedUser);
        Long countBooks = bookTransData.getFirst();
        List<BookTransactionDto> listBookTrans = bookTransData.getSecond();


        Page<BookTransactionDto> pageBookDto = new PageImpl<>(listBookTrans, pageable, countBooks);

        PageNewResponse<BookTransactionDto> ib = PageNewResponse.<BookTransactionDto>builder()
                .data(listBookTrans)
                .build();

        if (Objects.nonNull(searchReq.getGetTotalCount()) && Boolean.TRUE.equals(searchReq.getGetTotalCount())) {
            ib.setPagination(this.buildPagination(pageBookDto.getSize(), pageBookDto.getTotalPages(),
                    pageBookDto.getNumber() + 1, pageBookDto.getTotalElements()));
        }

        log.info("end ...");
        return ib;
    }

    @Override
    public void sendMaiNoticeOTBorrowBook() {
        // check list book transaction about 1 days loan borrow expired with status = 0, now() > end_date  - 1days
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        List<NoticeMailExpiredTimeDto> userInfos = bookTransactionRepository.getInfoUserExpiredTime(yesterday);

        log.info("get start send email for list user {}", JsonUtils.objToString(userInfos));
        // send mail to alert user
        for (NoticeMailExpiredTimeDto user : userInfos) {
            this.sendMailNoticeCommon(user);
        }

    }

    @Override
    public void sendMailNoticeSingle(Integer id) {
        NoticeMailExpiredTimeDto userInfo = bookTransactionRepository.getInfoTransById(id);
        log.info("Get start send email for list user {}", JsonUtils.objToString(userInfo));

        this.sendMailNoticeCommon(userInfo);
    }



    private void sendMailNoticeCommon(NoticeMailExpiredTimeDto user) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("LOAN BORROW EXPIRED")
                .messageBody("LOAN EXPIRED TIME. Your account details:\n"
                        + "Account name: " + user.getFirstName() + " " + user.getLastName()
                        + "\n Email: " + user.getEmail() + "\n Phone: " + user.getPhone()
                        + "\n\n Your books info:"
                        + "\n Book isbn: " + user.getBookIsbn()
                        + "\n Book title: " + user.getBookTitle()
                        + "\n Book author: " + user.getBookAuthor()
                        + "\n Book price: " + user.getBookPrice()
                        + "\n\n Your books borrow :"
                        + "\n Quantity: " + user.getQuantity()
                        + "\n Start date: " + user.getStartDate()
                        + "\n End date: " + user.getEndDate()
                        + "\n Amount: " + user.getAmount()
                )
                .build();
        emailService.sendEmailAlert(emailDetails);
    }


    private Map<String, Long> buildPagination(Integer limit, Integer totalPage, Integer currentPage, Long totalRecord){
        log.info("start buildPagination ...");

        Map<String, Long> pagination = new HashMap<>();
        pagination.put("limit", Long.valueOf(limit));
        pagination.put("total_page", Long.valueOf(totalPage));
        pagination.put("current_page", Long.valueOf(currentPage));
        pagination.put("total_record", totalRecord);

        log.info("end buildPagination ...");
        return pagination;
    }

}
