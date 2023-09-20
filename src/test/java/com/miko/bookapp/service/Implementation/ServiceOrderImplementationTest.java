package com.miko.bookapp.service.Implementation;

import com.miko.bookapp.Dummy;
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
import com.miko.bookapp.service.MemoryOrderItemRepo;
import com.miko.bookapp.service.MemoryOrderRepo;
import com.miko.bookapp.service.MemoryProductRepo;
import com.miko.bookapp.service.MemoryUserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ServiceOrderImplementationTest {
    private static OrderRepo orderRepo;
    private static ProductRepo productRepo;
    private static UserRepo userRepo;
    private static OrderItemRepo orderItemRepo;
    private static ServiceOrderImplementation service;

    @BeforeAll
    public static void setUp(){
        orderRepo = new MemoryOrderRepo();
        productRepo = new MemoryProductRepo();
        userRepo = new MemoryUserRepo();
        orderItemRepo = new MemoryOrderItemRepo();
        service = new ServiceOrderImplementation(orderRepo,orderItemRepo,userRepo,productRepo);
    }

    @AfterEach
    public void tearDown(){
        orderRepo.deleteAll();
        productRepo.deleteAll();
        userRepo.deleteAll();
        orderItemRepo.deleteAll();
    }

    @Test
    @DisplayName("check if finds order by id")
    public void checkIfFindsOrderById(){
        Order order = Dummy.dummyOrder(1,75d);
        orderRepo.save(order);
        assertThat(service.findOrderById(1).isPresent()).isEqualTo(true);
        assertThat(service.findOrderById(1).get()).isEqualTo(order);
        assertThat(service.findOrderById(20).isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("checks if returns orders since given days correctly")
    public void checkIfReturnsOrdersSinceDays(){
        User user = Dummy.dummyUser(1, "miko2002");
        userRepo.save(user);
        Product product = new Product(1,"product", 45d);
        productRepo.save(product);
        OrderItem orderItem = new OrderItem(1L,null, product, 3);
        List<OrderItem> orderItemList = new LinkedList<>(){{add(orderItem);}};


        Order order1 = new Order(1L,user,orderItemList, LocalDateTime.now().minusDays(2),135d);
        Order order2 = new Order(2L,user,orderItemList, LocalDateTime.now().minusDays(5),135d);
        Order order3 = new Order(3L,user,orderItemList, LocalDateTime.now().minusDays(10),135d);

        orderRepo.save(order1);
        orderRepo.save(order2);
        orderRepo.save(order3);

        assertThat(service.getOrdersSinceDays(1,1).getData()).isEqualTo(Optional.empty());
        assertThat(service.getOrdersSinceDays(1,1).getMessage()).isEqualTo(Utils.NO_ORDERS_FOUND);
        assertThat(service.getOrdersSinceDays(1,2).getData()).isEqualTo(Optional.empty());
        assertThat(service.getOrdersSinceDays(1,2).getMessage()).isEqualTo(Utils.ID_NOT_FOUND);
        assertThat(service.getOrdersSinceDays(3,1).getData()).isEqualTo(Optional.of(List.of(order1)));
        assertThat(service.getOrdersSinceDays(3,1).getMessage()).isEqualTo(Utils.SUCCESS_ORDERS);
        assertThat(service.getOrdersSinceDays(6,1).getData()).isEqualTo(Optional.of(List.of(order1,order2)));
        assertThat(service.getOrdersSinceDays(12,1).getData()).isEqualTo(Optional.of(List.of(order1,order2, order3)));
    }

    @Test
    @DisplayName("checks if finds orders of given user correctly")
    public void checkIfFindsOrdersOfUser(){
        User user1 = Dummy.dummyUser(1, "miko2002");
        User user2 = Dummy.dummyUser(2,"miki123");
        User user3 = Dummy.dummyUser(3,"new email");
        userRepo.save(user1);userRepo.save(user2);userRepo.save(user3);
        Product product = new Product(1,"product", 45d);
        productRepo.save(product);
        OrderItem orderItem = new OrderItem(1L,null, product, 3);
        List<OrderItem> orderItemList = new LinkedList<>(){{add(orderItem);}};

        Order order1 = new Order(1L,user1,orderItemList, LocalDateTime.now().minusDays(2),135d);
        Order order2 = new Order(2L,user1,orderItemList, LocalDateTime.now().minusDays(5),135d);
        Order order3 = new Order(3L,user2,orderItemList, LocalDateTime.now().minusDays(10),135d);
        Order order4 = new Order(4L,user2,orderItemList, LocalDateTime.now().minusDays(12),135d);

        orderRepo.save(order1);
        orderRepo.save(order2);
        orderRepo.save(order3);
        orderRepo.save(order4);

        assertThat(service.getOrdersForUser(6).getData()).isEqualTo(Optional.empty());
        assertThat(service.getOrdersForUser(6).getMessage()).isEqualTo(Utils.ID_NOT_FOUND);
        assertThat(service.getOrdersForUser(3).getData()).isEqualTo(Optional.empty());
        assertThat(service.getOrdersForUser(3).getMessage()).isEqualTo(Utils.NO_ORDERS_FOUND);
        assertThat(service.getOrdersForUser(1).getMessage()).isEqualTo(Utils.SUCCESS_ORDERS);
        assertThat(service.getOrdersForUser(1).getData()).isEqualTo(Optional.of(List.of(order1,order2)));
        assertThat(service.getOrdersForUser(2).getData()).isEqualTo(Optional.of(List.of(order3,order4)));
    }

    @Test
    @DisplayName("checks if refunds the order correctly")
    public void checkIfRefundsOrderCorrectly(){
        User user1 = new User(1,"miko2002", "secret", true, null,  635d);
        User user2 = new User(2,"miki123", "secret", true, null,  635d);
        userRepo.save(user1);userRepo.save(user2);
        Product product = new Product(1,"product", 45d);
        productRepo.save(product);
        OrderItem orderItem = new OrderItem(1L,null, product, 3);
        List<OrderItem> orderItemList = new LinkedList<>(){{add(orderItem);}};

        Order order1 = new Order(1L,user1,orderItemList, LocalDateTime.now().minusDays(2),135d);
        Order order2 = new Order(2L,Dummy.dummyUser(2,"unknown user"),orderItemList, LocalDateTime.now().minusDays(2),135d);
        Order order3 = new Order(3L,null,orderItemList, LocalDateTime.now().minusDays(2),135d);
        orderRepo.save(order1);
        orderRepo.save(order2);
        orderRepo.save(order3);

        assertThat(service.refundOrder(34).getMessage()).isEqualTo(Utils.NO_ORDERS_FOUND);
        assertThat(service.refundOrder(34).getData()).isEqualTo(Optional.empty());
        assertThat(service.refundOrder(3).getMessage()).isEqualTo(Utils.ORDER_SAVE_FAILED);
        assertThat(service.refundOrder(2).getMessage()).isEqualTo(Utils.EMAIL_NOT_FOUND);
        order1.setOrderStatus(OrderStatus.ORDERED);
        assertThat(service.refundOrder(1).getMessage()).isEqualTo(Utils.ORDER_REFUNDED);
        assertThat(userRepo.findById(1).get().getWalletWorth()).isEqualTo(635+135);
        assertThat(service.refundOrder(1).getData()).isEqualTo(Optional.of(order1));
    }
    @Test
    @DisplayName("checks if changes the order status correctly")
    public void checkIfChangesOrderStatus(){
        User user = Dummy.dummyUser(1,"miko2002");
        userRepo.save(user);
        Product product = new Product(1,"product", 45d);
        productRepo.save(product);
        OrderItem orderItem = new OrderItem(1L,null, product,3);
        List<OrderItem> orderItemList = new LinkedList<>(){{add(orderItem);}};
        Order order = new Order(1L,user,orderItemList, LocalDateTime.now().minusDays(2),135d);
        orderRepo.save(order);
        order.setOrderStatus(OrderStatus.SHIPPED);

        assertThat(service.changeStatus(23,"new").getData()).isEqualTo(Optional.empty());
        assertThat(service.changeStatus(23,"new").getMessage()).isEqualTo(Utils.ID_NOT_FOUND);
        assertThat(service.changeStatus(1,"invalid status").getMessage()).isEqualTo(Utils.BAD_ENUM_VALUE);
        assertThat(service.changeStatus(1,"shipped").getData()).isEqualTo(Optional.of(order));
        assertThat(service.changeStatus(1,"shipped").getMessage()).isEqualTo(Utils.ORDER_SAVED);
    }
}
