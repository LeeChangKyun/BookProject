package com.book.BookProject.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 추가적으로 사용자별 주문을 조회하는 메서드를 정의할 수 있음
}