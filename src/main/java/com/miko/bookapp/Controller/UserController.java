package com.miko.bookapp.Controller;


import com.miko.bookapp.Utils;
import com.miko.bookapp.model.PasswordForm;
import com.miko.bookapp.model.Response;
import com.miko.bookapp.model.ServiceResponse;
import com.miko.bookapp.model.User;
import com.miko.bookapp.service.ServiceUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseUtil.okResponse("all users retrieved", "users", result);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Response> listUserById(@PathVariable long id){
        Optional<User> result = service.findUserById(id);
        return result.map(user -> ResponseUtil.okResponse("user with id: " + id + " retrieved", "user", user)).orElseGet(() -> ResponseUtil.idNotFoundResponse(User.class));
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createNewUser(@RequestBody User user){
        ServiceResponse<User> response = service.saveUser(user);

        if(response.getMessage().equals(Utils.EMAIL_OR_PASS_NOT_GIVEN)){
            return ResponseUtil.badRequestResponse(Utils.EMAIL_OR_PASS_NOT_GIVEN);
        }

        if(response.getMessage().equals(Utils.EMAIL_EXISTS)){
            return ResponseUtil.badRequestResponse(Utils.EMAIL_EXISTS);
        }

        if(response.getMessage().equals(Utils.ACCOUNT_CREATED)){
            return ResponseUtil.okResponse(Utils.ACCOUNT_CREATED, "user", response.getData());
        }
        return ResponseUtil.somethingWentWrongResponse();
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<Response> changePassword(@RequestBody PasswordForm passwordForm){
        ServiceResponse<User> response = service.changePassword(passwordForm.getEmail(), passwordForm.getOldPassword(), passwordForm.getNewPassword());

        if (response.getMessage().equals(Utils.BAD_PASSWORD)) {
            return ResponseUtil.badRequestResponse(Utils.BAD_PASSWORD);
        }
        if(response.getMessage().equals(Utils.EMAIL_NOT_FOUND)){
            return ResponseUtil.badRequestResponse(Utils.EMAIL_NOT_FOUND);
        }
        if(response.getMessage().equals(Utils.PASS_CHANGED)){
            return ResponseUtil.okResponse(Utils.PASS_CHANGED, "user", response.getData());
        }
        return ResponseUtil.somethingWentWrongResponse();
    }

}
