package com.miko.bookapp.controller;

import com.miko.bookapp.Dummy;
import com.miko.bookapp.Utils;
import com.miko.bookapp.service.ServiceUser;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@AutoConfigureMockMvc
public class UserControllerIntTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ServiceUser serviceUser;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @AfterEach
    void tearDown() {
        serviceUser.deleteAllUsers();
    }

    @Test
    @DisplayName("checks if returns all users correctly")
    public void checkIfReturnsAllUsers(){
        serviceUser.saveUser(Dummy.dummyUser(1,"miko2002"));
        serviceUser.saveUser(Dummy.dummyUser(2,"miki123"));

        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/users/list"))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("all users retrieved")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("miko2002")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("miki123")))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }catch (Exception e) {
            throw new RuntimeException(e + "failed getting all users");
        }
    }

    @Test
    @DisplayName("checks if returns user correctly")
    public void checkIfReturnsUserById(){
        serviceUser.saveUser(Dummy.dummyUser(1,"miko2002"));

        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/users/list/1"))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("user with id: 1 retrieved")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("miko2002")))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }catch (Exception e) {
            throw new RuntimeException(e + "failed getting a user with id: 1");
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/users/list/23"))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("id of class: User not found")))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e + "failed getting a user with id: 23");
        }
    }

    @Test
    @DisplayName("checks if returns user by email correctly")
    public void checkIfReturnsUserByEmail(){
        serviceUser.saveUser(Dummy.dummyUser(1,"miko2002"));

        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/users/listByEmail")
                            .content("miko2002")
                            .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("user with email: miko2002 retrieved")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("miko2002")))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }catch (Exception e) {
            throw new RuntimeException(e + "failed getting a user with email: miko2002");
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/users/listByEmail")
                            .content( "unknown"))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("account with email address: unknown not found")))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e + "failed getting a user with email: unknown");
        }
    }

    @Test
    @DisplayName("checks if updates user by id correctly")
    public void checkIfUpdatesUserById(){
        serviceUser.saveUser(Dummy.dummyUser(1,"miko2002"));
        JSONObject json;
        try{
            json = new JSONObject(){{
                put("email", "updatedEmail@");
                        put("password", "newPassword");
                        put("premiumStatus", true);
                        put("walletWorth", 85.4d);
            }};
        } catch (JSONException e) {
            throw new RuntimeException(e + " error creating json");
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.put("/users/updateUser/1")
                            .content(json.toString())
                            .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("user with id: 1 updated")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("updatedEmail@")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("newPassword")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("true")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("85.4")));
        } catch (Exception e) {
            throw new RuntimeException(e + " error updating user with id: 1");
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.put("/users/updateUser/23")
                    .content(new JSONObject(){{put("email", "irrelevant");}}.toString())
                            .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("id of class: User not found")));
        } catch (Exception e) {
            throw new RuntimeException(e + " error updating user with id: 23");
        }
    }

    @Test
    @DisplayName("checks if updates user by email correctly")
    public void checkIfUpdatesUserByEmail(){
        serviceUser.saveUser(Dummy.dummyUser(1,"miko2002"));
        JSONObject json;
        try{
            json = new JSONObject(){{
                put("email", "miko2002");
                put("password", "newPassword");
                put("premiumStatus", true);
                put("walletWorth", 85.4d);
            }};
        } catch (JSONException e) {
            throw new RuntimeException(e + " error creating json");
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.put("/users/updateUserByEmail")
                            .content(json.toString())
                            .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.ACCOUNT_UPDATED)))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("miko2002")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("newPassword")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("true")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("85.4")));
        } catch (Exception e) {
            throw new RuntimeException(e + " error updating user with email miko2002");
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.put("/users/updateUserByEmail")
                            .content(new JSONObject(){{put("password", "secretvalue");put("walletWorth",75d);}}.toString())
                            .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.EMAIL_NOT_GIVEN)));
        } catch (Exception e) {
            throw new RuntimeException(e + " error updating user");
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.put("/users/updateUserByEmail")
                            .content(new JSONObject(){{put("email", "givenEmail@");}}.toString())
                            .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.EMAIL_NOT_FOUND)));
        }catch (Exception e) {
            throw new RuntimeException(e + " error updating user");
        }
    }

    @Test
    @DisplayName("checks if changes user correctly")
    public void checkIfChangesCorrectly(){
        serviceUser.saveUser(Dummy.dummyUser(1,"miko2002"));
        JSONObject json;
        try{
            json = new JSONObject(){{
                put("email", "miko2002");
                put("premiumStatus", true);
                put("walletWorth", 85.4d);
            }};
        } catch (JSONException e) {
            throw new RuntimeException(e+" error creating json");
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.patch("/users/changeUser")
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(json.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("secret"))) //secret - default password for dummy user
                    .andExpect(MockMvcResultMatchers.content().string(containsString("true")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("85.4")));
        } catch (Exception e) {
            throw new RuntimeException(e + ": error changing user");
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.patch("/users/changeUser")
                            .content(new JSONObject(){{put("password", "secretvalue");put("walletWorth",75d);}}.toString())
                            .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.EMAIL_NOT_GIVEN)));
        } catch (Exception e) {
            throw new RuntimeException(e + " error updating user");
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.patch("/users/changeUser")
                            .content(new JSONObject(){{put("email", "givenEmail@");}}.toString())
                            .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.EMAIL_NOT_FOUND)));
        }catch (Exception e) {
            throw new RuntimeException(e + " error updating user");
        }
    }

    @Test
    @DisplayName("checks if creates user correctly")
    public void checkIfCreatesUserCorrectly(){
        JSONObject json;

        try{
            json = new JSONObject(){{
                put("email", "miko2002");
                put("password", "newPassword");
                put("premiumStatus", true);
                put("walletWorth", 85.4d);
            }};
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(json.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("miko2002")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("newPassword")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("true")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("85.4")));
        } catch (Exception e) {
            throw new RuntimeException(e + ": error creating user");
        }
        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                            .content(new JSONObject(){{put("password", "secretvalue");put("walletWorth",75d);}}.toString())
                            .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.EMAIL_OR_PASS_NOT_GIVEN)));

        } catch (Exception e) {
            throw new RuntimeException(e + ": error creating user");
        }
        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(json.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.EMAIL_EXISTS)));
        } catch (Exception e) {
            throw new RuntimeException(e + ": error creating user");
        }
    }

    @Test
    @DisplayName("checks if deletes user by id and email correctly")
    public void checksIfDeletesUser(){
        serviceUser.saveUser(Dummy.dummyUser(1,"miko2002"));
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("user with id: 1 deleted")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/24"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.ID_NOT_FOUND)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        serviceUser.saveUser(Dummy.dummyUser(1,"miko2002"));
        try{
            mockMvc.perform(MockMvcRequestBuilders.delete("/users/deleteByEmail")
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(new JSONObject(){{
                        put("email", "unknown");
                    }}.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.EMAIL_NOT_FOUND)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/users/deleteByEmail")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content("miko2002"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("successfully deleted user with email: miko2002")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try{
            mockMvc.perform(MockMvcRequestBuilders.delete("/users/deleteByEmail")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content(""))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.EMAIL_NOT_GIVEN)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("checks if adds funds to account correctly")
    public void checkIfAddsFunds(){
        serviceUser.saveUser(Dummy.dummyUser(1,"miko2002"));
        try{
            mockMvc.perform(MockMvcRequestBuilders.patch("/users/addFunds/1")
                    .contentType(APPLICATION_JSON_UTF8)
                    .content("84.0"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("added funds: 84.0 to account with id: 1")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            mockMvc.perform(MockMvcRequestBuilders.patch("/users/addFunds/23")
                    .contentType(APPLICATION_JSON_UTF8)
                    .content("34"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.ID_NOT_FOUND)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("checks if changes the password correctly")
    public void checkIfChangesPassword(){
        serviceUser.saveUser(Dummy.dummyUser(1,"miko2002"));
        JSONObject json;
        try{
            json = new JSONObject(){{
                put("email","miko2002");
                put("oldPassword","secret");
                put("newPassword","correctPass");
            }};
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try{
            mockMvc.perform(MockMvcRequestBuilders.patch("/users/changePassword")
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(json.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(containsString("correctPass")))
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.PASS_CHANGED)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        serviceUser.saveUser(Dummy.dummyUser(2,"newEmail"));
        JSONObject jsonBadPass;
        try{
            jsonBadPass = new JSONObject(){{
                put("email","newEmail");
                put("oldPassword","wrongPass");
                put("newPassword","correctPass");
            }};
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.patch("/users/changePassword")
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(jsonBadPass.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.BAD_PASSWORD)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.patch("/users/changePassword")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content(new JSONObject(){{put("email","unknownEmail");}}.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.EMAIL_NOT_FOUND)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.patch("/users/changePassword")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content(new JSONObject(){{put("oldPassword","unknownValue");}}.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(containsString(Utils.EMAIL_NOT_GIVEN)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
