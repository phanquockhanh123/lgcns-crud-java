package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "SELECT * FROM categories where id = :id ", nativeQuery = true)
    Category findBookById(Integer id);

    Boolean existsByName(String name);

    Optional<Category> findCategoryByName(String name);

    @Query(value = "SELECT id FROM categories", nativeQuery = true)
    List<Integer> findIdsCategory();

}
