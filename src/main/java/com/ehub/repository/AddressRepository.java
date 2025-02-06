package com.ehub.repository;

import com.ehub.model.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    AddressEntity findByUserIdAndAddressType(Long userId, Integer addressType);
}
