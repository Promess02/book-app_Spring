package com.miko.bookapp.Controller;


import com.miko.bookapp.Utils;
import com.miko.bookapp.model.*;
import com.miko.bookapp.service.ServiceUser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private ServiceUser service;

    @GetMapping("/list")
    public ResponseEntity<Response> listAllUsers(){
        List<User> result = service.getAllUsers();
            return Utils.okResponse("all users retrieved", "users", result);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Response> listUserById(@PathVariable long id){
        Optional<User> result = service.findUserById(id);
        return result.map(user -> Utils.okResponse("user with id: " + id + " retrieved", "user", user)).orElseGet(() -> Utils.idNotFoundResponse(User.class));
//        if(result.isPresent()){
//            return Utils.okResponse("user with id: " + id + " retrieved", "user", result.get());
//        }
//        return Utils.idNotFoundResponse(User.class);
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createNewUser(@RequestBody User user){
        ServiceResponse<User> response = service.saveUser(user);

        if(response.getMessage().equals(Utils.EMAIL_OR_PASS_NOT_GIVEN)){
            return Utils.badRequestResponse(Utils.EMAIL_OR_PASS_NOT_GIVEN);
        }

        if(response.getMessage().equals(Utils.EMAIL_EXISTS)){
            return Utils.badRequestResponse(Utils.EMAIL_EXISTS);
        }

        if(response.getMessage().equals(Utils.ACCOUNT_CREATED)){
            return Utils.okResponse(Utils.ACCOUNT_CREATED, "user", response.getData());
        }
        return Utils.somethingWentWrongResponse();
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<Response> changePassword(@RequestBody PasswordForm passwordForm){
        ServiceResponse<User> response = service.changePassword(passwordForm.getEmail(), passwordForm.getOldPassword(), passwordForm.getNewPassword());

        if (response.getMessage().equals(Utils.BAD_PASSWORD)) {
            return Utils.badRequestResponse(Utils.BAD_PASSWORD);
        }
        if(response.getMessage().equals(Utils.EMAIL_NOT_FOUND)){
            return Utils.badRequestResponse(Utils.EMAIL_NOT_FOUND);
        }
        if(response.getMessage().equals(Utils.PASS_CHANGED)){
            return Utils.okResponse(Utils.PASS_CHANGED, "user", response.getData());
        }
        return Utils.somethingWentWrongResponse();
    }


}
