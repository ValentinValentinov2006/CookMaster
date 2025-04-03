package com.example.CookMaster.app.dish.service;


import com.example.CookMaster.app.calendar.service.MenuService;
import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.dish.model.DishType;
import com.example.CookMaster.app.dish.repository.DishRepository;
import com.example.CookMaster.app.exception.CreateDishException;
import com.example.CookMaster.app.exception.DishDoesNotExistsException;
import com.example.CookMaster.app.exception.DomainException;
import com.example.CookMaster.app.exception.NotHaveEnoughDishes;
import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.ingredient.service.IngredientService;
import com.example.CookMaster.app.store.service.StoreService;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.service.UserService;
import com.example.CookMaster.app.web.dto.CreateDishRequest;
import com.example.CookMaster.app.web.dto.EditDishRequest;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@Getter
public class DishService {

    private final DishRepository mDishRepository;
    private final IngredientService mIngredientService;
    private final StoreService mStoreService;
    private final UserService userService;
    private final MenuService menuService;

    @Autowired
    public DishService(DishRepository mDishRepository, @Lazy IngredientService mIngredientService, StoreService mStoreService, UserService userService, MenuService menuService) {
        this.mDishRepository = mDishRepository;
        this.mIngredientService = mIngredientService;
        this.mStoreService = mStoreService;
        this.userService = userService;
        this.menuService = menuService;
    }

    @Transactional
    public void createDish(CreateDishRequest request, User user) {

        DishType dishType = request.getDishType();


        Set<Ingredient> ingredients = convertStringsToIngredients(request.getIngredients());
        if (request.getDishName() == null || request.getDishType() == null || request.getDishDescription() == null) {
           throw new CreateDishException("Invalid create dish request! All fields must be filled!");
        }

        Dish dish = new Dish();
        dish.setUser(user);
        dish.setName(request.getDishName());
        dish.setDescription(request.getDishDescription());
        dish.setType(dishType);
        System.out.println("Ingredient Create" +ingredients);
        dish.setIngredients(ingredients);
        dish.setCreatedAt(LocalDate.now());


        addDishToIngredients(ingredients);
        mDishRepository.save(dish);


    }



    private void addDishToIngredients(Set<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {

            mIngredientService.saveIngredient(ingredient);
        }
    }
    public Set<Ingredient> convertStringsToIngredients(Set<String> ingredientNames) {
        Set<Ingredient> ingredientSet = new HashSet<>();

        for (String name : ingredientNames) {
            Optional<Ingredient> ingredientOpt = mIngredientService.getmIngredientRepository().findByName(name);
            Ingredient ingredient = ingredientOpt.orElseGet(() -> {
                Ingredient newIngredient = new Ingredient();
                newIngredient.setName(name);
                newIngredient.setIsBought(false);
                return mIngredientService.saveIngredientAndReturn(newIngredient);
            });

            ingredientSet.add(ingredient);
        }

        return ingredientSet;
    }




    public Dish findUserDishByName(String name, User user) {
        // !!!!
        if (name == null) {
            throw new IllegalArgumentException("Dish name cannot be null");
        }
        return user.getDishes().stream()
                .filter(dish -> dish.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new DishDoesNotExistsException("Dish not found for this user"));
    }


    public void editDishRequest(UUID id, EditDishRequest request) {
        Dish dish = findDishNyId(id);

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

        Dish dish = mDishRepository.findById(id)
                .orElseThrow(() -> new DomainException("Dish not found"));


        menuService.removeDishFromMenu(dish);


        mDishRepository.delete(dish);

        log.info("Dish {} was deleted with ID {}", dish.getName(), dish.getId());
    }

    public Dish findDishNyId(UUID dishId) {
        return mDishRepository.findById(dishId).orElseThrow(() -> new DomainException("Dish not found"));
    }

    public boolean checkIsIngredientValid(String ingredientName) {

        Set<Dish> dishes = new HashSet<>(mDishRepository.findAll());

        for (Dish dish : dishes) {
            boolean ingredientFound = dish.getIngredients().stream()
                    .anyMatch(ingredient -> ingredient.getName().equals(ingredientName));

            if (ingredientFound) {
                return true;
            }
        }

        return false;
    }

    public  List<Dish> getBreakfastDishes( Set<Dish> userDishes) {
        return takeAllDishesByTypeDish(DishType.BREAKFAST, userDishes);
    }
    public  List<Dish> getLunchDishes( Set<Dish> userDishes) {

        return takeAllDishesByTypeDish(DishType.LUNCH, userDishes);
    }
    public List<Dish> getDinnerDishes(Set<Dish> userDishes) {
        return takeAllDishesByTypeDish(DishType.DINNER, userDishes);
    }

    private List<Dish> takeAllDishesByTypeDish(DishType dishType, Set<Dish> userDishes) {
        return userDishes.stream()
                .filter(dish -> dish.getType().equals(dishType))
                .collect(Collectors.toList());
    }

    public String convertDays(String dayName) {
        if (dayName == null) {
            throw new IllegalArgumentException("Day name cannot be null");
        }
        Map<String, String> daysMap = Map.of(
                "Sun", "Sunday",
                "Mon", "Monday",
                "Tue", "Tuesday",
                "Wed", "Wednesday",
                "Thu", "Thursday",
                "Fri", "Friday",
                "Sat", "Saturday"
        );

        return daysMap.getOrDefault(dayName, "Unknown Day");
    }


    public void checkIfYouHaveThreeDifferentDishes(User user) {
        Set<Dish> dishes = user.getDishes();
    boolean hasBreakfast = dishes.stream().anyMatch(dish -> dish.getType() == DishType.BREAKFAST);
    boolean hasLunch = dishes.stream().anyMatch(dish -> dish.getType() == DishType.LUNCH);
    boolean hasDinner = dishes.stream().anyMatch(dish -> dish.getType() == DishType.DINNER);

        if (hasBreakfast && hasLunch && hasDinner) {
        log.info("You have dishes for breakfast, lunch, and dinner!");
    } else {
        throw new NotHaveEnoughDishes("You are missing some dish types!");
    }
}
}


