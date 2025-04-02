package com.example.CookMaster.web;

import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.dish.service.DishService;
import com.example.CookMaster.app.ingredient.service.IngredientService;
import com.example.CookMaster.app.security.AuthenticationMetadata;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.service.UserService;
import com.example.CookMaster.app.web.DishController;
import com.example.CookMaster.app.web.dto.CreateDishRequest;
import com.example.CookMaster.app.web.dto.EditDishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DishController.class)
public class DishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DishService dishService;

    @MockBean
    private UserService userService;

    @MockBean
    private IngredientService ingredientService;

    @Autowired
    private ObjectMapper objectMapper;

    private User mockUser;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockUser.setId(UUID.randomUUID());
    }

   /* @Test
    void testGetCreateDishRequest_ShouldReturnCreateDishPage() throws Exception {
        mockMvc.perform(get("/dish/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-dish"))
                .andExpect(model().attributeExists("createDish", "dishTypes"));
    }

    @Test
    void testCreateDishRequest_ShouldRedirectToProfile() throws Exception {
        CreateDishRequest request = new CreateDishRequest();
        when(userService.getById(any())).thenReturn(mockUser);

        mockMvc.perform(post("/dish/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Pizza")
                        .param("type", "MAIN_COURSE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

        Mockito.verify(dishService).createDish(any(), any());
    }

    @Test

    void testSearchDishRequest_ShouldReturnDishIfFound() throws Exception {
        when(userService.getById(any())).thenReturn(mockUser);
        when(dishService.findUserDishByName("Pizza", mockUser)).thenReturn((Dish) new Object());

        mockMvc.perform(get("/dish/searching")
                        .param("name", "Pizza"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-dish"))
                .andExpect(model().attributeExists("dish", "dishRequest"));
    }

    @Test

    void testUpdateUserDishRequest_ShouldRedirectIfValid() throws Exception {
        EditDishRequest request = new EditDishRequest();
        UUID dishId = UUID.randomUUID();

        mockMvc.perform(patch("/dish/update/" + dishId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Updated Pizza"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

        Mockito.verify(dishService).editDishRequest(any(), any());
    }

    @Test
    void testDeleteUserDishRequest_ShouldRedirect() throws Exception {
        UUID dishId = UUID.randomUUID();

        mockMvc.perform(delete("/dish/delete/" + dishId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

        Mockito.verify(dishService).deleteDish(dishId);
    }*/
}
