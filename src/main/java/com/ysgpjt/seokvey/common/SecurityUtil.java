package com.ysgpjt.seokvey.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

public class SecurityUtil {
    /**
     * 현재 로그인된 사용자의 ID 또는 username 반환
     * 로그인하지 않은 경우 null 반환
     */
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // 익명 사용자
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername(); // 또는 userDetails.getId()로 맞춤
        } else if (principal instanceof String username) {
            return username; // principal이 단순 String일 경우
        }
        return null;
    }

    /**
     * 로그인 여부 체크
     */
    public static boolean isLoggedIn() {
        return !Objects.equals(getCurrentUserId(), "anonymousUser");
    }

}
