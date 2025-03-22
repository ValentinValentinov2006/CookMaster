package com.example.CookMaster.app.store.service;

import com.example.CookMaster.app.dish.service.DishService;
import com.example.CookMaster.app.exception.DomainException;
import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.ingredient.service.IngredientService;
import com.example.CookMaster.app.store.model.Store;
import com.example.CookMaster.app.store.repository.StoreRepository;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.service.UserService;
import com.example.CookMaster.app.web.dto.CreateStoreRequest;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class StoreService {
    private static final String REGEX = "^([a-zA-Z]+):([a-zA-Z ,]+)(;([a-zA-Z]+):([a-zA-Z ,]+))*$";




    @Getter
    private final StoreRepository storeRepository;
    private final IngredientService ingredientService;
    private final DishService dishService;


    @Autowired
    public StoreService(StoreRepository storeRepository, IngredientService ingredientService, @Lazy DishService dishService) {
        this.storeRepository = storeRepository;
        this.ingredientService = ingredientService;
        this.dishService = dishService;
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
        log.info("Parsing assignments: {}", assignments);

        String[] assignmentParts = assignments.split(";");
        for (String assignment : assignmentParts) {
            String[] parts = assignment.split(":");
            String storeName = parts[0];
            String[] ingredients = parts[1].split(",");

            log.info("Store: {}", storeName);


            Store store = storeRepository.findByName(storeName);
            if (store == null) {
                store = new Store();
                store.setName(storeName);
            }


            for (String ingredientName : ingredients) {
                ingredientName = ingredientName.trim();  // премахваме излишните интервали
                log.info("Adding ingredient: {}", ingredientName);
                Ingredient ingredient = ingredientService.findIngredientByName(ingredientName);
                if (ingredient == null) {
                    log.warn("Ingredient not found: {}", ingredientName);
                } else {
                    ingredient.setIsBought(true);
                    store.getIngredients().add(ingredient);
                    ingredient.setStore(store);
                }
            }


            storeRepository.saveAndFlush(store);
            log.info("Store after saving: {}", store);
        }
    }

    private Store findStoreByName(String name) {
        return storeRepository.findByName(name);
    }

    public Set<Store> getAllStores() {
        return new HashSet<>(storeRepository.findAll());
    }


    public void deleteBoughtIngredients(String name) {
        Store store = findStoreByName(name);

        if (store == null) {
            throw new DomainException("Store not found with name: " + name);
        }


        for (Ingredient ingredient : store.getIngredients()) {
            ingredient.setStore(null);
            ingredient.setIsBought(false);
        }


        store.getIngredients().clear();
        storeRepository.saveAndFlush(store);
    }


    @Transactional
    public void removeUnlinkedIngredients() {
        Set<Store> allStores = getAllStores();
        for (Store store : allStores) {
            for (Iterator<Ingredient> iterator = store.getIngredients().iterator(); iterator.hasNext();) {
                Ingredient ingredient = iterator.next();
                boolean isUsedInDish = dishService.checkIsIngredientValid(ingredient.getName());
                if (!isUsedInDish) {
                    iterator.remove();
                    ingredient.setStore(null);
                    ingredient.setIsBought(false);
                    log.info("Ingredient {} removed from store {}", ingredient.getName(), store.getName());
                }
            }
            storeRepository.saveAndFlush(store);
        }
    }



}


