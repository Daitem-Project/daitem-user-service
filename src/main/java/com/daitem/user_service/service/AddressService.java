package com.daitem.user_service.service;

import com.daitem.user_service.entity.Address;
import com.daitem.user_service.entity.User;
import com.daitem.user_service.entity.dto.AddressCreateRequest;
import com.daitem.user_service.repository.AddressRepository;
import com.daitem.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    /**
     * 주소등록
     */

    public void createAddress(String username, AddressCreateRequest request){

        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("유저가 존재하지 않습니다" + username));

        long count = addressRepository.countByUser(user);

        //주소 최대 5개 설정
        if(count >= 5){
            throw new IllegalArgumentException("최대 5개의 주소만 등록 가능합니다.");
        }

        Address address = Address.builder()
                .user(user)
                .name(request.name())
                .addressNumber(request.addressNumber())
                .address(request.address())
                .addressDetail(request.addressDetail())
                .build();

        if(count == 0){
            address.changeDefault(true);
        }

        addressRepository.save(address);

    }

    /**
     * 주소 삭제
     */

    public void deleteAddress(String username, Long addressId) throws AccessDeniedException {
        Address address = addressRepository.findById(addressId).orElseThrow(()-> new IllegalArgumentException("유효하지않은 주소입니다"));

        //검증
        if(!address.getUser().getUsername().equals(username)){
            throw new AccessDeniedException("본인 주소만 삭제 가능합니다.");
        }

        addressRepository.delete(address);
    }

    /**
     * 대표주소설정
     */
    public void setMainAddress(String username, Long addressId) throws AccessDeniedException {

        Address address = addressRepository.findById(addressId).orElseThrow(()-> new IllegalArgumentException("유효하지않은 주소입니다"));

        //검증
        if(!address.getUser().getUsername().equals(username)){
            throw new AccessDeniedException("본인 주소만 설정 가능합니다.");
        }

        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("유저가 존재하지 않습니다" + username));

        //현재 대표주소
        Optional<Address> current = addressRepository.findByUserAndIsDefaultTrue(user);

        current.ifPresent(addr -> addr.changeDefault(false));

        address.changeDefault(true);
    }

    /**
     * 주소목록
     */
    public List<Address> getAddresses(String username){

        User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("유저가 존재하지 않습니다" + username));


        return addressRepository.findByUser(user);
    }



}
