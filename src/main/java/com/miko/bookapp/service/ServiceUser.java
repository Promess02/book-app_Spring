package com.miko.bookapp.service;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.Order;
import com.miko.bookapp.model.ServiceResponse;
import com.miko.bookapp.model.User;

import java.util.List;
import java.util.Optional;

public interface ServiceUser {
    List<User> getAllUsers();

    ServiceResponse<User> saveUser(User user);

    Optional<User> findUserById(long id);

    Optional<User> findUserByEmail(String email);

    boolean checkIfExistsByEmail(String email);

    Optional<User> updateUserById(long id, User user);

    Optional<User> updateUserByEmail(String email, User user);

    Optional<User> changeUser(long id, User user);

    ServiceResponse<User> changePassword(String email, String oldPassword, String newPassword);

    ServiceResponse<User> deleteUserById(long id);

    Optional<List<Order>> getListOfOrdersOfUser(User user);
    Optional<List<Order>> getListOfOrdersOfUser(String email);
    Optional<List<Order>> getListOfOrdersOfUser(long id);
    void deleteAllUsers();

}
