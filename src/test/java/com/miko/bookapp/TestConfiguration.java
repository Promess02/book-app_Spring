package com.miko.bookapp;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.model.OrderItem;
import com.miko.bookapp.model.Product;
import com.miko.bookapp.repo.*;
import com.miko.bookapp.service.MemoryOrderRepo;
import com.miko.bookapp.service.ServiceBook;
import com.miko.bookapp.service.Implementation.ServiceBookImplementation;
import com.miko.bookapp.service.ServiceBundle;
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
        return new BookRepo() {
            @Override
            public Book saveEntity(Book entity) {
                if (entity.getId()==0){
                    try{
                        var field = Book.class.getSuperclass().getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity,++index);
                    }catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                map.put(entity.getId(), entity);

                return entity;
            }

            private int index = 0;
            private Map<Long, Book> map = new HashMap<>();
            @Override
            public List<Book> findAll() {
                return new ArrayList<>(map.values());
            }

            @Override
            public Book save(Book entity) {
                if (entity.getId()==0){
                    try{
                        var field = Book.class.getSuperclass().getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity,++index);
                    }catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                map.put(entity.getId(), entity);

                return entity;
            }

            @Override
            public Optional<Book> findById(long id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public boolean existsById(long id) {
                return map.containsKey(id);
            }

            @Override
            public long count() {
                return map.size();
            }

            @Override
            public void deleteById(long id) {
                map.remove(id);
            }

            @Override
            public void delete(Book entity) {
                map.remove(entity);
            }

            @Override
            public void deleteAll() {
                map.clear();
            }

            @Override
            public long getNextGeneratedId() {
                return index+1;
            }
        };
    }

    @Bean
    @Primary
    BundleRepo testBundleRepo(){
        return new BundleRepo() {
            @Override
            public BookBundle saveEntity(BookBundle entity) {
                if (entity.getId()==0){
                    try{
                        var field = BookBundle.class.getSuperclass().getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity,++index);
                    }catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                map.put(entity.getId(), entity);

                return entity;
            }

            private int index = 0;
            private Map<Long, BookBundle> map = new HashMap<>();
            @Override
            public List<BookBundle> findAll() {
                return new ArrayList<>(map.values());
            }

            @Override
            public BookBundle save(BookBundle entity) {
                if (entity.getId()==0){
                    try{
                        var field = BookBundle.class.getSuperclass().getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity,++index);
                    }catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                map.put(entity.getId(), entity);

                return entity;
            }

            @Override
            public Optional<BookBundle> findById(long id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public boolean existsById(long id) {
                return map.containsKey(id);
            }

            @Override
            public long count() {
                return map.size();
            }

            @Override
            public void deleteById(long id) {
                map.remove(id);
            }

            @Override
            public void delete(BookBundle entity) {
                map.remove(entity.getId(),entity);
            }

            @Override
            public void deleteAll() {
                map.clear();
            }

            @Override
            public long getNextGeneratedId() {
                return index+1;
            }
        };
    }

    @Bean
    @Primary
    ProductRepo testProductRepo(){
        return new ProductRepo() {
            private int index = 0;
            private Map<Long, Product> map = new HashMap<>();


            @Override
            public Product saveEntity(Product entity) {
                if (entity.getId()==0){
                    try{
                        var field = Product.class.getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity,++index);
                    }catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                map.put(entity.getId(), entity);

                return entity;
            }

            @Override
            public List<Product> findAll() {
                return new ArrayList<>(map.values());
            }

            @Override
            public Product save(Product entity) {
                if (entity.getId()==0){
                    try{
                        var field = Product.class.getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity,++index);
                    }catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                map.put(entity.getId(), entity);

                return entity;
            }

            @Override
            public Optional<Product> findById(long id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public boolean existsById(long id) {
                return map.containsKey(id);
            }

            @Override
            public long count() {
                return map.size();

            }

            @Override
            public void deleteById(long id) {
                map.remove(id);
            }

            @Override
            public void delete(Product entity) {
                map.remove(entity.getId(),entity);

            }

            @Override
            public void deleteAll() {
                map.clear();
            }

            @Override
            public long getNextGeneratedId() {
                return index+1;
            }
        };
    }

    @Bean
    @Primary
    OrderItemRepo testOrderItemRepo() {
        return new OrderItemRepo() {
            private int index = 0;
            private Map<Long, OrderItem> map = new HashMap<>();


            @Override
            public List<OrderItem> findAll() {
                return new ArrayList<>(map.values());
            }

            @Override
            public OrderItem save(OrderItem entity) {
                if (entity.getId()==0){
                    try{
                        var field = OrderItem.class.getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity,++index);
                    }catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                map.put(entity.getId(), entity);

                return entity;    }

            @Override
            public Optional<OrderItem> findById(long id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public boolean existsById(long id) {
                return map.containsKey(id);
            }

            @Override
            public long count() {
                return map.size();

            }

            @Override
            public void deleteById(long id) {
                map.remove(id);
            }

            @Override
            public void delete(OrderItem entity) {
                map.remove(entity.getId(),entity);
            }

            @Override
            public void deleteAll() {
                map.clear();
            }

            @Override
            public long getNextGeneratedId() {
                return map.size()+1;
            }
        };
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

}
