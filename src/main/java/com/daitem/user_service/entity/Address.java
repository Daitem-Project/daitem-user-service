package com.daitem.user_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "Addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adr_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "adr_name")
    private String name;

    @NotNull
    @Column(name = "adr_number")
    private String addressNumber;

    @NotNull
    @Column(name = "adr_address")
    private String address;

    @NotNull
    @Column(name = "adr_detail")
    private String addressDetail;


    @JsonProperty("isDefault")
    @Column(name = "is_default")
    private Boolean isDefault = false;

    public void changeDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

}
