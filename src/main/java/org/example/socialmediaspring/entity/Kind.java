package org.example.socialmediaspring.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "kinds")
public class Kind {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="thumbnail")
    private String thumbnail;

    @Column(name="description")
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

    @ManyToMany(mappedBy = "kinds", fetch =  FetchType.LAZY)
    @JsonBackReference
    private Set<Product> products;


}
