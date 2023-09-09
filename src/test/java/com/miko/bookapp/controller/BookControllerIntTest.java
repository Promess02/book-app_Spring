package com.miko.bookapp.controller;

import com.miko.bookapp.Dummy;
import com.miko.bookapp.service.ServiceBook;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@ActiveProfiles("integration")
@AutoConfigureMockMvc
public class BookControllerIntTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ServiceBook serviceBook;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    void httpGet_returnsGivenBook(){
        long id = serviceBook.saveBook(Dummy.dummyBook(1,"book1")).getId();
        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/books/list/" + id))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("book1")));
        }catch (Exception e){
            throw new RuntimeException(e + "failed getting book");
        }
    }

    @Test
    void httpGet_returnsAllBooks(){
        serviceBook.saveBook(Dummy.dummyBook(1, "book1"));
        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/books/list"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }catch (Exception e){
            throw new RuntimeException(e + "failed getting book");
        }
    }

    @Test
    void httpPost_createsBook(){
        var json = new JSONObject();
        try {
            json.put("isbn", 56);
            json.put("name", "Mistborn");
            json.put("author", "Sanderson");
            json.put("description", "cool book");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/books/create")
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(json.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("cool book")));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
