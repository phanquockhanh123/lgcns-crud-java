package org.example.socialmediaspring.service;

import org.example.socialmediaspring.dto.book.BookBestSellerRes;
import org.example.socialmediaspring.dto.user.BestCustomerRes;

import java.util.List;

public interface ReportService {
//    List<BookBestSellerRes> getBookBestSeller();

    List<BestCustomerRes> getListBestCustomer();
}
