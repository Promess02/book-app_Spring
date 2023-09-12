package com.miko.bookapp;

import com.miko.bookapp.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Utils {

    public static String EMAIL_OR_PASS_NOT_GIVEN = "email or password not provided";
    public static String EMAIL_EXISTS = "an account with given email already exists";
    public static String ACCOUNT_CREATED = "account successfully created";

    public static String BAD_PASSWORD = "incorrect password given";
    public static String PASS_CHANGED = "password changed successfully";
    public static String EMAIL_NOT_FOUND = "no account with provided email found";

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

    public static ResponseEntity<Response> idNotFoundResponse(Class objectClass){
        return ResponseEntity.badRequest().body(
                Response.builder()
                        .message("id of class: " + objectClass.getName() + "not found")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    public static ResponseEntity<Response> somethingWentWrongResponse(){
        return ResponseEntity.ok(
                Response.builder()
                        .message("something went wrong")
                        .timestamp(LocalDateTime.now())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    public static ResponseEntity<Response> badRequestResponse(String message){
        return ResponseEntity.ok(
                Response.builder()
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build()
        );

    }
    public static ResponseEntity<Response> okResponse(String message, String dataName, Object data){
        return ResponseEntity.ok(
                Response.builder()
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of(dataName, data))
                        .build()
        );
    }

}
