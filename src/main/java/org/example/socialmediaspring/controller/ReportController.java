package org.example.socialmediaspring.controller;

import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.service.CategoryService;
import org.example.socialmediaspring.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/reports")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class ReportController {
    @Autowired
    ResponseFactory responseFactory;

    @Autowired
    ReportService reportService;

//    @GetMapping("/book-best-sellers")
//    @PreAuthorize("hasAnyAuthority('admin:read')")
//    public ResponseEntity getBookBestSeller() {
//        return responseFactory.success(reportService.getBookBestSeller());
//    }

    @GetMapping("/best-customers")
    @PreAuthorize("hasAnyAuthority('admin:read')")
    public ResponseEntity getBestCustomers() {
        return responseFactory.success(reportService.getListBestCustomer());
    }
}
