package com.daitem.user_service.entity;

import com.daitem.user_service.entity.dto.UserUpdateRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Setter
    @Column(name = "user_name")
    private String username;

    @Setter
    @Column(name = "user_email")
    private String email;

    //@NotNull //소셜로그인
    @Setter
    @Column(name = "user_password")
    private String password;

    @Setter
    @Column(name = "name")
    private String name;

    @Setter
    @Column(name = "user_nickname")
    private String nickname;

    @Setter
    @Column(name = "user_number")
    private String phoneNumber;

    @Setter
    @Column(name = "user_profile_url")
    private String profileUrl;

    @NotNull
    @Column(name = "user_rating")
    private double rating;

    @NotNull
    @Column(name = "user_point")
    private int point;

    @NotNull
    @CreatedDate
    @Column(name = "user_created_at")
    private LocalDateTime createdAt;


    @LastModifiedDate
    @Column(name = "user_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "user_role")
    @Setter
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "is_locked")
    private boolean isLocked;

    @Column(name = "is_social")
    private boolean isSocial;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "social_provider_type")
    private SocialType socialType;



    @PrePersist
    protected void onPrePersist(){

    }

    /**
     * 회원정보수정
     **/

    public void updateUser(UserUpdateRequest request){
        this.name=request.name();
        this.nickname = request.nickname();
        this.phoneNumber=request.phoneNumber();
        this.profileUrl = request.profileUrl();
    }

    /**
     * 회원가입
     **/

    public static User createUser(String username, String email, String password, String name, String nickname, String phoneNumber, String profileUrl){
        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.name = name;
        user.nickname = nickname;
        user.phoneNumber = phoneNumber;
        user.profileUrl = profileUrl;

        user.isSocial = false;
        user.isLocked = false;
        user.role = UserRole.USER;
        user.rating = 0.0;
        user.point = 0;
        return user;

    }

//    @Builder(builderMethodName = "standard")
//    public User(String username, String email, String password, String name, String nickname, String phoneNumber, String profileUrl) {
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.name = name;
//        this.nickname = nickname;
//        this.phoneNumber = phoneNumber;
//        this.profileUrl = profileUrl;
//
//        //기본값
//        this.rating = 0.0;
//        this.point = 0;
//        this.role = UserRole.USER;
//        this.isLocked = false;
//        this.isSocial = false;
//        this.socialType = null;
//    }

//    @Builder(builderMethodName = "social")
//    public User(String username, String email, String name, String nickname, String phoneNumber, String profileUrl, SocialType socialType) {
//        this.username = username;
//        this.nickname = nickname;
//        this.profileUrl = profileUrl;
//        this.socialType = socialType;
//        this.email = email;
//        this.name = name;
//        this.phoneNumber = phoneNumber;
//
//        //기본값 TODO(리팩토링필요, builder사용시 생성자값 적용이 안됨)
//        this.isSocial = true;
//        this.rating = 0.0;
//        this.point = 0;
//        this.role = UserRole.USER;
//        this.isLocked = false;
//        this.password = "";
//    }

    public static User createSocialUser(String username, String email, String name,
                                        String nickname, String phoneNumber,
                                        String profileUrl, SocialType socialType) {
        User user = new User();
        user.username = username;
        user.email = email;
        user.name = name;
        user.nickname = nickname;
        user.phoneNumber = phoneNumber;
        user.profileUrl = profileUrl;
        user.socialType = socialType;

        user.isSocial = true;
        user.isLocked = false;
        user.role = UserRole.USER;
        user.password = "";
        user.rating = 0.0;
        user.point = 0;
        return user;
    }
}
