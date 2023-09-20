package com.miko.bookapp.service.Implementation;

import com.miko.bookapp.DTO.OrderItemDTO;
import com.miko.bookapp.DTO.OrderReadDTO;
import com.miko.bookapp.DTO.ServiceResponse;
import com.miko.bookapp.Utils;
import com.miko.bookapp.enums.OrderStatus;
import com.miko.bookapp.model.Order;
import com.miko.bookapp.model.OrderItem;
import com.miko.bookapp.model.Product;
import com.miko.bookapp.model.User;
import com.miko.bookapp.repo.OrderItemRepo;
import com.miko.bookapp.repo.OrderRepo;
import com.miko.bookapp.repo.ProductRepo;
import com.miko.bookapp.repo.UserRepo;
import com.miko.bookapp.service.ServiceOrder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ServiceResponse<OrderReadDTO> saveOrder(OrderReadDTO orderRead) {

        var response = checkIfValidOrderReadDTO(orderRead);
        if(!response.getMessage().equals("passed")) return response;


        List<OrderItem> orderItems = new LinkedList<>();

        long orderId = orderRepo.getNextGeneratedId();
        User user = userRepo.findByEmail(orderRead.getUserEmail()).get();
        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        for(OrderItemDTO orderItemDTO: orderRead.getOrderItems()){
            long productId = orderItemDTO.getProduct_id();
            if(productRepo.findById(productId).isPresent()) {
                orderItems = saveOrderItem(orderItemDTO,productId,orderItems);
            }else return new ServiceResponse<>(Optional.empty(),"no product found");
        }

        double totalAmount = 0;
        for(OrderItem orderItem: orderItems)
            totalAmount=totalAmount+orderItem.getTotalPrice();

        if(totalAmount>user.getWalletWorth()) return new ServiceResponse<>(Optional.empty(),Utils.INSUFFICIENT_FUNDS);

        user.decreaseWalletWorth(totalAmount);
        userRepo.save(user);
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        orderRepo.save(order);

        for(OrderItem orderItem: orderItems){
            orderItem.setOrder(order);
            orderItemRepo.save(orderItem);
        }
        orderRead.setTotalAmount(totalAmount);
        orderRead.getOrderItems().forEach(orderItemDTO -> orderItemDTO.setCustom_order_id(orderId));

        return new ServiceResponse<>(Optional.of(orderRead), Utils.ORDER_SAVED);
    }

    @Override
    public Optional<Order> findOrderById(long id) {
        if(orderRepo.existsById(id)) return orderRepo.findById(id);
        return Optional.empty();
    }

    private ServiceResponse<OrderReadDTO> checkIfValidOrderReadDTO(OrderReadDTO order){
        if(order.getUserEmail()==null) return new ServiceResponse<>(Optional.empty(),Utils.EMAIL_NOT_GIVEN);
        var email = order.getUserEmail();
        var user = userRepo.findByEmail(email);
        if(user.isEmpty()) return new ServiceResponse<>(Optional.empty(),Utils.EMAIL_NOT_FOUND);

        List<OrderItemDTO> orderItemsDTO = order.getOrderItems();
        if(orderItemsDTO.isEmpty()) return new ServiceResponse<>(Optional.empty(), Utils.NO_ORDER_ITEMS);
        return new ServiceResponse<>(Optional.empty(),"passed");
    }

    private List<OrderItem> saveOrderItem(OrderItemDTO orderItemDTO, long productId, List<OrderItem> orderItems){
        Product product = productRepo.findById(productId).get();
        OrderItem orderItem = new OrderItem(null, product,orderItemDTO.getQuantity());
        var nextOrderItemId = orderItemRepo.getNextGeneratedId();
        orderItem.setId(nextOrderItemId);
        orderItems.add(orderItem);
        orderItemRepo.save(orderItem);

        return orderItems;
    }

    @Override
    public ServiceResponse<List<Order>> getOrdersSinceDays(int days, long userId) {
        List<Order> listOfOrders = orderRepo.findAll();
        Optional<User> user = userRepo.existsById(userId)?userRepo.findById(userId):Optional.empty();
        if(user.isEmpty()) return new ServiceResponse<>(Optional.empty(), Utils.ID_NOT_FOUND);

        List<Order> recentOrders = listOfOrders.stream()
                .filter(order -> order.getUser().equals(user.get()))
                .filter(order -> order.getOrderDate().isAfter(LocalDateTime.now().minusDays(days)))
                .toList();

        if(recentOrders.isEmpty()) return new ServiceResponse<>(Optional.empty(), Utils.NO_ORDERS_FOUND);
        return new ServiceResponse<>(Optional.of(recentOrders), Utils.SUCCESS_ORDERS);
    }

    @Override
    public ServiceResponse<List<Order>> getOrdersForUser(long userId) {
        List<Order> listOfOrders = orderRepo.findAll();
        Optional<User> user = userRepo.existsById(userId)?userRepo.findById(userId):Optional.empty();
        if(user.isEmpty()) return new ServiceResponse<>(Optional.empty(), Utils.ID_NOT_FOUND);
        List<Order> orders = listOfOrders.stream()
                .filter(order -> order.getUser().equals(user.get())).toList();
        if(orders.isEmpty()) return new ServiceResponse<>(Optional.empty(),Utils.NO_ORDERS_FOUND);
        return new ServiceResponse<>(Optional.of(orders),Utils.SUCCESS_ORDERS);
    }

    @Override
    public ServiceResponse<Order> refundOrder(long id) {
        if(!orderRepo.existsById(id)) return new ServiceResponse<>(Optional.empty(),Utils.NO_ORDERS_FOUND);
        var order = orderRepo.findById(id).isPresent()?orderRepo.findById(id).get():null;
        if(order==null || order.getUser()==null ) return new ServiceResponse<>(Optional.empty(),Utils.ORDER_SAVE_FAILED);
        var userEmail = order.getUser().getEmail();
        var user = userRepo.findByEmail(userEmail).isPresent()?userRepo.findByEmail(userEmail).get():null;
        if(user==null) return new ServiceResponse<>(Optional.empty(),Utils.EMAIL_NOT_FOUND);
        user.increaseWalletWorth(order.getTotalAmount());
        orderRepo.findById(id).get().setOrderStatus(OrderStatus.REFUNDED);
        Order refundedOrder = orderRepo.existsById(id)?orderRepo.findById(id).get():null;
        if(refundedOrder==null) return new ServiceResponse<>(Optional.empty(), "refunded order not found");
        orderRepo.save(refundedOrder);
        return new ServiceResponse<>(Optional.of(refundedOrder),Utils.ORDER_REFUNDED);
    }

    @Override
    public ServiceResponse<Order> changeStatus(long id, String newStatus) {
        Optional<Order> order = orderRepo.existsById(id)?orderRepo.findById(id):Optional.empty();
        if(order.isEmpty()) return new ServiceResponse<>(Optional.empty(),Utils.ID_NOT_FOUND);
        OrderStatus orderStatus;
        try{
            orderStatus = OrderStatus.fromValue(newStatus);
        }catch (IllegalArgumentException e){
            return new ServiceResponse<>(Optional.empty(),Utils.BAD_ENUM_VALUE);
        }
        if(orderStatus==null) return new ServiceResponse<>(Optional.empty(),"failed changing order status");
        order.get().setOrderStatus(orderStatus);

        orderRepo.save(order.get());

        Order orderChanged = orderRepo.existsById(id)?orderRepo.findById(id).get():null;
        if(orderChanged==null) return new ServiceResponse<>(Optional.empty(), "no order found");
        return new ServiceResponse<>(Optional.of(orderChanged),Utils.ORDER_SAVED);
    }

    @Override
    public void deleteAllOrders() {
        orderRepo.deleteAll();
    }

}
