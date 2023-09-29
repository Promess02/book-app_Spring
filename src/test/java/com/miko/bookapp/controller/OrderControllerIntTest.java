package com.miko.bookapp.controller;

import com.miko.bookapp.Utils;
import com.miko.bookapp.model.Order;
import com.miko.bookapp.model.OrderItem;
import com.miko.bookapp.model.Product;
import com.miko.bookapp.model.User;
import com.miko.bookapp.repo.OrderItemRepo;
import com.miko.bookapp.repo.ProductRepo;
import com.miko.bookapp.repo.UserRepo;
import com.miko.bookapp.service.ServiceOrder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@AutoConfigureMockMvc
public class OrderControllerIntTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private ServiceOrder serviceOrder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private MockMvc mockMvc;

    private final String BASE_URL = "/orders";


    @BeforeEach
    void setUp() {
        User user = new User(1,"miko2002","new pass",false,null,140d);
        userRepo.save(user);
        User user2 = new User(2,"miko2022","new pass",false,null,120d);
        userRepo.save(user2);
        User user3 = new User(3,"miko2122","new pass",false,null,60d);
        userRepo.save(user3);

        Product product = new Product(1,"desc",10d);
        productRepo.save(product);

        OrderItem orderItem = new OrderItem(1L,null,product,3);
        List<OrderItem> list = new ArrayList<>();
        list.add(orderItem);

        OrderItem orderItem2 = new OrderItem(2L,null,product,2);
        List<OrderItem> list2 = new ArrayList<>();
        list2.add(orderItem2);

        Order order = new Order(1L,user,list);
        orderItem.setOrder(order);
        orderItemRepo.save(orderItem);
        serviceOrder.saveOrder(Utils.toOrderDTO(order));

        Order order2 = new Order(2L,user2,list2);
        orderItem2.setOrder(order2);
        orderItemRepo.save(orderItem2);
        serviceOrder.saveOrder(Utils.toOrderDTO(order2));
    }

    @AfterEach
    void tearDown(){
        userRepo.deleteAll();
        productRepo.deleteAll();
        orderItemRepo.deleteAll();
        serviceOrder.deleteAllOrders();
    }

    @Test
    @DisplayName("checks if returns all orders correctly")
    public void checkIfReturnsAllOrders(){
        try{
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/list"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("retrieved all orders")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("miko2002")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("ordered")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("checks if returns order by id correctly")
    public void checkIfReturnsOrderById(){
        try{
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/list/1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("retrieved order with id: 1")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("miko2002")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("ordered")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/list/3"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("id of class: Order not found")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("checks if creates the order successfully")
    public void checkIfCreatesOrder(){
        JSONObject jsonObject;
        try{
            jsonObject = new JSONObject(){{put("userEmail","miko2002");}};
            JSONObject orderItem = new JSONObject(){{
                put("product_id", 1);
                put("quantity", 3);
            }};
            JSONArray orderItemsArray = new JSONArray();
            orderItemsArray.put(orderItem);
            jsonObject.put("orderItems", orderItemsArray);
        }catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonObject.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("order has been saved")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("miko2002")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("checks if returns orders of given user correctly")
    public void getOrdersForUser(){
        try{
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/getForUser/1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("orders for user retrieved")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("miko2002")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("\"totalAmount\":30.0")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/getForUser/2"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("orders for user retrieved")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("miko2022")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("\"totalAmount\":20.0")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/getForUser/15"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("user id not found")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/getForUser/3"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("no orders found for user")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("checks if refunds order correctly")
    public void checkIfRefundsOrder(){
        try {
            mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL+"/refund/1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.ORDER_REFUNDED)))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("\"orderStatus\":\"refunded\"")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try{
            mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL+"/refund/14"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("checks if changes order status")
    public void checksIfChangesOrderStatus(){

        try{
            mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL+"/changeStatus/1")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content("shipped"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("status of order changed successfully")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("\"orderStatus\":\"shipped\"")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try{
            mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL+"/changeStatus/14"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
