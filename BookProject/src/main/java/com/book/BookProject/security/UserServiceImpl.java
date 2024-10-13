package com.book.BookProject.security;

import com.book.BookProject.user.UserEntity;
import com.book.BookProject.user.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        // 권한 설정을 검토합니다.
        String role = "ROLE_" + userEntity.getAuthority().toUpperCase();

        return User.builder()
                .username(userEntity.getId())
                .password(userEntity.getPwd())
                .authorities("ROLE_" + userEntity.getAuthority())
                .build();
    }
}