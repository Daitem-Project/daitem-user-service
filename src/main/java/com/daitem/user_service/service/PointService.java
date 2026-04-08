package com.daitem.user_service.service;

import com.daitem.user_service.entity.PointHistory;
import com.daitem.user_service.entity.User;
import com.daitem.user_service.entity.dto.PointRequest;
import com.daitem.user_service.exception.InsufficientPointException;
import com.daitem.user_service.repository.PointHistoryRepository;
import com.daitem.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

    private final PointHistoryRepository pointHistoryRepository;

    private final UserRepository userRepository;

    /**
     * 포인트 조회
     */
    @Transactional(readOnly = true)
    public int readUserPoints(String username){

        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("유저가 존재하지 않습니다."));

        return user.getPoint();
    }


    /**
     * 포인트 충전
     */
    public void chargeUserPoints(String username, PointRequest request){

        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("유저가 존재하지 않습니다."));

        int point = user.getPoint() + request.amount();
        user.setPoint(point);


        pointHistoryRepository.save(new PointHistory(user, request.amount(), request.reason(), point, LocalDateTime.now()));
    }

    /**
     * 포인트 차감
     */
    public void useUserPoints(String username, PointRequest request){

        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("유저가 존재하지 않습니다."));

        if(user.getPoint() < request.amount()){
            throw new InsufficientPointException("보유 포인트가 부족합니다.");
        }
        int point = user.getPoint() - request.amount();
        user.setPoint(point);

        pointHistoryRepository.save(new PointHistory(user, request.amount(), request.reason(), point, LocalDateTime.now()));
    }

    /**
     * 포인트 내역조회
     */
    @Transactional(readOnly = true)
    public Page<PointHistory> getPointHistoryList(String username, int page, int size){

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "changedAt"));

        return pointHistoryRepository.findByUserUsername(username, pageable);
    }



}
