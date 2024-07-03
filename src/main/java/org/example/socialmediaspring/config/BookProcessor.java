package org.example.socialmediaspring.config;

import org.example.socialmediaspring.entity.Book;
import org.springframework.batch.item.ItemProcessor;

public class BookProcessor implements ItemProcessor<Book, Book> {
    @Override
    public Book process(Book book) throws Exception {
        book.setId(null);
        return book;
    }
}
