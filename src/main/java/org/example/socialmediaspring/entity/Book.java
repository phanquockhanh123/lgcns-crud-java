package org.example.socialmediaspring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
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
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Long price;
    private Integer yearOfPublish;
    private Integer quantity = 0;
    private Integer quantityAvail = 0;
}
