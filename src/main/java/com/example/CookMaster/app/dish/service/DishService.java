package com.example.CookMaster.app.dish.service;


import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.dish.model.DishType;
import com.example.CookMaster.app.dish.repository.DishRepository;
import com.example.CookMaster.app.exception.DomainException;
import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.ingredient.service.IngredientService;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.web.dto.CreateDishRequest;
import com.example.CookMaster.app.web.dto.EditDishRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishService {

    private final DishRepository mDishRepository;
    private final IngredientService mIngredientService;

    @Autowired
    public DishService(DishRepository mDishRepository, IngredientService mIngredientService) {
        this.mDishRepository = mDishRepository;
        this.mIngredientService = mIngredientService;
    }

    @Transactional
    public Dish createDish(CreateDishRequest request, User user) {

        DishType dishType = request.getDishType();


        Set<Ingredient> ingredients = convertStringsToIngredients(request.getIngredients());
        if (request.getDishName() == null || request.getDishType() == null || request.getDishDescription() == null) {
           throw new DomainException("Invalid create dish request! All fields must be filled!");
        }

        Dish dish = new Dish();
        dish.setUser(user);
        dish.setName(request.getDishName());
        dish.setDescription(request.getDishDescription());
        dish.setType(dishType);
        System.out.println("Ingredient Create" +ingredients);
        dish.setIngredients(ingredients);
        dish.setCreatedAt(LocalDate.now());


        addDishToIngredients(dish, ingredients);
        mDishRepository.save(dish);

        return dish;
    }

    private void addDishToIngredients(Dish dish, Set<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {

            mIngredientService.getmIngredientRepository().save(ingredient);
        }
    }
    private Set<Ingredient> convertStringsToIngredients(Set<String> ingredientNames) {
        Set<Ingredient> ingredientSet = new HashSet<>();

        for (String name : ingredientNames) {
            Optional<Ingredient> ingredientOpt = mIngredientService.getmIngredientRepository().findByName(name);
            Ingredient ingredient = ingredientOpt.orElseGet(() -> {
                Ingredient newIngredient = new Ingredient();
                newIngredient.setName(name);
                return mIngredientService.getmIngredientRepository().save(newIngredient);
            });

            ingredientSet.add(ingredient);
        }

        return ingredientSet;
    }


    public Dish findByName(String name) {
        Dish dish = mDishRepository.findByNameIgnoreCase(name).orElseThrow(() -> new DomainException("Dish not found"));
        return dish;
    }



    public void editDishRequest(UUID id, EditDishRequest request) {
        Dish dish = mDishRepository.findById(id)
                .orElseThrow(() -> new DomainException("Dish not found"));

        dish.setName(request.getDishName());
        dish.setDescription(request.getDishDescription());
        dish.setType(request.getDishType());

        if (request.getIngredients() != null) {
            Set<Ingredient> updatedIngredients = convertStringsToIngredients(request.getIngredients());
            dish.setIngredients(updatedIngredients);
        } else {
            dish.setIngredients(new HashSet<>());
        }

        mDishRepository.save(dish);
    }

    public void deleteDish(UUID id) {

        Dish dish = mDishRepository.findById(id).orElseThrow(() -> new DomainException("Dish not found"));
        mDishRepository.delete(dish);
        log.info("Dish with %s was deleted with (id) %s!".formatted(dish.getName(), dish.getId()));
    }
}


