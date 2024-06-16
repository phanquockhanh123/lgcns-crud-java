package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Integer> {
    @Transactional
    @Query(value = "DELETE FROM BookCategory bc where bc.bookId in (:bookId)")
    void deleteAllByBookId(List<Integer> bookId);
}
