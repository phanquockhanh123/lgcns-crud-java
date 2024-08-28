package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileRepository extends JpaRepository<Files, Long> {
    Files findByName(String name);
}
