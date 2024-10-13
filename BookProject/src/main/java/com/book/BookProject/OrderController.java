package com.book.BookProject;

import com.book.BookProject.order.Order;
import com.book.BookProject.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 주문 생성 페이지로 이동
    @GetMapping("/create")
    public String showCreateOrderPage() {
        return "/member/order/orderCreate";
    }

    // 주문 생성 처리
    @PostMapping("/create")
    public String createOrder(@ModelAttribute Order order, Model model) {
        Order newOrder = orderService.createOrder(order);
        model.addAttribute("order", newOrder);
        return "order/orderConfirmation";
    }

    // 특정 주문 조회
    @GetMapping("/{orderId}")
    public String getOrder(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            model.addAttribute("order", order);
            return "member/orderDetail";
        } else {
            return "error";
        }
    }

    // 모든 주문 조회
    @GetMapping
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "/order/orderList";
    }

    // 주문 삭제
    @PostMapping("/{orderId}/delete")
    public String deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return "redirect:/order";
    }
}