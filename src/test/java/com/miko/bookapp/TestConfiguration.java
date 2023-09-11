package com.miko.bookapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.repo.BookRepo;
import com.miko.bookapp.repo.BundleRepo;
import com.miko.bookapp.service.ServiceBook;
import com.miko.bookapp.service.ServiceBookImplementation;
import com.miko.bookapp.service.ServiceBundle;
import com.miko.bookapp.service.ServiceBundleImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
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
                        var field = Book.class.getDeclaredField("id");
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
        };
    }

    @Bean
    @Primary
    BundleRepo testBundleRepo(){
        return new BundleRepo() {
            private int index = 0;
            private Map<Long, BookBundle> map = new HashMap<>();
            @Override
            public List<BookBundle> findAll() {
                return new ArrayList<>(map.values());
            }

            @Override
            public BookBundle save(BookBundle entity) {
                if (entity.getBundleID()==0){
                    try{
                        var field = BookBundle.class.getDeclaredField("bundleID");
                        field.setAccessible(true);
                        field.set(entity,++index);
                    }catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                map.put(entity.getBundleID(), entity);

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
                map.remove(entity.getBundleID(),entity);
            }

            @Override
            public void deleteAll() {
                map.clear();
            }
        };
    }

    @Bean
    @Primary
    ServiceBook testServiceBook(){
        return new ServiceBookImplementation(testBookRepo());
    }

    @Bean
    @Primary
    ServiceBundle testServiceBundle(){
        return new ServiceBundleImplementation(testBundleRepo(), testBookRepo());
    }

}
