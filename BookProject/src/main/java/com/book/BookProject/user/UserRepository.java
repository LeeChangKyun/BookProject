package com.book.BookProject.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsById(String id);
    boolean existsByNick(String nick);

    Optional<UserEntity> findById(String id);

    // 이름과 전화번호로 사용자 찾기 메서드 추가
    Optional<UserEntity> findByNameAndPhone(String name, String phone);

    Optional<UserEntity> findByNameAndEmailAndId(String name, String email, String id);

    Optional<UserEntity> findByEmail(String email);
}