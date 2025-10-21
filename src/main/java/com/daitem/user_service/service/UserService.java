package com.daitem.user_service.service;

import com.daitem.user_service.entity.User;
import com.daitem.user_service.entity.dto.UserCreatRequest;
import com.daitem.user_service.entity.dto.UserUpdateRequest;

import java.util.List;

public interface UserService {

    //생성
    User createUser(UserCreatRequest request);

    //수정
    User updateUser(UserUpdateRequest request);

    //단일조회
    User getUser(long id);

    //전체조회
    List<User> getUsers();

    //삭제
    void deleteUser(long id);


}
