package com.pj.garahe.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record CustomPage<T>(
        List<T> content,
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages
) {
    public CustomPage(Page<T> page) {
        this(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
