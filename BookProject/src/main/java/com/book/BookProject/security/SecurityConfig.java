package com.book.BookProject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserServiceImpl userServiceImpl;


    public SecurityConfig(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
                .authorizeRequests(auth -> auth
                        .requestMatchers("/guest/**", "/css/**", "/js/**", "/images/**", "/webjars/**", "/static/**").permitAll() // 정적 리소스 경로 허용
                        .requestMatchers("/book", "/newbook", "/notablebooks", "/blogbestbooks", "/bookList", "/search", "/api/category", "/category/**", "/refundPayment").permitAll()  // API 경로 허용
                        .requestMatchers("/bestseller", "/bookdetail/**", "/mypage/**").permitAll()
                        .requestMatchers("/order/**").permitAll()
                        .requestMatchers("/", "/register", "/signup", "/login", "/findId", "/findPassword", "/IdCheck", "/NickCheck").permitAll()  // 추가
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                )
                .exceptionHandling(handling -> handling
                        .accessDeniedPage("/access-denied")
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userServiceImpl)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
}