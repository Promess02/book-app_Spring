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
        if(message.equals(Utils.EMAIL_NOT_GIVEN)) return ResponseUtil.badRequestResponse(Utils.EMAIL_NOT_GIVEN);
        if(message.equals(Utils.EMAIL_NOT_FOUND)) return ResponseUtil.badRequestResponse(Utils.EMAIL_NOT_FOUND);
        if(message.equals(Utils.NO_ORDER_ITEMS)) return ResponseUtil.badRequestResponse(Utils.NO_ORDER_ITEMS);
        if(message.equals(Utils.INSUFFICIENT_FUNDS)) return ResponseUtil.badRequestResponse(Utils.INSUFFICIENT_FUNDS);
        if(message.equals(Utils.ORDER_SAVED))
            return ResponseUtil.okResponse("order has been saved", "order", response.getData());
        return ResponseUtil.somethingWentWrongResponse(message);
    }
}
