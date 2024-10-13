package com.book.BookProject.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupServiceImpl implements SignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void registerUser(UserDTO userDTO) {
        if (!isIdUnique(userDTO.getId())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        if (!isNickUnique(userDTO.getNick())) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }
        if (!userDTO.getPwd().equals(userDTO.getPwdconfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String authority = "USER";
        if (userDTO.getId().equalsIgnoreCase("admin")) {
            authority = "ADMIN";
        }

        UserEntity userEntity = UserEntity.builder()
                .id(userDTO.getId())
                .nick(userDTO.getNick())
                .pwd(passwordEncoder.encode(userDTO.getPwd())) // 비밀번호 암호화
                .name(userDTO.getName())
                .phone(userDTO.getPhone())
                .email(userDTO.getEmail())
                .zipcode(userDTO.getZipcode())
                .address(userDTO.getAddress())
                .detailAddress(userDTO.getDetailAddress())
                .authority(authority)
                .enabled(1)
                .build();

        userRepository.save(userEntity);
    }
    @Override
    public boolean isIdUnique(String id) {
        return !userRepository.existsById(id);
    }

    @Override
    public boolean isNickUnique(String nick) {
        return !userRepository.existsByNick(nick);
    }
}