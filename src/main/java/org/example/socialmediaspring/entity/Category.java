package org.example.socialmediaspring.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.socialmediaspring.common.BaseEntity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name= "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;

    @Column(name = "created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;

    @Column(name = "modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modified;

    @PrePersist
    private void onCreate() {
        this.created = new Date();
        this.modified = new Date();
    }

    @PreUpdate
    private  void onUpdate() {
        this.modified = new Date();
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnore
    @JoinTable(
            name = "category_products",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();
}
