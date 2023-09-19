package com.miko.bookapp.service.Implementation;


import com.miko.bookapp.Utils;
import com.miko.bookapp.model.Order;
import com.miko.bookapp.DTO.ServiceResponse;
import com.miko.bookapp.model.User;
import com.miko.bookapp.repo.OrderRepo;
import com.miko.bookapp.repo.UserRepo;
import com.miko.bookapp.service.ServiceUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ServiceUserImplementation implements ServiceUser {

    private final UserRepo userRepo;
    private final OrderRepo orderRepo;

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public ServiceResponse<User> saveUser(User user) {
        if(user.getPassword()==null || user.getEmail()==null) return new ServiceResponse<>(Optional.empty(), Utils.EMAIL_OR_PASS_NOT_GIVEN);
        if(userRepo.existsByEmail(user.getEmail())) return new ServiceResponse<>(Optional.empty(),Utils.EMAIL_EXISTS);
        userRepo.save(user);

        return new ServiceResponse<>(Optional.of(user), Utils.ACCOUNT_CREATED);
    }

    @Override
    public Optional<User> findUserById(long id) {
        if(userRepo.existsById(id)){
            log.info("found user with id: " + id);
            return userRepo.findById(id);
        }
        log.info("user with id: " + id + "not found");
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        if(userRepo.existsByEmail(email)) {
            log.info("found user with email: " + email);
            return userRepo.findByEmail(email);
        }
        log.info("user with email: " + email + "not found");
        return Optional.empty();
    }

    @Override
    public boolean checkIfExistsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public Optional<User> updateUserById(long id, User user) {
        if(userRepo.existsById(id)){
            userRepo.save(user);
            log.info("updated user with id: " + id);
            return userRepo.findById(id);
        }
        log.info("user with id: " + id + " not found");
        return Optional.empty();
    }

    @Override
    public ServiceResponse<User> updateUserByEmail(User user) {
        if(user.getEmail() == null) return new ServiceResponse<>(Optional.empty(),Utils.EMAIL_NOT_GIVEN);
        var email = user.getEmail();
        if(userRepo.existsByEmail(email)){
            userRepo.save(user);
            log.info("updated user with email: " + email);
            return new ServiceResponse<>(userRepo.findByEmail(email),Utils.ACCOUNT_UPDATED);
        }
        log.info("user with email: " + email + " not found");
        return new ServiceResponse<>(Optional.empty(), Utils.EMAIL_NOT_FOUND);
    }

    @Override
    public Optional<User> changeUser(long id, User user) {
        if(userRepo.existsById(id)){
            var userFields = Utils.extractFields(user);

            Optional<User> userOptional = userRepo.findById(id);
            if(userOptional.isEmpty()) return Optional.empty();
            User result = userOptional.get();

            for(Field field: userFields){
                field.setAccessible(true);
                try {
                    var newValue = field.get(user);
                    if(newValue!=null && !field.getName().equals("id"))
                        field.set(result,newValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            userRepo.save(result);
            log.info("user changed with id: " + id);
            return Optional.of(result);
        }
        log.info("id provided for user was not found");
        return Optional.empty();
    }

    @Override
    public ServiceResponse<User> changeUserByEmail(User user){
        if(user.getEmail()==null) return new ServiceResponse<>(Optional.empty(), Utils.EMAIL_NOT_GIVEN);
        var email = user.getEmail();

        if(userRepo.existsByEmail(email)){
            var userFields = Utils.extractFields(user);

            Optional<User> userOptional = userRepo.findByEmail(email);
            if(userOptional.isEmpty()) return new ServiceResponse<>(Optional.empty(), Utils.EMAIL_NOT_FOUND);
            User result = userOptional.get();

            for(Field field: userFields){
                field.setAccessible(true);
                try {
                    var newValue = field.get(user);
                    if(newValue!=null && !field.getName().equals("id"))
                        field.set(result,newValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            userRepo.save(result);
            log.info("user changed with email: " + email);
            return new ServiceResponse<>(Optional.of(result), Utils.ACCOUNT_UPDATED);
        }
        log.info("email provided for user was not found");
        return new ServiceResponse<>(Optional.empty(), Utils.EMAIL_NOT_FOUND);
    }

    @Override
    public ServiceResponse<User> changePassword(String email, String oldPassword, String newPassword) {
        if(email==null) return new ServiceResponse<>(Optional.empty(),Utils.EMAIL_NOT_GIVEN);
        if(userRepo.existsByEmail(email)){
            Optional<User> user = userRepo.findByEmail(email);
            if(user.isPresent() && user.get().getPassword().equals(oldPassword)) user.get().setPassword(newPassword);
            else{
                log.info("given password is incorrect");
                return new ServiceResponse<>(Optional.empty(), Utils.BAD_PASSWORD);
            }
            user.ifPresent(userRepo::save);
            log.info("password changed successfully");
            return new ServiceResponse<>(user, Utils.PASS_CHANGED);
        }
        log.info("incorrect email when changing password");
        return new ServiceResponse<>(Optional.empty(), Utils.EMAIL_NOT_FOUND);
    }

    @Override
    public ServiceResponse<User> addFunds(long id, Double funds) {
        if(userRepo.existsById(id)){
            Optional<User> user = userRepo.findById(id);
            user.ifPresent(value -> value.increaseWalletWorth(funds));
            user.ifPresent(userRepo::save);
            return new ServiceResponse<>(Optional.of(user.get()),Utils.ACCOUNT_UPDATED);
        }
        return new ServiceResponse<>(Optional.empty(),Utils.ID_NOT_FOUND);
    }

    @Override
    public ServiceResponse<User> deleteUserById(long id) {
        var user = userRepo.findById(id);
        if(user.isPresent()){
            userRepo.deleteById(id);
            log.info("successfully deleted user with id: " + id);
            return new ServiceResponse<>(Optional.empty(),Utils.ACCOUNT_UPDATED);
        }
        return new ServiceResponse<>(Optional.empty(),Utils.ID_NOT_FOUND);
    }

    @Override
    public ServiceResponse<User> deleteUserByEmail(String email) {
        if(email==null) return new ServiceResponse<>(Optional.empty(),Utils.EMAIL_NOT_GIVEN);
        if(userRepo.existsByEmail(email)){
            Optional<User> user = userRepo.findByEmail(email);
            if(user.isEmpty()) return new ServiceResponse<>(Optional.empty(), Utils.EMAIL_NOT_FOUND);
            userRepo.delete(user.get());
            return new ServiceResponse<>(Optional.empty(),Utils.ACCOUNT_UPDATED);
        }
        return new ServiceResponse<>(Optional.empty(),Utils.EMAIL_NOT_FOUND);
    }


    @Override
    public Optional<List<Order>> getListOfOrdersOfUser(User user) {
        var listOfOrders = orderRepo.findAll();
        List<Order> result = new LinkedList<>();
        for(Order order: listOfOrders){
            if(order.getUser().equals(user)) result.add(order);
        }
        if(result.isEmpty()) return Optional.empty();
        return Optional.of(result);
    }

    @Override
    public Optional<List<Order>> getListOfOrdersOfUser(String email) {
        var listOfOrders = orderRepo.findAll();
        List<Order> result = new LinkedList<>();
        for(Order order: listOfOrders){
            if(order.getUser().getEmail().equals(email)) result.add(order);
        }
        if(result.isEmpty()) return Optional.empty();
        return Optional.of(result);
    }

    @Override
    public Optional<List<Order>> getListOfOrdersOfUser(long id) {
        var listOfOrders = orderRepo.findAll();
        List<Order> result = new LinkedList<>();
        for(Order order: listOfOrders){
            if(order.getUser().getId()==id) result.add(order);
        }
        if(result.isEmpty()) return Optional.empty();
        return Optional.of(result);    }

    @Override
    public void deleteAllUsers() {
        userRepo.deleteAll();
    }
}
