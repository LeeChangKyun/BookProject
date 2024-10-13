package com.book.BookProject.order;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // 주문 생성 메서드
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    // 특정 주문 조회 메서드
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    // 모든 주문 조회 메서드
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 주문 삭제 메서드
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}