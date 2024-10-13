package com.book.BookProject.order;

import com.book.BookProject.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ORDERS") // ORDER는 예약어일 수 있으므로 백틱(`)을 사용하여 테이블 이름을 설정
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId; // 주문 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false) // 회원 테이블의 IDX와 매핑
    private UserEntity member; // 회원과의 연관 관계 설정

    @Column(nullable = false)
    private LocalDateTime orderDate; // 주문 날짜

    @Column(nullable = false, length = 50)
    private String status; // 주문 상태 (예: ORDERED, CANCELED, SHIPPED 등)

    @Column(nullable = false)
    private Double totalAmount; // 주문 총액

    @Column(nullable = false, length = 200)
    private String shippingAddress; // 배송 주소

    @Column(nullable = false, length = 100)
    private String recipientName; // 수령인 이름

    @Column(nullable = false, length = 20)
    private String recipientPhone; // 수령인 연락처

    @Column(nullable = false, length = 50)
    private String paymentMethod; // 결제 방법 (예: CARD, BANK_TRANSFER 등)

    @Column(nullable = false, length = 50)
    private String paymentStatus; // 결제 상태 (예: PAID, PENDING, REFUNDED 등)

    // 생성 날짜와 수정 날짜를 자동으로 관리
    @Builder.Default
    @Column(updatable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updateDate = LocalDateTime.now();
}