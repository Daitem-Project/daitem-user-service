package com.daitem.user_service.controller;

import com.daitem.user_service.entity.Address;
import com.daitem.user_service.entity.dto.AddressCreateRequest;
import com.daitem.user_service.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user/address")
public class AddressController {

    private final AddressService addressService;

    /**
     * 주소 생성
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addAddress(@RequestBody @Valid AddressCreateRequest request,
                                           @AuthenticationPrincipal String username) {

        addressService.createAddress(username, request);

        return ResponseEntity.ok().build();
    }

    /**
     * 주소 삭제
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("id") Long id,
                                              @AuthenticationPrincipal String username) throws AccessDeniedException {

        addressService.deleteAddress(username, id);

        return ResponseEntity.ok().build();
    }


    /**
     * 주소 목록
     */
    @GetMapping("/list")
    public ResponseEntity<List<Address>> getAddresses(@AuthenticationPrincipal String username){

        List<Address> addresses = addressService.getAddresses(username);

        return ResponseEntity.ok(addresses);
    }

    /**
     * 대표 주소 설정
     */
    @PatchMapping("/default/{id}")
    public ResponseEntity<Void> updateDefaultAddress(@PathVariable("id") Long id,
                                                     @AuthenticationPrincipal String username) throws AccessDeniedException {

        addressService.setMainAddress(username, id);

        return ResponseEntity.ok().build();
    }


}
