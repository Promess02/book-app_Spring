package com.miko.bookapp.service.Implementation;

import com.miko.bookapp.DTO.OrderItemDTO;
import com.miko.bookapp.DTO.OrderReadDTO;
import com.miko.bookapp.DTO.ServiceResponse;
import com.miko.bookapp.Utils;
import com.miko.bookapp.model.Order;
import com.miko.bookapp.model.OrderItem;
import com.miko.bookapp.model.Product;
import com.miko.bookapp.repo.OrderItemRepo;
import com.miko.bookapp.repo.OrderRepo;
import com.miko.bookapp.repo.ProductRepo;
import com.miko.bookapp.repo.UserRepo;
import com.miko.bookapp.service.ServiceOrder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ServiceOrderImplementation implements ServiceOrder {

    private OrderRepo orderRepo;
    private OrderItemRepo orderItemRepo;
    private UserRepo userRepo;
    private ProductRepo productRepo;


    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public ServiceResponse<Order> saveOrder(OrderReadDTO orderRead) {

        if(orderRead.getUserEmail()==null) return new ServiceResponse<>(Optional.empty(),Utils.EMAIL_NOT_GIVEN);
        var email = orderRead.getUserEmail();
        var user = userRepo.findByEmail(email);
        if(user.isEmpty()) return new ServiceResponse<>(Optional.empty(),Utils.EMAIL_NOT_FOUND);

        List<OrderItemDTO> orderItemsDTO = orderRead.getOrderItems();
        if(orderItemsDTO.isEmpty()) return new ServiceResponse<>(Optional.empty(), Utils.NO_ORDER_ITEMS);

        List<OrderItem> orderItems = new LinkedList<>();

        for(OrderItemDTO orderItemDTO: orderItemsDTO){
            long productId = orderItemDTO.getProduct_id();
            if(productRepo.existsById(productId)) {
                Product product = productRepo.findById(productId).get();
                OrderItem orderItem = orderItemRepo.save(new OrderItem(product,orderItemDTO.getQuantity(),product.getPrice()*orderItemDTO.getQuantity()));
                orderItems.add(orderItem);
            }
        }

        double totalAmount = 0;
        for(OrderItem orderItem: orderItems)
            totalAmount=totalAmount+orderItem.getTotalPrice();

        if(totalAmount>user.get().getWalletWorth()) return new ServiceResponse<>(Optional.empty(),Utils.INSUFFICIENT_FUNDS);

        user.get().decreaseWalletWorth(totalAmount);
        userRepo.save(user.get());
        Order order = orderRepo.save(new Order(user.get(),orderItems));

        return new ServiceResponse<>(Optional.of(order), Utils.ORDER_SAVED);
    }

    @Override
    public Optional<Order> findOrderById(long id) {
        if(orderRepo.existsById(id)) return orderRepo.findById(id);
        return Optional.empty();
    }

    @Override
    public ServiceResponse<Order> updateOrder(long id, OrderReadDTO order) {
        return null;
    }

    @Override
    public ServiceResponse<Order> changeOrder(long id, OrderReadDTO order) {
        return null;
    }

    @Override
    public ServiceResponse<Order> deleteOrderById(long id) {
        return null;
    }

    @Override
    public ServiceResponse<Order> addOrderItem(OrderItem orderItem, long orderId) {
        return null;
    }

    @Override
    public ServiceResponse<Order> deleteOrderItem(OrderItem orderItem, long orderId) {
        return null;
    }

    @Override
    public ServiceResponse<Order> deleteOrder(long id) {
        return null;
    }

    @Override
    public ServiceResponse<List<Order>> getOrdersSinceDays(int days) {
        return null;
    }

    @Override
    public void deleteAllOrders() {
        orderRepo.deleteAll();
    }
}
