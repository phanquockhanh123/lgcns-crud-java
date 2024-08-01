package org.example.socialmediaspring.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
    private Long rating;

    @Column(name="stock")
    private Long stock;

    @Column(name="brand")
    private String brand;

    @Column(name="sku")
    private String sku;

    @Column(name="weight")
    private Long weight;

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

    @Column(name="thumbnail")
    private String thumbnail;

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

    @ManyToMany(fetch =  FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinTable(name="kind_products",
        joinColumns = {
            @JoinColumn(name="product_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name="kind_id", referencedColumnName = "id")
        })
    @JsonManagedReference
    private Set<Kind> kinds;

}
