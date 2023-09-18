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
    ServiceResponse<List<Order>> getOrdersSinceDays(int days, long userId);
    ServiceResponse<List<Order>> getOrdersForUser(long userId);
    ServiceResponse<Order> refundOrder(long id);
    ServiceResponse<Order> changeStatus(long id, String newStatus);
    void deleteAllOrders();
}
