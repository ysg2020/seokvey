package com.ysgpjt.seokvey.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> items;
    int page;
    int size;
    int totalCount;
    int totalPages;
}
