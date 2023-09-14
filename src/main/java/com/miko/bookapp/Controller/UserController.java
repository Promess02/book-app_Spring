package com.miko.bookapp.Controller;


import com.miko.bookapp.Utils;
import com.miko.bookapp.DTO.PasswordForm;
import com.miko.bookapp.DTO.Response;
import com.miko.bookapp.DTO.ServiceResponse;
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

    @GetMapping("/listByEmail")
    public ResponseEntity<Response> listUserByEmail(@RequestBody String email){
        Optional<User> result = service.findUserByEmail(email);
        return result.map(user -> ResponseUtil.okResponse("user with email: " + email + " retrieved", "user", user)).orElseGet(() -> ResponseUtil.emailNotFoundResponse(email));
    }

    @PostMapping("/updateUser/{id}")
    public ResponseEntity<Response> updateUserById(@PathVariable int id, @RequestBody User updatedUser){
        Optional<User> result = service.updateUserById(id, updatedUser);
        return result.map(user -> ResponseUtil.okResponse("user with id: " + id + " updated", "user", user)).orElseGet(() -> ResponseUtil.idNotFoundResponse(User.class));
    }

    @PostMapping("/updateUserByEmail")
    public ResponseEntity<Response> updateUserByEmail(@RequestBody User updatedUser){
        ServiceResponse<User> result = service.updateUserByEmail(updatedUser);

        if(result.getMessage().equals(Utils.EMAIL_NOT_GIVEN)) return ResponseUtil.badRequestResponse(Utils.EMAIL_NOT_GIVEN);
        if (result.getMessage().equals(Utils.EMAIL_NOT_FOUND)) return ResponseUtil.badRequestResponse(Utils.EMAIL_NOT_FOUND);

        if (result.getMessage().equals(Utils.ACCOUNT_UPDATED))
            return ResponseUtil.okResponse(Utils.ACCOUNT_UPDATED, "user", result.getData());
        return ResponseUtil.somethingWentWrongResponse(result.getMessage());
    }

    @PatchMapping("/changeUser")
    public ResponseEntity<Response> changeUserByEmail(@RequestBody User updatedUser){
        ServiceResponse<User> result = service.changeUserByEmail(updatedUser);

        if(result.getMessage().equals(Utils.EMAIL_NOT_GIVEN)) return ResponseUtil.badRequestResponse(Utils.EMAIL_NOT_GIVEN);
        if (result.getMessage().equals(Utils.EMAIL_NOT_FOUND)) return ResponseUtil.badRequestResponse(Utils.EMAIL_NOT_FOUND);

        if (result.getMessage().equals(Utils.ACCOUNT_UPDATED))
            return ResponseUtil.okResponse(Utils.ACCOUNT_UPDATED, "user", result.getData());
        return ResponseUtil.somethingWentWrongResponse(result.getMessage());
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
        return ResponseUtil.somethingWentWrongResponse(response.getMessage());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteUserById(@PathVariable long id){
        ServiceResponse<User> response = service.deleteUserById(id);

        if(response.getMessage().equals(Utils.ID_NOT_FOUND)){
            return ResponseUtil.idNotFoundResponse(User.class);
        }
        if(response.getMessage().equals(Utils.ACCOUNT_UPDATED)){
            return ResponseUtil.okResponse("user with id: " + id + " deleted", "user", response.getData());
        }
        return ResponseUtil.somethingWentWrongResponse(response.getMessage());
    }

    @DeleteMapping("/deleteByEmail")
    public ResponseEntity<Response> deleteUserByEmail(@RequestBody String email){
        ServiceResponse<User> response = service.deleteUserByEmail(email);
        var serviceResponse = response.getMessage();

        if(serviceResponse.equals(Utils.EMAIL_NOT_GIVEN)) return ResponseUtil.badRequestResponse(Utils.EMAIL_NOT_GIVEN);
        if(serviceResponse.equals(Utils.EMAIL_NOT_FOUND)) return ResponseUtil.badRequestResponse(Utils.EMAIL_NOT_FOUND);
        if(serviceResponse.equals(Utils.ACCOUNT_UPDATED)) return ResponseUtil.okResponse("successfully deleted user with email: " + email,
                "user", response.getData());
        return ResponseUtil.somethingWentWrongResponse(response.getMessage());
    }

    @PatchMapping("/addFunds/{id}")
    public ResponseEntity<Response> addFundsToAccount(@PathVariable long id, @RequestBody Double funds){
        ServiceResponse<User> response = service.addFunds(id,funds);
        var responseMessage = response.getMessage();
        if(responseMessage.equals(Utils.ID_NOT_FOUND)) return ResponseUtil.badRequestResponse(Utils.ID_NOT_FOUND);
        else return ResponseUtil.okResponse("added funds: " + funds + " to account with id: " + id,
                "user", response.getData());
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
        return ResponseUtil.somethingWentWrongResponse(response.getMessage());
    }



}
