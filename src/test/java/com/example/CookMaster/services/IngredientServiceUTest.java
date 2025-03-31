package com.example.CookMaster.services;

import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.ingredient.repository.IngredientRepository;
import com.example.CookMaster.app.ingredient.service.IngredientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IngredientServiceUTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        ingredient = new Ingredient();
        UUID id = UUID.randomUUID();
        ingredient.setId(id);
        ingredient.setName("Tomato");

    }

    @Test
    void testSaveIngredient() {

        ingredientService.saveIngredient(ingredient);


        verify(ingredientRepository, times(1)).save(ingredient);
    }

    @Test
    void testSaveIngredientAndReturn() {

        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);


        Ingredient result = ingredientService.saveIngredientAndReturn(ingredient);


        verify(ingredientRepository, times(1)).save(ingredient);
        assertEquals(ingredient, result);
    }

    @Test
    void testFindIngredientByName_IngredientExists() {

        when(ingredientRepository.getIngredientByName("Tomato")).thenReturn(ingredient);


        Ingredient result = ingredientService.findIngredientByName("Tomato");


        verify(ingredientRepository, times(1)).getIngredientByName("Tomato");


        assertEquals(ingredient, result);
    }

    @Test
    void testFindIngredientByName_IngredientNotFound() {

        when(ingredientRepository.getIngredientByName("NonExistentIngredient")).thenReturn(null);


        Ingredient result = ingredientService.findIngredientByName("NonExistentIngredient");


        verify(ingredientRepository, times(1)).getIngredientByName("NonExistentIngredient");


        assertEquals(null, result);
    }
}
