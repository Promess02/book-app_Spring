package com.miko.bookapp.controller;

import com.miko.bookapp.Dummy;
import com.miko.bookapp.model.Response;
import com.miko.bookapp.repo.BookRepo;
import com.miko.bookapp.service.ServiceBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private ServiceBook serviceBook;

    @Test
    void httpGet_returnsAllBooks(){
        var initial = serviceBook.getAllBooks().size();
        //given
        serviceBook.saveBook(Dummy.dummyBook(1, "desc1"));
        serviceBook.saveBook(Dummy.dummyBook(2, "desc2"));

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port +"/books/list", String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody()).contains("desc1", "desc2");
    }



}
