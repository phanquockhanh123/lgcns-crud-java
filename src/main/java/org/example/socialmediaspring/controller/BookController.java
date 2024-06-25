package org.example.socialmediaspring.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.dto.book.CUBookRequest;
import org.example.socialmediaspring.dto.book.SearchBookRequest;
import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.book.BookRequest;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin/books")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
public class BookController {
    private final BookService bookService;

    private final ResponseFactory responseFactory;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'manager:create')")
    public ResponseEntity saveBook(
             @Valid @ModelAttribute CUBookRequest request,
             @RequestPart("filePath") MultipartFile file
            ) throws IOException {
        return responseFactory.success(bookService.saveBook(request, file));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'manager:update')")
    public ResponseEntity updateBook(
            @PathVariable Integer id,
            @ModelAttribute CUBookRequest request,
            @RequestPart("filePath") MultipartFile file) throws  IOException {
        return responseFactory.success(bookService.updateBook(id, request, file));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'manager:read', 'user:read')")
    public ResponseEntity getBook(@PathVariable Integer id) {
        return responseFactory.success(bookService.getBookById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public  ResponseEntity deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return responseFactory.success(null);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'manager:read', 'user:read')")
    public ResponseEntity searchBooksByConds(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "limit", defaultValue = "20", required = false) int limit,
            @RequestParam(name = "get_total_count", required = false) Boolean getTotalCount,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "yearFrom", required = false) Integer yearFrom,
            @RequestParam(name = "yearTo", required = false) Integer yearTo
    ) {
        return responseFactory.success(bookService.searchAllBooks(new SearchBookRequest(limit, page, getTotalCount, title, author, yearFrom, yearTo)));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('admin:delete', 'manager:delete')")
    public ResponseEntity deleteBooksByIds(@RequestBody IdsRequest ids) {

        return responseFactory.success(bookService.deleteBooksByIds(ids));
    }

    @GetMapping("/cates/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'manager:read')")
    public ResponseEntity getCategoriesByBookId(@Valid @PathVariable Integer id) {
        return responseFactory.success(bookService.getCategoriesByBookId(id));
    }

}
