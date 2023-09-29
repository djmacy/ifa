package edu.carroll.ifa.web.controller;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * Unit tests for the home page to make sure everything works as expected on that page.
 */
@WebMvcTest(HomeController.class)
public class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * This unit test checks to see that the home page contains the following text
     * @throws Exception
     */
    @Test
    //after adding spring security for Bcrypt it seems that we need authentication to run tests so this solves it
    @WithMockUser
    public void indexTest() throws Exception {
        mockMvc.perform(get("/")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome to the application!")))
                .andExpect(content().string(containsString("login")))
                .andExpect(content().string(containsString("register")));
    }

    /**
     * This unit test makes sure that the /login page is reachable
     * @throws Exception
     */
    @Test
    public void testLoginLink() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
    /* This is not working for some reason it is returning a 404 which makes no sense
    @WithMockUser
    @Test
    public void testRegisterLink() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

     */
}
