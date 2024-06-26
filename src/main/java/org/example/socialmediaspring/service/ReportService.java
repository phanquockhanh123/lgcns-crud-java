package org.example.socialmediaspring.service;

import org.example.socialmediaspring.dto.book.BookBestSerllerRes;
import org.example.socialmediaspring.dto.user.BestCustomerRes;

import java.util.List;

public interface ReportService {
    List<BookBestSerllerRes> getBookBestSeller();

    List<BestCustomerRes> getListBestCustomer();
}
