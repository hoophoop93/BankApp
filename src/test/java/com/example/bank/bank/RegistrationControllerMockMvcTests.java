package com.example.bank.bank;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes={BankApplication.class})
public class RegistrationControllerMockMvcTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void registrationTestWrongEmail() throws Exception {
        this.mockMvc.perform(
                post("/register")
                        .param("imie", "Gal")
                        .param("nazwisko", "Anonim")
                        .param("adres", "Nieznany")
                        .param("pesel", "12345678909")
                        .param("email", "galanonim@")// Wrong email.
                        .param("haslo", "password1234")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/unauthorised/register")); // Same page with errors displayed.
    }

    @Test
    public void registrationTestCorrectEmail() throws Exception {
        this.mockMvc.perform(
                post("/register")
                        .param("imie", "Gal")
                        .param("nazwisko", "Anonim")
                        .param("adres", "Nieznany")
                        .param("pesel", "12345678909")
                        .param("email", "galanonim@wp.pl")
                        .param("haslo", "password1234") //dobrze wypełniony formularz
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/unauthorised/register")); // Same page with errors displayed.
    }
}
