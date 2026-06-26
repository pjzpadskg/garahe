package com.pj.garahe.controller;

import com.pj.garahe.dto.*;
import com.pj.garahe.service.ParkingSpaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/parking-spaces")
@RequiredArgsConstructor
public class ParkingSpaceController {
    private final ParkingSpaceService parkingSpaceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParkingSpaceGeneralItem create(@Valid @RequestBody ParkingSpaceCreationRequest request) {
        return parkingSpaceService.create(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPage<ParkingSpaceItem> get(Pageable pageable) {
        return parkingSpaceService.get(pageable);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ParkingSpaceGeneralItem update(@Valid @RequestBody ParkingSpaceUpdateRequest request) {
        return parkingSpaceService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        parkingSpaceService.delete(id);
    }
}
