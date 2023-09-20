package com.miko.bookapp;

import com.miko.bookapp.DTO.OrderItemDTO;
import com.miko.bookapp.DTO.OrderReadDTO;
import com.miko.bookapp.model.Order;
import com.miko.bookapp.model.OrderItem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static String EMAIL_OR_PASS_NOT_GIVEN = "email or password not provided";
    public static String EMAIL_EXISTS = "an account with given email already exists";
    public static String ACCOUNT_CREATED = "account successfully created";

    public static String BAD_PASSWORD = "incorrect password given";
    public static String PASS_CHANGED = "password changed successfully";
    public static String EMAIL_NOT_FOUND = "no account with provided email found";
    public static String EMAIL_NOT_GIVEN = "no email has been provided in the request";
    public static String NULL_OBJECT = "a null object provided";
    public static String NO_BOOKS_FOUND = "no matching books found";

    public static String BOOK_RETRIEVED = "books retrieved successfully";

    public static String ID_NOT_FOUND = "id not found";
    public static String ACCOUNT_UPDATED = "account has been updated";
    public static String NO_ORDER_ITEMS = "no order items provided in the request";
    public static String ORDER_SAVED = "order has been saved successfully";
    public static String INSUFFICIENT_FUNDS = "account funds are insufficient to execute transaction";

    public static String SUCCESS_ORDERS = "successfully retrieved orders";
    public static String NO_ORDERS_FOUND = "no orders were found";
    public static String ORDER_REFUNDED = "order refunded successfully";
    public static String BAD_ENUM_VALUE = "bad enum value provided";
    public static String ORDER_SAVE_FAILED = "order saving failed";

    public static Field[] extractFields(Object object) {
        List<Field> fieldsList = new ArrayList<>();
        Class<?> currentClass = object.getClass();

        while (currentClass != null) {
            Field[] declaredFields = currentClass.getDeclaredFields();
            fieldsList.addAll(Arrays.asList(declaredFields));
            currentClass = currentClass.getSuperclass();
        }

        Field[] fieldsArray = new Field[fieldsList.size()];
        return fieldsList.toArray(fieldsArray);
    }

    public OrderItemDTO toOrderItemDTO(OrderItem orderItem){
        OrderItemDTO result = new OrderItemDTO();
        if(orderItem.getOrder()==null) result.setCustom_order_id(0);
        else result.setCustom_order_id(orderItem.getId());
        result.setProduct_id(orderItem.getProduct().getId());
        result.setQuantity(orderItem.getQuantity());
        return result;
    }

    public OrderReadDTO toOrderDTO(Order order){
        OrderReadDTO result = new OrderReadDTO();
        result.setUserEmail(order.getUser().getEmail());
        result.setTotalAmount(order.getTotalAmount());
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        for(OrderItem orderItem: order.getOrderItems())
            orderItemDTOList.add(toOrderItemDTO(orderItem));
        result.setOrderItems(orderItemDTOList);
        return result;
    }

}
