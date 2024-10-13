package com.book.BookProject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/mypage")
@Controller
public class MypageController {

    @GetMapping
    public String mypage() {
        // 현재 인증 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자가 로그인하지 않았을 때는 anonymousUser로 간주됩니다.
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            // 로그인하지 않은 상태일 때 guest/mypage로 리다이렉트
            return "/login";
        } else {
            // 로그인된 상태일 때 member/mypage로 리다이렉트
            return "/member/mypage";
        }
    }

    @GetMapping("/order")
    public String order() {
        return "member/orderList";
    }


}