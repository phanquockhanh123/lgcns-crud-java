package org.example.socialmediaspring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.dto.book.BookBestSellerRes;
import org.example.socialmediaspring.dto.user.BestCustomerRes;
import org.example.socialmediaspring.repository.BookRepository;
import org.example.socialmediaspring.repository.UserRepository;
import org.example.socialmediaspring.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final BookRepository bookRepository;

    private final UserRepository userRepository;

//    @Override
//    public List<BookBestSellerRes> getBookBestSeller() {
//
//        List<BookBestSellerRes> listBooks = bookRepository.getBookBestSeller();
//        return listBooks;
//    }

    @Override
    public List<BestCustomerRes> getListBestCustomer() {
        List<BestCustomerRes> listCustomers = userRepository.getBestCustomer();

        return listCustomers;
    }
}
