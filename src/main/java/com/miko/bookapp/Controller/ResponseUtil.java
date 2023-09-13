package com.miko.bookapp.Controller;

import com.miko.bookapp.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

public class ResponseUtil {
    public static ResponseEntity<Response> idNotFoundResponse(Class objectClass){
        return ResponseEntity.badRequest().body(
                Response.builder()
                        .message("id of class: " + objectClass.getSimpleName() + " not found")
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
