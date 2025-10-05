package com.cdac.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "books")
@ToString(exclude = {"carts", "reviews"})
public class Book extends BaseEntity {

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 150)
    private String author;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(length = 500)
    private String imagePath;

    @Column(length = 500)
    private String filePath; // path to local storage or cloud url

    private boolean approved = false;

    private boolean available = true;

    // Many books belong to one category (lazy loaded)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // Seller - Many books can be uploaded by one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    // Book can be in many carts; owning side is Cart (you had mappedBy="books" previously)
    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    private List<Cart> carts = new ArrayList<>();

    // Book has many reviews; cascade so reviews are removed if book is deleted
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();
}
