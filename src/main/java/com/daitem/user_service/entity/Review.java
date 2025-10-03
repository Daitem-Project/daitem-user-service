package com.daitem.user_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluand_id", nullable = false)
    private User evaluand;

    @NotNull
    @Column(name = "review_content")
    private String content;

    @NotNull
    @Column(name = "review_rating")
    private double rating;

    @NotNull
    @Column(name = "review_created_at")
    private LocalDateTime createdAt;

    //item-service
    private Long itemId;
}
