package com.miko.bookapp.controller;


import com.miko.bookapp.Dummy;
import com.miko.bookapp.service.ServiceBook;
import com.miko.bookapp.service.ServiceBundle;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@AutoConfigureMockMvc
public class BundleControllerIntTest {

    @Autowired
    private ServiceBundle serviceBundle;

    @Autowired
    private ServiceBook serviceBook;

    @Autowired
    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    @DisplayName("checks if returns all bundles")
    public void httpGet_returnBundle(){
        serviceBundle.saveBundle(Dummy.dummyBundle(1,"httpGetReturnBundle"));

        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/bundles/list"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("List Of Bundles retrieved")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("httpGetReturnBundle")));
        } catch (Exception e) {
            throw new RuntimeException(e + ": error getting a list of bundles");
        }
    }

    @Test
    @DisplayName("checks if returns a bundle with given id")
    public void httpGet_returnBundleById(){
        serviceBundle.saveBundle(Dummy.dummyBundle(1,"httpGetReturnBundleById"));

        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/bundles/list/1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("a bundle with id: 1 retrieved")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("httpGetReturnBundleById")));
        } catch (Exception e) {
            throw new RuntimeException(e + ": error getting a bundle");
        }
    }

    @Test
    @DisplayName("checks if returns a created bundle")
    public void httpPost_returnCreatedBundle(){
        var json = new JSONObject();

        try{
            json.put("description", "httpPostReturnCreatedBundle");
            json.put("discount", 0.56d);
        }catch (JSONException e){
            throw new RuntimeException(e + ": error creating json");
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/bundles/create")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content(json.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("a new book bundle added")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("httpPostReturnCreatedBundle")));
        } catch (Exception e) {
            throw new RuntimeException(e + ": error creating a bundle");
        }
    }

    @Test
    @DisplayName("checks if updates a bundle correctly")
    public void httpPUT_returnUpdatedBundle(){
        serviceBundle.saveBundle(Dummy.dummyBundle(1,"old desc"));
        var json = new JSONObject();
        try{
            json.put("discount", 0.34d);
            json.put("description", "httpPutReturnUpdatedBundle");
        }catch (JSONException e){
            throw new RuntimeException(e + ": error creating json");
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.put("/bundles/update/1")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content(json.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("a bundle with id: 1 updated")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("httpPutReturnUpdatedBundle")));
        } catch (Exception e) {
            throw new RuntimeException(e + ": error updating a bundle");
        }
    }

    @Test
    @DisplayName("checks if returns changed bundle (PATCH HTTP) ")
    public void httpPatch_returnChangedBundle(){
        // dummy.discount = 0.45d
        serviceBundle.saveBundle(Dummy.dummyBundle(1,"httpPatchReturnChangedBundle"));
        var json = new JSONObject();
        try{
            json.put("discount", 0.32d);
        }catch (JSONException e){
            throw new RuntimeException(e + ": error creating json");
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.patch("/bundles/update/1")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content(json.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("a bundle with id: 1 changed")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("httpPatchReturnChangedBundle")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("\"discount\":0.32")));
        } catch (Exception e) {
            throw new RuntimeException(e + ": error changing a bundle");
        }
    }

    @Test
    @DisplayName("checks if deletes a bundle correctly")
    public void httpDelete_returnDeletedBundle(){
        serviceBundle.saveBundle(Dummy.dummyBundle(1,"httpPatchReturnChangedBundle"));
        try{
            mockMvc.perform(MockMvcRequestBuilders.delete("/bundles/delete/1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("a bundle with id: 1 deleted")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("\"bundle: \":null")));
        } catch (Exception e) {
            throw new RuntimeException(e + ": error deleting a bundle");
        }
    }

    @Test
    @DisplayName("checks if adds a book to a bundle correctly")
    public void httpPatch_addBookToBundle(){
        serviceBook.saveBook(Dummy.dummyBook(1,"Book_addBookToBundle"));
        serviceBundle.saveBundle(Dummy.dummyBundle(1, "Bundle_addBookToBundle"));

        try{
            mockMvc.perform(MockMvcRequestBuilders.patch("/bundles/addBook/1/1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("added a book to bundle with id: 1")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("Bundle_addBookToBundle")));
        } catch (Exception e) {
            throw new RuntimeException(e + ": error adding a book to a bundle");
        }
    }

    @Test
    @DisplayName("checks if deletes a book from a bundle correctly")
    public void httpPatch_deleteBookFromBundle(){
        serviceBook.saveBook(Dummy.dummyBook(1,"Book_deleteBookFromBundle"));
        serviceBundle.saveBundle(Dummy.dummyBundle(1, "Bundle_deleteBookFromBundle"));

        try{
            mockMvc.perform(MockMvcRequestBuilders.patch("/bundles/deleteBook/1/1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("deleted a book with id: 1 from bundle with id: 1")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("Bundle_deleteBookFromBundle")));
        } catch (Exception e) {
            throw new RuntimeException(e + ": error deleting a book from a bundle");
        }
    }

    @Test
    @DisplayName("checks if changes discount from a bundle correctly")
    public void httpPatch_changeDiscount(){
        //dummyBundle.discount = 0.45
        serviceBundle.saveBundle(Dummy.dummyBundle(1, "Bundle_changeDiscountFromBundle"));

        try{
            mockMvc.perform(MockMvcRequestBuilders.patch("/bundles/discount/1/0.23"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("changed discount value of bundle with id: 1 to 0.23")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("Bundle_changeDiscountFromBundle")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("\"discount\":0.23")));
        } catch (Exception e) {
            throw new RuntimeException(e + ": error changing discount in the bundle");
        }
    }

    @Test
    @DisplayName("checks if returns a id not found response correctly")
    public void checkIfReturnsIdNotFoundResponse(){
        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/bundles/list/123"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("id not found")));
        } catch (Exception e) {
            throw new RuntimeException(e + ": error getting a bundle");
        }

    }

}
