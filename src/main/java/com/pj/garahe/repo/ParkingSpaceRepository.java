package com.pj.garahe.repo;

import com.pj.garahe.entity.ParkingSpace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, UUID> {
    Page<ParkingSpace> findAllByOwnerId(UUID ownerId, Pageable pageable);
}
