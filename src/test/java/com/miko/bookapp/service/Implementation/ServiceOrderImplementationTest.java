package com.miko.bookapp.service.Implementation;

import com.miko.bookapp.repo.OrderItemRepo;
import com.miko.bookapp.repo.OrderRepo;
import com.miko.bookapp.repo.ProductRepo;
import com.miko.bookapp.repo.UserRepo;
import com.miko.bookapp.service.Implementation.ServiceOrderImplementation;
import com.miko.bookapp.service.MemoryOrderItemRepo;
import com.miko.bookapp.service.MemoryOrderRepo;
import com.miko.bookapp.service.MemoryProductRepo;
import com.miko.bookapp.service.MemoryUserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

public class ServiceOrderImplementationTest {
    private static OrderRepo orderRepo;
    private static ProductRepo productRepo;
    private static UserRepo userRepo;
    private static OrderItemRepo orderItemRepo;
    private static ServiceOrderImplementation service;

    @BeforeAll
    public static void setUp(){
        orderRepo = new MemoryOrderRepo();
        productRepo = new MemoryProductRepo();
        userRepo = new MemoryUserRepo();
        orderItemRepo = new MemoryOrderItemRepo();
        service = new ServiceOrderImplementation(orderRepo,orderItemRepo,userRepo,productRepo);
    }

    @AfterEach
    public void tearDown(){
        orderRepo.deleteAll();
        productRepo.deleteAll();
        userRepo.deleteAll();
        orderItemRepo.deleteAll();
    }





}
