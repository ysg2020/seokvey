package com.ysgpjt.seokvey.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponse<T> {

    private boolean success;                // 성공 여부
    private T data;                         // 응답 데이터
    private String path;                    // 요청 URI

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;       // 응답 생성 시각

    public static <T> ApiResponse<T> ok(T data, String path) {
        ApiResponse<T> res = new ApiResponse<>();
        res.success = true;
        res.data = data;
        res.path = path;
        res.timestamp = LocalDateTime.now();
        return res;
    }

}
