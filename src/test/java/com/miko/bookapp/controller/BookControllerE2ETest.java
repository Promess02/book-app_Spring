package com.miko.bookapp.controller;

import com.miko.bookapp.Dummy;
import com.miko.bookapp.TestConfiguration;
import com.miko.bookapp.model.Book;
import com.miko.bookapp.repo.BookRepo;
import com.miko.bookapp.service.ServiceBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource(locations = "classpath:application-integration.yml")
//@Import(TestConfiguration.class)
public class BookControllerE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepo bookRepo;

//    @Autowired
//    private BundleRepo bundleRepo;

    @Autowired
    private ServiceBook serviceBook;

    @Test
    void httpGet_returnsAllBooks(){
        var initial = serviceBook.getAllBooks().size();
        //given
        serviceBook.saveBook(Dummy.dummyBook(1, "desc1"));
        serviceBook.saveBook(Dummy.dummyBook(2, "desc2"));

        Book[] result = restTemplate.getForObject("http://localhost:" + port + "/books/list", Book[].class);
        assertThat(result).hasSize(2 + initial);
    }

}
