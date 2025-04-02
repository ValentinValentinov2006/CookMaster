package com.example.CookMaster.services;

import com.example.CookMaster.app.dish.service.DishService;
import com.example.CookMaster.app.exception.DomainException;
import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.ingredient.service.IngredientService;
import com.example.CookMaster.app.store.model.Store;
import com.example.CookMaster.app.store.repository.StoreRepository;
import com.example.CookMaster.app.store.service.StoreService;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.web.dto.CreateStoreRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceUTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private DishService dishService;

    @InjectMocks
    private StoreService storeService;

    private User mockUser;
    private Store mockStore;
    private Ingredient mockIngredient;
    private static final String VALID_ASSIGNMENT = "Walmart:Tomato,Potato;Target:Carrot";

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setStores(new HashSet<>());

        mockStore = new Store();
        mockStore.setName("Test Store");
        mockStore.setIngredients(new HashSet<>());

        mockIngredient = new Ingredient();
        mockIngredient.setName("Test Ingredient");
    }

    @Test
    void testCreateStore_ShouldCreateStoreSuccessfully() {
        CreateStoreRequest request = new CreateStoreRequest("Bila", "", "0884323640", "Notes");

        when(storeRepository.save(any(Store.class))).thenReturn(mockStore);

        storeService.createStore(request, mockUser);

        assertTrue(mockUser.getStores().stream().anyMatch(s -> s.getName().equals("Bila")));
        verify(storeRepository, times(1)).save(any(Store.class));
    }

   /* @Test
    void testUpdateStoresAndIngredients_ShouldUpdateSuccessfully() {
        Store realStore = new Store();
        realStore.setIngredients(new HashSet<>());

        Ingredient realIngredient = new Ingredient();
        realIngredient.setIsBought(false);

        when(storeRepository.findByName("Tarator")).thenReturn(realStore);
        when(ingredientService.findIngredientByName("Salt")).thenReturn(realIngredient);

        storeService.updateStoresAndIngredients("Tarator:Salt");
        assertTrue(realStore.getIngredients().contains(realIngredient), "Store should contain the ingredient");
        assertTrue(realIngredient.getIsBought(), "Ingredient should be marked as bought");

        verify(storeRepository, times(1)).saveAndFlush(any(Store.class));
    }*/

    @Test
    void testDeleteBoughtIngredients_ShouldClearIngredients() {
        mockStore.getIngredients().add(mockIngredient);
        when(storeRepository.findByName("Test Store")).thenReturn(mockStore);

        storeService.deleteBoughtIngredients("Test Store");

        assertTrue(mockStore.getIngredients().isEmpty());
        verify(storeRepository, times(1)).saveAndFlush(mockStore);
    }

    @Test
    void testDeleteBoughtIngredients_ShouldThrowExceptionWhenStoreNotFound() {
        when(storeRepository.findByName("NonExistent Store")).thenReturn(null);
        assertThrows(DomainException.class, () -> storeService.deleteBoughtIngredients("NonExistent Store"));
    }

    @Test
    void testRemoveUnlinkedIngredients_ShouldRemoveUnusedIngredients() {
        mockStore.getIngredients().add(mockIngredient);
        when(storeRepository.findAll()).thenReturn(Collections.singletonList(mockStore));
        when(dishService.checkIsIngredientValid(anyString())).thenReturn(false);

        storeService.removeUnlinkedIngredients();

        assertTrue(mockStore.getIngredients().isEmpty());
        verify(storeRepository, times(1)).saveAndFlush(mockStore);
    }


    @Test
    void testUpdateStoresAndIngredients_InvalidAssignments() {
        String invalidAssignment = "InvalidFormat";

        storeService.updateStoresAndIngredients(invalidAssignment);

        verify(storeRepository, never()).saveAndFlush(any(Store.class));
    }

    @Test
    void testUpdateStoresAndIngredients_MissingIngredient() {
        Store walmart = new Store();
        walmart.setName("Walmart");

        Ingredient missingIngredient = null;

        when(storeRepository.findByName("Walmart")).thenReturn(walmart);
        when(ingredientService.findIngredientByName("Unknown")).thenReturn(missingIngredient);

        storeService.updateStoresAndIngredients("Walmart:Unknown");

        verify(storeRepository).saveAndFlush(any(Store.class));
    }
}
