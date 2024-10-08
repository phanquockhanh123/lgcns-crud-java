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
@Builder
@Entity
@Table(name = "book_categories")
public class BookCategory extends BaseEntity {
    private Integer categoryId;
    private Integer bookId;
}
