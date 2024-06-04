package org.example.socialmediaspring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.socialmediaspring.common.BaseEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "books")
public class Book extends BaseEntity {
    private Integer categoryId;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Long price;

}
