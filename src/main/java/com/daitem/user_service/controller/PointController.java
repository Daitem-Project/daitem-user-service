package com.daitem.user_service.controller;

import com.daitem.user_service.entity.PointHistory;
import com.daitem.user_service.entity.dto.PointRequest;
import com.daitem.user_service.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    /**
     * 포인트 조회
     */
    @GetMapping
    public ResponseEntity<Integer> getUserPoints(@AuthenticationPrincipal String username) {

        int point = pointService.readUserPoints(username);

        return ResponseEntity.ok(point);
    }

    /**
     * 유저 포인트내역 조회
     */
    @GetMapping("/history")
    public ResponseEntity<Page<PointHistory>> getPointHistoryList(@AuthenticationPrincipal String username,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Page<PointHistory> historyPage = pointService.getPointHistoryList(username, page, size);

        return ResponseEntity.ok(historyPage);
    }

    /**
     * 포인트 충전
     */
    @PostMapping("/charge")
    public ResponseEntity<Void> chargePoint(@AuthenticationPrincipal String username,
                                            @RequestBody PointRequest request) {

        pointService.chargeUserPoints(username, request);

        return ResponseEntity.ok().build();
    }

    /**
     * 포인트 차감
     */
    @PostMapping("/use")
    public ResponseEntity<Void> usePoint(@AuthenticationPrincipal String username,
                                         @RequestBody PointRequest request) {
        pointService.useUserPoints(username, request);

        return ResponseEntity.ok().build();
    }
}
