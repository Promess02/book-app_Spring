package com.miko.bookapp.service;

import com.miko.bookapp.DTO.OrderReadDTO;
import com.miko.bookapp.DTO.ServiceResponse;
import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.Order;
import com.miko.bookapp.model.OrderItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ServiceOrder {
    List<Order> getAllOrders();
    ServiceResponse<OrderReadDTO> saveOrder(OrderReadDTO order);
    Optional<Order> findOrderById(long id);
    ServiceResponse<Order> updateOrder(long id, OrderReadDTO order);
    ServiceResponse<Order> changeOrder(long id, OrderReadDTO order);
    ServiceResponse<Order> deleteOrderById(long id);
    ServiceResponse<Order> addOrderItem(OrderItem orderItem, long orderId);
    ServiceResponse<Order> deleteOrderItem(OrderItem orderItem, long orderId);
    ServiceResponse<Order> deleteOrder(long id);
    ServiceResponse<List<Order>> getOrdersSinceDays(int days);
    void deleteAllOrders();
}
