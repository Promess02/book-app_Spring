package com.miko.bookapp.Controller;

import com.miko.bookapp.DTO.OrderReadDTO;
import com.miko.bookapp.DTO.Response;
import com.miko.bookapp.Utils;
import com.miko.bookapp.model.Order;
import com.miko.bookapp.service.ServiceOrder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final ServiceOrder service;

    public OrderController(ServiceOrder service) {
        this.service = service;
    }

    @GetMapping("/list")
    public ResponseEntity<Response> getAllOrders(){
        return ResponseUtil.okResponse("retrieved all orders", "orders", service.getAllOrders());
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Response> getAllOrdersById(@PathVariable long id){
        var result = service.findOrderById(id);
        return result.map(order -> ResponseUtil.okResponse("retrieved order with id: " + id, "order", order))
                .orElseGet(() -> ResponseUtil.idNotFoundResponse(Order.class));
    }

    @PostMapping("/create")
    public ResponseEntity<Response> saveOrder(@Valid @RequestBody OrderReadDTO orderReadDTO){
        var response = service.saveOrder(orderReadDTO);
        var message = response.getMessage();
        if(message.equals(Utils.ORDER_SAVED))
            return ResponseUtil.okResponse("order has been saved", "order", response.getData());
        return ResponseUtil.badRequestResponse(message);
    }

    @GetMapping("/getForUser/{userId}")
    public ResponseEntity<Response> getOrdersForUser(@PathVariable long userId){
        var response = service.getOrdersForUser(userId);
        String message = response.getMessage();

        if(message.equals(Utils.ID_NOT_FOUND)) return ResponseUtil.badRequestResponse("user id not found");
        if(message.equals(Utils.NO_ORDERS_FOUND)) return ResponseUtil.badRequestResponse("no orders found for user");
        if(message.equals(Utils.SUCCESS_ORDERS)) return ResponseUtil.okResponse("orders for user retrieved", "user", response.getData());
        return ResponseUtil.badRequestResponse(message);
    }

    @PatchMapping("/refund/{orderId}")
    public ResponseEntity<Response> refundOrder(@PathVariable long orderId){
        var response = service.refundOrder(orderId);
        var message = response.getMessage();
        if(message.equals(Utils.ORDER_REFUNDED)) return ResponseUtil.okResponse(Utils.ORDER_REFUNDED,"order", response.getData());
        return ResponseUtil.badRequestResponse(message);
    }

    @PatchMapping("/changeStatus/{orderId}")
    public ResponseEntity<Response> changeStatus(@PathVariable long orderId, @RequestBody String status){
        var response = service.changeStatus(orderId,status);
        var message = response.getMessage();
        if(message.equals(Utils.ORDER_SAVED)) return ResponseUtil.okResponse("status of order changed successfully", "order",response.getData());
        return ResponseUtil.badRequestResponse(message);
    }
}
