package com.pj.garahe.service;

import com.pj.garahe.dto.*;
import com.pj.garahe.entity.ParkingSpace;
import com.pj.garahe.entity.User;
import com.pj.garahe.exception.ResourceNotFoundException;
import com.pj.garahe.exception.UnauthorizedException;
import com.pj.garahe.repo.ParkingSpaceRepository;
import com.pj.garahe.security.SecurityUtil;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParkingSpaceService {
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final SecurityUtil securityUtil;
    private final EntityManager entityManager;

    private ParkingSpaceGeneralItem toGeneralItem(ParkingSpace space) {
        User owner = space.getOwner();
        BaseUserDetail detail = BaseUserDetail.builder()
                .id(owner.getId())
                .email(owner.getEmail())
                .contactNumber(owner.getContactNumber())
                .firstName(owner.getFirstName())
                .lastName(owner.getLastName())
                .build();

        return ParkingSpaceGeneralItem.builder()
                .id(space.getId())
                .address(space.getAddress())
                .price(space.getPrice())
                .owner(detail)
                .build();
    }

    public ParkingSpaceGeneralItem create(ParkingSpaceCreationRequest request) {
        UUID currentUserId = securityUtil.getCurrentUserClaims().id();
        User currentUser = entityManager.getReference(User.class, currentUserId);

        ParkingSpace space = new ParkingSpace();
        space.setAddress(request.address());
        space.setPrice(request.price());
        space.setOwner(currentUser);
        return toGeneralItem(parkingSpaceRepository.save(space));
    }

    public CustomPage<ParkingSpaceItem> get(Pageable pageable) {
        UUID currentUserId = securityUtil.getCurrentUserClaims().id();
        Page<ParkingSpaceItem> items = parkingSpaceRepository.findAllByOwnerId(currentUserId, pageable).map(item ->
                ParkingSpaceItem.builder()
                        .id(item.getId())
                        .address(item.getAddress())
                        .price(item.getPrice())
                        .build()
        );
        return new CustomPage<>(items);
    }

    @Transactional
    public ParkingSpaceGeneralItem update(ParkingSpaceUpdateRequest request) {
        UUID currentUserId = securityUtil.getCurrentUserClaims().id();
        User currentUser = entityManager.getReference(User.class, currentUserId);
        ParkingSpace space = parkingSpaceRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("Parking space does not exist."));
        if (currentUser.getRole() == User.Role.USER && space.getOwner() != currentUser)
            throw new UnauthorizedException("Unauthorized update of non-owned parking space.");

        if (request.address() != null && !request.address().isBlank()) space.setAddress(request.address());
        if (request.price() != null) space.setPrice(request.price());
        return toGeneralItem(parkingSpaceRepository.save(space));
    }

    @Transactional
    public void delete(UUID parkingId) {
        UUID currentUserId = securityUtil.getCurrentUserClaims().id();
        User currentUser = entityManager.getReference(User.class, currentUserId);
        ParkingSpace space = parkingSpaceRepository.findById(parkingId)
                .orElseThrow(() -> new ResourceNotFoundException("Parking space does not exist."));
        if (currentUser.getRole() == User.Role.USER && space.getOwner() != currentUser)
            throw new UnauthorizedException("Unauthorized delete of non-owned parking space.");

        parkingSpaceRepository.delete(space);
    }
}
