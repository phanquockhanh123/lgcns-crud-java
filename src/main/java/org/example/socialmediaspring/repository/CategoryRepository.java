package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "SELECT * FROM categories where id = :id ", nativeQuery = true)
    Category findBookById(Integer id);
}
