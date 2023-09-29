package com.miko.bookapp;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.model.OrderItem;
import com.miko.bookapp.model.Product;
import com.miko.bookapp.repo.*;
import com.miko.bookapp.service.*;
import com.miko.bookapp.service.Implementation.ServiceOrderImplementation;
import com.miko.bookapp.service.Implementation.ServiceUserImplementation;
import com.miko.bookapp.service.Implementation.ServiceBookImplementation;
import com.miko.bookapp.service.Implementation.ServiceBundleImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.*;

@Configuration
@Profile("integration")
public class TestConfiguration {

    @Bean
    @Primary
    DataSource testDataSource(){
        var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }

    @Bean
    @Primary
    BookRepo testBookRepo(){
        return new MemoryBookRepo();
    }

    @Bean
    @Primary
    BundleRepo testBundleRepo(){
        return new MemoryBundleRepo();
    }

    @Bean
    @Primary
    ProductRepo testProductRepo(){
        return new MemoryProductRepo();
    }

    @Bean
    @Primary
    OrderItemRepo testOrderItemRepo() {
        return new MemoryOrderItemRepo();
    }

    @Bean
    @Primary
    UserRepo testUserRepo() {
        return new MemoryUserRepo();
    }

    @Bean
    @Primary
    OrderRepo testOrderRepo(){
        return new MemoryOrderRepo();
    }


    @Bean
    @Primary
    ServiceBook testServiceBook(){
        return new ServiceBookImplementation(testBookRepo(), testProductRepo());
    }

    @Bean
    @Primary
    ServiceBundle testServiceBundle(){
        return new ServiceBundleImplementation(testBundleRepo(), testBookRepo(), testProductRepo());
    }

    @Bean
    @Primary
    ServiceUser testServiceUser(){
        return new ServiceUserImplementation(testUserRepo(),testOrderRepo());
    }

    @Bean
    @Primary
    ServiceOrder testServiceOrder(){
        return new ServiceOrderImplementation(testOrderRepo(),testOrderItemRepo(),testUserRepo(),testProductRepo());
    }

}
