package org.example.socialmediaspring.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="price")
    private Long price;

    @Column(name="discount_percentage")
    private Long discountPercentage;

    @Column(name="rating")
    private Double rating;

    @Column(name="stock")
    private Long stock;

    @Column(name="brand")
    private String brand;

    @Column(name="sku")
    private String sku;

    @Column(name="weight")
    private Double weight;

    @Column(name="warranty_information")
    private String warrantyInformation;

    @Column(name="shipping_information")
    private String shippingInformation;

    @Column(name="availability_status")
    private String availabilityStatus;

    @Column(name="return_policy")
    private String returnPolicy;

    @Column(name="minimum_order_quantity")
    private Integer minimumOrderQuantity;

    @Column(name="category_id")
    private Integer categoryId;

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

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnore
    @JoinTable(
            name = "category_products",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product",
            cascade = {
                    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    private  List<Files> files;
}
