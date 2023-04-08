package com.mockproject.repository;

import com.mockproject.entity.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryTypeRepository extends JpaRepository<DeliveryType, Long> {

//    DeliveryType findByIdAndStatus(Long id, boolean status);

    Optional<List<DeliveryType>> findByStatus(boolean status);

    Optional<DeliveryType> findByIdAndStatus(Long deliveryId, boolean status);
}
