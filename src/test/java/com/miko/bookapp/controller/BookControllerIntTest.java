package com.miko.bookapp.controller;

import com.miko.bookapp.Dummy;
import com.miko.bookapp.enums.BookCategory;
import com.miko.bookapp.enums.BookType;
import com.miko.bookapp.model.Book;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    @Test
    void httpPost_updatesBook(){

        serviceBook.saveBook(Dummy.dummyBook(1, "old desc"));
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
            mockMvc.perform(MockMvcRequestBuilders.put("/books/update/1")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content(json.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("cool book")));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void httpPatch_changeBook(){
        serviceBook.saveBook(Dummy.dummyBook(1, "old desc"));
        var json = new JSONObject();
        try {
            json.put("isbn", 56);
            json.put("name", "Mistborn");
            json.put("author", "Sanderson");
            json.put("bookCategory", "fantasy");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.patch("/books/update/1")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content(json.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    //check if value from before change is the same as after change
                    .andExpect(MockMvcResultMatchers.content().string(containsString("old desc")))
                    //check if changes a value
                    .andExpect(MockMvcResultMatchers.content().string(containsString("fantasy")));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void httpPatch_filterBooks(){
        serviceBook.saveBook(new Book(1,56,"name1", "author1", null, BookCategory.FANTASY, BookType.HARDCOVER, 30.5d,null));
        serviceBook.saveBook(new Book(2,34,"name2", "author2", null,BookCategory.DRAMA, BookType.AUDIOBOOK, 60.5d,null));
        serviceBook.saveBook(new Book(3,87,"name3", "author3", null,BookCategory.HORROR, BookType.EBOOK, 30.5d,null));

        var json = new JSONObject();
        try {
            json.put("bookCategory", "fantasy");
            json.put("bookType", "hardcover");
            json.put("prize", 80.4);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/books/filter")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content(json.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("List Of filtered Books retrieved")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("author1")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("hardcover")));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void httpDelete_deleteBook(){
        serviceBook.saveBook(Dummy.dummyBook(1, "old desc"));
        try{
            mockMvc.perform(MockMvcRequestBuilders.delete("/books/delete/1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("a book with id: 1 deleted")));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void checksIfBadEnumExceptionThrown(){
        serviceBook.saveBook(Dummy.dummyBook(1, "old desc"));
        var json = new JSONObject();
        try {
            json.put("bookCategory", "invalidCategory");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.patch("/books/update/1")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content(json.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("bad enum value given")));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void checksIfReturnsNoIdResponse(){
        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/books/list/145"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("id not found")));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }



}
