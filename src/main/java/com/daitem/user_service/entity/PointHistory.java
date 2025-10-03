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
@Table(name = "point_history")
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_history_id")
    private Long id;

    @NotNull
    @Column(name = "point_history_changed")
    private int changed;

    @NotNull
    @Column(name = "point_history_reason")
    private String reason;

    @NotNull
    @Column(name = "point_history_amount")
    private int amount;

    @NotNull
    @Column(name = "point_changed_at")
    private LocalDateTime changedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @PrePersist
    protected void onCreate(){
        this.changedAt = LocalDateTime.now();
    }

}
