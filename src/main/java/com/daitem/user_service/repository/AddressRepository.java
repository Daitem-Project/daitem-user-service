package com.daitem.user_service.repository;


import com.daitem.user_service.entity.Address;
import com.daitem.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {


    long countByUser(User user);

    Optional<Address> findByUserAndIsDefaultTrue(User user);

    List<Address> findByUser(User user);
}
