package com.example.CookMaster.app.store.service;

import com.example.CookMaster.app.exception.DomainException;
import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.ingredient.service.IngredientService;
import com.example.CookMaster.app.store.model.Store;
import com.example.CookMaster.app.store.repository.StoreRepository;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.web.dto.CreateStoreRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class StoreService {
    private static final String REGEX = "^([a-zA-Z]+):([a-zA-Z,]+)(;([a-zA-Z]+):([a-zA-Z,]+))?$";
    private final StoreRepository storeRepository;
    private final IngredientService ingredientService;

    @Autowired
    public StoreService(StoreRepository storeRepository, IngredientService ingredientService) {
        this.storeRepository = storeRepository;
        this.ingredientService = ingredientService;
    }


    public void createStore(CreateStoreRequest createStoreRequest, User user) {


        Store store = initializeStore(createStoreRequest);
        store.getUsers().add(user);
        user.getStores().add(store);
        log.info("User is connected with Store[%s]: ".formatted(store));
        storeRepository.save(store);
        log.info("Store with %s is created".formatted(createStoreRequest.getStoreName()));

    }

    private static Store initializeStore(CreateStoreRequest createStoreRequest) {
        return Store.builder()
                 .name(createStoreRequest.getStoreName())
                 .address(createStoreRequest.getAddress())
                 .phone(createStoreRequest.getPhoneNumber())
                 .notes(createStoreRequest.getNotes())
                .ingredients(new HashSet<>())
                .users(new HashSet<>())
                .build();
    }
    @Transactional
    public void updateStoresAndIngredients(String assignments) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(assignments);
        if (matcher.find()) {
            parseAssignments(assignments);
        } else {
            log.error("Invalid assignments format [%s]".formatted(assignments));
            
        }
        log.info("%s".formatted(assignments));
    }


    private void parseAssignments(String assignments) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(assignments);

        while (matcher.find()) {
            String storeName = matcher.group(1);
            String[] ingredientsArray = matcher.group(2).split(",");

            Store store = storeRepository.findByName(storeName);



            for (String ingredientName : ingredientsArray) {
                Ingredient ingredient = ingredientService.getmIngredientRepository().getIngredientByName(ingredientName);


                store.getIngredients().add(ingredient);
                ingredient.setStore(store);
            }



            storeRepository.saveAndFlush(store);

            log.info("Store after saving: {}", store);

        }
    }

    public Set<Store> getAllStores() {
        return new HashSet<>(storeRepository.findAll());
    }


    public void deleteBoughtIngredients(String name) {
        Store store = storeRepository.findByName(name);

        if (store == null) {
            throw new DomainException("Store not found with name: " + name);
        }

        for (Ingredient ingredient : store.getIngredients()) {
            ingredient.setStore(null);
        }


        store.getIngredients().clear();
        storeRepository.saveAndFlush(store);
    }
}
