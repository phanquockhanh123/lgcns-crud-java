package org.example.socialmediaspring.config;

import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.utils.JsonUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.scheduling.annotation.Async;

import java.util.Objects;
import java.util.UUID;

@Slf4j
public class BookProcessor implements ItemProcessor<Book, Book> {
    @Override
    public Book process(Book book) throws Exception {
        book.setId(null);
        book.setIsbn(UUID.randomUUID().toString());

        if (Objects.nonNull(book)) {
            log.info("CSV file import completed with: {}" + JsonUtils.objToString(book));
        }
        return book;
    }
}
