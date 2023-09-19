package com.miko.bookapp.service.Implementation;

import com.miko.bookapp.DTO.OrderItemDTO;
import com.miko.bookapp.DTO.OrderReadDTO;
import com.miko.bookapp.DTO.ServiceResponse;
import com.miko.bookapp.Dummy;
import com.miko.bookapp.Utils;
import com.miko.bookapp.model.Product;
import com.miko.bookapp.model.User;
import com.miko.bookapp.repo.OrderItemRepo;
import com.miko.bookapp.repo.OrderRepo;
import com.miko.bookapp.repo.ProductRepo;
import com.miko.bookapp.repo.UserRepo;
import com.miko.bookapp.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ServiceUserImplementationTest {

    private static UserRepo userRepo;
    private static OrderRepo orderRepo;
    private static ServiceUser serviceUser;
    private static ServiceOrder orderService;
    private static OrderItemRepo orderItemRepo;
    private static ProductRepo productRepo;

    @BeforeAll
    public static void setUp(){
        userRepo = new MemoryUserRepo();
        orderRepo = new MemoryOrderRepo();
        serviceUser = new ServiceUserImplementation(userRepo,orderRepo);
        productRepo = new MemoryProductRepo();
        orderItemRepo = new MemoryOrderItemRepo();
        orderService = new ServiceOrderImplementation(orderRepo,orderItemRepo,userRepo,productRepo);
    }

    @AfterEach
    public void tearDown(){
        userRepo.deleteAll();
        orderRepo.deleteAll();
        productRepo.deleteAll();
        orderItemRepo.deleteAll();
    }

    @Test
    @DisplayName("checks if service returns all users successfully")
    public void checkIfReturnsAllUsers(){
        userRepo.save(Dummy.dummyUser(1,"miko1"));
        userRepo.save(Dummy.dummyUser(2,"miko2"));
        userRepo.save(Dummy.dummyUser(3,"miko3"));

        assertThat(serviceUser.getAllUsers().size()).isEqualTo(3);
        assertThat(serviceUser.getAllUsers().get(0).toString().contains("miko1")).isEqualTo(true);
        assertThat(serviceUser.getAllUsers().get(1).toString().contains("miko2")).isEqualTo(true);
        assertThat(serviceUser.getAllUsers().get(2).toString().contains("miko3")).isEqualTo(true);

    }

    @Test
    @DisplayName("checks if before saving user service validates the given user object")
    public void checkIfValidatesBeforeSave(){
        User userBadSave = new User();
        assertThat(serviceUser.saveUser(userBadSave).getMessage()).isEqualTo(Utils.EMAIL_OR_PASS_NOT_GIVEN);
        assertThat(serviceUser.saveUser(userBadSave).getData()).isEqualTo(Optional.empty());
        userRepo.save(Dummy.dummyUser(1,"miko1"));
        User userExistingEmail = Dummy.dummyUser(2,"miko1");
        assertThat(serviceUser.saveUser(userExistingEmail).getMessage()).isEqualTo(Utils.EMAIL_EXISTS);
        assertThat(serviceUser.saveUser(userExistingEmail).getData()).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checks if saves user correctly")
    public void checkIfSavesCorrectly(){
        User correctUser = Dummy.dummyUser(1,"miko2002");
        var serviceResponse = serviceUser.saveUser(correctUser);
        assertThat(serviceResponse.getData()).isEqualTo(Optional.of(correctUser));
        assertThat(serviceResponse.getMessage()).isEqualTo(Utils.ACCOUNT_CREATED);
        assertThat(userRepo.findById(1).isPresent()).isEqualTo(true);
    }

    @Test
    @DisplayName("checks if returns user by id correctly")
    public void checkIfGetsByIdCorrectly(){
        User correctUser = Dummy.dummyUser(1,"miko2002");
        userRepo.save(correctUser);
        var serviceResponseSuccess = serviceUser.findUserById(1);
        assertThat(serviceResponseSuccess).isEqualTo(Optional.of(correctUser));

        var serviceResponseFail = serviceUser.findUserById(20);
        assertThat(serviceResponseFail).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checks if returns user by email correctly")
    public void checkIfGetsByEmailCorrectly(){
        User correctUser = Dummy.dummyUser(1,"miko2002");
        userRepo.save(correctUser);
        var serviceResponseSuccess = serviceUser.findUserByEmail("miko2002");
        assertThat(serviceResponseSuccess).isEqualTo(Optional.of(correctUser));


        var serviceResponseFail = serviceUser.findUserByEmail("randomEmail");
        assertThat(serviceResponseFail).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checks if updates user by id correctly")
    public void checkIfUpdatesByIdCorrectly(){
        userRepo.save(Dummy.dummyUser(1,"oldEmail"));
        User updatedUser = Dummy.dummyUser(1,"miko2002");
        var serviceResponseSuccess = serviceUser.updateUserById(1, updatedUser);
        assertThat(serviceResponseSuccess).isEqualTo(Optional.of(updatedUser));
        assertThat(userRepo.findById(1).isPresent()).isEqualTo(true);
        assertThat(userRepo.findById(1).get().getEmail()).isEqualTo("miko2002");

        var serviceResponseFail = serviceUser.updateUserById(20, updatedUser);
        assertThat(serviceResponseFail).isEqualTo(Optional.empty());
    }


    @Test
    @DisplayName("checks if updates user by email correctly")
    public void checkIfUpdatesByEmailCorrectly(){
        userRepo.save(Dummy.dummyUser(1,"miko2002"));
        User changedUser = Dummy.dummyUser(1,"miko2002");
        changedUser.setPassword("new password");
        ServiceResponse<User> serviceResponseSuccess = serviceUser.updateUserByEmail(changedUser);
        assertThat(serviceResponseSuccess.getMessage()).isEqualTo(Utils.ACCOUNT_UPDATED);
        assertThat(serviceResponseSuccess.getData()).isEqualTo(Optional.of(changedUser));
        assertThat(serviceResponseSuccess.getData().get().toString().contains("new password")).isEqualTo(true);
        assertThat(userRepo.existsByEmail("miko2002")).isEqualTo(true);
        assertThat(userRepo.findByEmail("miko2002").get().getPassword()).isEqualTo("new password");

        User unknownUser = Dummy.dummyUser(1,"unknownEmail");
        var serviceResponseEmailNotFound = serviceUser.updateUserByEmail(unknownUser);
        assertThat(serviceResponseEmailNotFound.getData()).isEqualTo(Optional.empty());
        assertThat(serviceResponseEmailNotFound.getMessage()).isEqualTo(Utils.EMAIL_NOT_FOUND);

        User nullUser = new User();
        var serviceResponseNullUser = serviceUser.updateUserByEmail(nullUser);
        assertThat(serviceResponseNullUser.getData()).isEqualTo(Optional.empty());
        assertThat(serviceResponseNullUser.getMessage()).isEqualTo(Utils.EMAIL_NOT_GIVEN);
    }

    @Test
    @DisplayName("checks if changes user by id correctly")
    public void checkIfChangesByIdCorrectly(){
        userRepo.save(Dummy.dummyUser(1,"oldEmail"));
        User changedUser = new User(1,"oldEmail", null, true, null,76d);
        var serviceResponseSuccess = serviceUser.changeUser(1, changedUser);
        assertThat(serviceResponseSuccess.isPresent()).isEqualTo(true);
        assertThat(serviceResponseSuccess.get().toString().contains("password='secret'")).isEqualTo(true);//checks if old value persists
        assertThat(serviceResponseSuccess.get().toString().contains("premiumStatus=true")).isEqualTo(true);
        assertThat(serviceResponseSuccess.get().toString().contains("walletWorth=76.0")).isEqualTo(true);

        var user = userRepo.findById(1);
        assertThat(user.isPresent()).isEqualTo(true);
        assertThat(user.get().getPassword()).isEqualTo("secret");
        assertThat(user.get().isPremiumStatus()).isEqualTo(true);
        assertThat(user.get().getWalletWorth()).isEqualTo(76d);

        var serviceResponseFail = serviceUser.changeUser(20, changedUser);
        assertThat(serviceResponseFail).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checks if checks if account with a given email already exists")
    public void checkExistsByEmail(){
        userRepo.save(Dummy.dummyUser(1,"miko2002"));
        assertThat(serviceUser.checkIfExistsByEmail("miko2002")).isEqualTo(true);
        assertThat(serviceUser.checkIfExistsByEmail("unknown")).isEqualTo(false);
    }

    @Test
    @DisplayName("checks if changes user by email correctly")
    public void checkIfChangesByEmailCorrectly(){
        userRepo.save(Dummy.dummyUser(1,"miko2002"));
        User updatedUser = new User(1,"miko2002", null, true, null,68d);
        ServiceResponse<User> serviceResponseSuccess = serviceUser.changeUserByEmail(updatedUser);
        assertThat(serviceResponseSuccess.getMessage()).isEqualTo(Utils.ACCOUNT_UPDATED);
        assertThat(serviceResponseSuccess.getData().isPresent()).isEqualTo(true);
        assertThat(serviceResponseSuccess.getData().get().toString().contains("password='secret'")).isEqualTo(true);//checks if old value persists
        assertThat(serviceResponseSuccess.getData().get().toString().contains("premiumStatus=true")).isEqualTo(true);
        assertThat(serviceResponseSuccess.getData().get().toString().contains("walletWorth=68.0")).isEqualTo(true);
        var user = userRepo.findById(1);
        assertThat(user.isPresent()).isEqualTo(true);
        assertThat(user.get().getPassword()).isEqualTo("secret");
        assertThat(user.get().isPremiumStatus()).isEqualTo(true);
        assertThat(user.get().getWalletWorth()).isEqualTo(68d);

        User unknownUser = Dummy.dummyUser(1,"unknownEmail");
        var serviceResponseEmailNotFound = serviceUser.changeUserByEmail(unknownUser);
        assertThat(serviceResponseEmailNotFound.getData()).isEqualTo(Optional.empty());
        assertThat(serviceResponseEmailNotFound.getMessage()).isEqualTo(Utils.EMAIL_NOT_FOUND);

        User nullUser = new User();
        var serviceResponseNullUser = serviceUser.changeUserByEmail(nullUser);
        assertThat(serviceResponseNullUser.getData()).isEqualTo(Optional.empty());
        assertThat(serviceResponseNullUser.getMessage()).isEqualTo(Utils.EMAIL_NOT_GIVEN);
    }

    @Test
    @DisplayName("checks if changes password correctly")
    public void checkIfChangesPasswordCorrectly(){
        userRepo.save(Dummy.dummyUser(1,"miko2002"));
        User updatedUser = new User(1,"miko2002", "new password", true, null,68d);
        ServiceResponse<User> serviceResponseSuccess = serviceUser.changePassword("miko2002","secret","new password");
        assertThat(serviceResponseSuccess.getMessage()).isEqualTo(Utils.PASS_CHANGED);
        assertThat(serviceResponseSuccess.getData().isPresent()).isEqualTo(true);
        assertThat(serviceResponseSuccess.getData().get().toString().contains("password='new password'")).isEqualTo(true);
        assertThat(userRepo.findById(1).get().getPassword()).isEqualTo("new password");

        ServiceResponse<User> serviceResponseBadPassword = serviceUser.changePassword("miko2002", "wrong password", "new password");
        assertThat(serviceResponseBadPassword.getData()).isEqualTo(Optional.empty());
        assertThat(serviceResponseBadPassword.getMessage()).isEqualTo(Utils.BAD_PASSWORD);

        var serviceResponseEmailNotFound = serviceUser.changePassword("unknownEmail", "secret", "new password");
        assertThat(serviceResponseEmailNotFound.getData()).isEqualTo(Optional.empty());
        assertThat(serviceResponseEmailNotFound.getMessage()).isEqualTo(Utils.EMAIL_NOT_FOUND);

        var serviceResponseNullUser = serviceUser.changePassword(null, "secret", "new password");
        assertThat(serviceResponseNullUser.getData()).isEqualTo(Optional.empty());
        assertThat(serviceResponseNullUser.getMessage()).isEqualTo(Utils.EMAIL_NOT_GIVEN);
    }

    @Test
    @DisplayName("checks if adds funds correctly to the account")
    public void checkIfAddsFundsCorrectly(){
        userRepo.save(Dummy.dummyUser(1,"miko2002"));
        ServiceResponse<User> responseSuccess = serviceUser.addFunds(1,60d);
        assertThat(responseSuccess.getMessage()).isEqualTo(Utils.ACCOUNT_UPDATED);
        assertThat(userRepo.findById(1).isPresent()).isEqualTo(true);
        assertThat(userRepo.findById(1).get().getWalletWorth()).isEqualTo(60d);
        assertThat(responseSuccess.getData()).isEqualTo(userRepo.findById(1));
        responseSuccess = serviceUser.addFunds(1,140d);
        assertThat(responseSuccess.getMessage()).isEqualTo(Utils.ACCOUNT_UPDATED);
        assertThat(userRepo.findById(1).get().getWalletWorth()).isEqualTo(60d + 140d);

        ServiceResponse<User> responseFail = serviceUser.addFunds(20,46d);
        assertThat(responseFail.getMessage()).isEqualTo(Utils.ID_NOT_FOUND);
        assertThat(responseFail.getData()).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checks if deletes user by id correctly")
    public void checkIfDeletesUserById(){
        userRepo.save(Dummy.dummyUser(1,"miko2002"));
        var response = serviceUser.deleteUserById(1);
        assertThat(response.getData()).isEqualTo(Optional.empty());
        assertThat(response.getMessage()).isEqualTo(Utils.ACCOUNT_UPDATED);
        var responseFail = serviceUser.deleteUserById(20);
        assertThat(responseFail.getData()).isEqualTo(Optional.empty());
        assertThat(responseFail.getMessage()).isEqualTo(Utils.ID_NOT_FOUND);
    }

    @Test
    @DisplayName("checks if deletes user by email correctly")
    public void checkIfDeletesUserByEmail(){
        userRepo.save(Dummy.dummyUser(1,"miko2002"));
        var response = serviceUser.deleteUserByEmail("miko2002");
        assertThat(response.getData()).isEqualTo(Optional.empty());
        assertThat(response.getMessage()).isEqualTo(Utils.ACCOUNT_UPDATED);
        var responseFail = serviceUser.deleteUserByEmail("unknown");
        assertThat(responseFail.getData()).isEqualTo(Optional.empty());
        assertThat(responseFail.getMessage()).isEqualTo(Utils.EMAIL_NOT_FOUND);
        var responseNull = serviceUser.deleteUserByEmail(null);
        assertThat(responseNull.getMessage()).isEqualTo(Utils.EMAIL_NOT_GIVEN);
        assertThat(responseNull.getData()).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("checks if returns a list of user orders")
    public void checksIfGetsListOfUserOrders(){
        var user1 = new User(1, "miko2002", "secret", false,null,800d);
        var user2 = new User(2, "miki123", "secret", false,null,800d);
        userRepo.save(user1);userRepo.save(user2);
        var product1 = new Product(1,"product1", 45d);
        var product2 = new Product(2,"product2", 25d);
        var product3 = new Product(3,"product3", 75d);
        productRepo.save(product1); productRepo.save(product2); productRepo.save(product3);
        var orderItem1 = new OrderItemDTO(1,1,2);// 45 *2 = 90
        var orderItem2 = new OrderItemDTO(1,2,3);// 25 * 3 = 75
        var orderItem3 = new OrderItemDTO(2,3,4); // 75*4 = 300
        var orderItem4 = new OrderItemDTO(3,2,1); // 25
        List<OrderItemDTO> listOrder1 = new LinkedList<>(){{
            add(orderItem1);
            add(orderItem2);
            // 90 + 75 = 165
        }};
        List<OrderItemDTO> listOrder2 = new LinkedList<>(){{
            add(orderItem3); //300
          }};
        List<OrderItemDTO> listOrder3 = new LinkedList<>(){{
            add(orderItem4); //25
        }};

        OrderReadDTO order1 = new OrderReadDTO("miko2002", listOrder1);
        OrderReadDTO order2 = new OrderReadDTO("miko2002", listOrder2);
        OrderReadDTO order3 = new OrderReadDTO("miki123", listOrder3);
        orderService.saveOrder(order1);
        orderService.saveOrder(order2);
        orderService.saveOrder(order3);

        assertThat(serviceUser.getListOfOrdersOfUser(user1).isPresent()).isEqualTo(true);
        assertThat(serviceUser.getListOfOrdersOfUser(user1).get().get(0).getTotalAmount()).isEqualTo(165d);
        assertThat(serviceUser.getListOfOrdersOfUser(user1).get().get(1).getTotalAmount()).isEqualTo(300d);
        assertThat(serviceUser.getListOfOrdersOfUser(user2).isPresent()).isEqualTo(true);
        assertThat(serviceUser.getListOfOrdersOfUser(user2).get().get(0).getTotalAmount()).isEqualTo(25d);

        assertThat(serviceUser.getListOfOrdersOfUser("miko2002").isPresent()).isEqualTo(true);
        assertThat(serviceUser.getListOfOrdersOfUser("miko2002").get().get(0).getTotalAmount()).isEqualTo(165d);
        assertThat(serviceUser.getListOfOrdersOfUser("miko2002").get().get(1).getTotalAmount()).isEqualTo(300d);
        assertThat(serviceUser.getListOfOrdersOfUser("miki123").isPresent()).isEqualTo(true);
        assertThat(serviceUser.getListOfOrdersOfUser("miki123").get().get(0).getTotalAmount()).isEqualTo(25d);

        assertThat(serviceUser.getListOfOrdersOfUser(1).isPresent()).isEqualTo(true);
        assertThat(serviceUser.getListOfOrdersOfUser(1).get().get(0).getTotalAmount()).isEqualTo(165d);
        assertThat(serviceUser.getListOfOrdersOfUser(1).get().get(1).getTotalAmount()).isEqualTo(300d);
        assertThat(serviceUser.getListOfOrdersOfUser(2).isPresent()).isEqualTo(true);
        assertThat(serviceUser.getListOfOrdersOfUser(2).get().get(0).getTotalAmount()).isEqualTo(25d);
    }

    @Test
    @DisplayName("check if deletes all users correctly")
    public void checkIfDeletesAllUsers(){
        userRepo.save(Dummy.dummyUser(1,"miko2002"));
        userRepo.save(Dummy.dummyUser(2,"miko2215"));

        serviceUser.deleteAllUsers();
        assertThat(userRepo.findAll().isEmpty()).isEqualTo(true);
    }

}