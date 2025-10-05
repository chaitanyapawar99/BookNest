package com.cdac.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "book"})
public class Review extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Min(1)
    @Max(5)
    @Column(nullable=false)
    private int rating; // 1..5

    @CreationTimestamp
    @Column(name = "review_date", nullable = false, updatable = false)
    private LocalDateTime reviewDate;

    // Who wrote the review — lazy to avoid always fetching user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Which book was reviewed — lazy to avoid eager loading
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
