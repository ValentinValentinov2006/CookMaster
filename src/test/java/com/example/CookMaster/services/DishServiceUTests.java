package com.example.CookMaster.services;

import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.dish.model.DishType;
import com.example.CookMaster.app.dish.repository.DishRepository;
import com.example.CookMaster.app.dish.service.DishService;
import com.example.CookMaster.app.exception.CreateDishException;
import com.example.CookMaster.app.exception.DishDoesNotExistsException;
import com.example.CookMaster.app.exception.DomainException;
import com.example.CookMaster.app.exception.NotHaveEnoughDishes;
import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.ingredient.repository.IngredientRepository;
import com.example.CookMaster.app.ingredient.service.IngredientService;
import com.example.CookMaster.app.store.service.StoreService;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.service.UserService;
import com.example.CookMaster.app.web.dto.CreateDishRequest;
import com.example.CookMaster.app.web.dto.EditDishRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;


public class DishServiceUTests {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private StoreService storeService;

    @Mock
    private UserService userService;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private DishService dishService;

    private User mockUser;
    private Dish mockDish;
    private Ingredient mockIngredient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockDish = new Dish();
        mockDish.setId(UUID.randomUUID());
        mockDish.setUser(mockUser);
        mockIngredient = new Ingredient();
        mockIngredient.setName("Test Ingredient");
        when(ingredientService.getmIngredientRepository()).thenReturn(ingredientRepository);
    }



    @Test
    public void testCreateDish_ShouldThrowExceptionWhenFieldsAreMissing() {

        CreateDishRequest request = CreateDishRequest.builder()
                .dishName(null)
                .dishDescription("Description")
                .dishType(DishType.LUNCH)
                .ingredients(new HashSet<>())
                .build();

        assertThrows(CreateDishException.class, () -> dishService.createDish(request, mockUser));
    }

    @Test
    public void testFindUserDishByName_ShouldReturnDish() {

        Set<Dish> dishes = new HashSet<>();
        mockDish.setName("Tarator");
        dishes.add(mockDish);
        mockUser.setDishes(dishes);

        when(dishRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockDish));
        Dish foundDish = dishService.findUserDishByName("Tarator", mockUser);
        assertEquals(mockDish, foundDish);
    }

    @Test
    public void testFindUserDishByName_ShouldThrowExceptionWhenDishNotFound() {
        String dishName = "Tarartorrr";


        mockUser.setDishes(new HashSet<>());

        when(dishRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(DishDoesNotExistsException.class, () -> dishService.findUserDishByName(dishName, mockUser));
    }
    @Test
    public void testEditDishRequest_ShouldUpdateDish() {

        EditDishRequest request = new EditDishRequest("Updated Dish",  DishType.BREAKFAST, "Updated Description",null);
        when(dishRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockDish));

        dishService.editDishRequest(mockDish.getId(), request);

        verify(dishRepository, times(1)).save(mockDish);
    }



    @Test
    public void testConvertDays_ShouldConvertCorrectly() {
        String result = dishService.convertDays("Mon");
        assertEquals("Monday", result);
    }

    @Test
    public void testConvertDays_ShouldReturnUnknownWhenInvalid() {
        String result = dishService.convertDays("InvalidDay");
        assertEquals("Unknown Day", result);
    }

    @Test
    public void testCheckIsIngredientValid_ShouldReturnFalseIfIngredientDoesNotExist() {
        when(dishRepository.findAll()).thenReturn(Collections.emptyList());
        boolean result = dishService.checkIsIngredientValid("Nonexistent Ingredient");
        assertFalse(result);
    }



    @Test
    public void testCheckIsIngredientValid_ShouldReturnTrueIfIngredientExists() {
        Dish dishWithIngredient = new Dish();
        dishWithIngredient.setIngredients(new HashSet<>(Collections.singletonList(mockIngredient)));
        when(dishRepository.findAll()).thenReturn(Collections.singletonList(dishWithIngredient));

        boolean result = dishService.checkIsIngredientValid("Test Ingredient");

        assertTrue(result);
    }


    @Test
    public void testGetBreakfastDishes_ShouldReturnBreakfastDishes() {
        Dish breakfastDish = new Dish();
        breakfastDish.setType(DishType.BREAKFAST);
        Set<Dish> userDishes = new HashSet<>(Collections.singletonList(breakfastDish));

        List<Dish> result = dishService.getBreakfastDishes(userDishes);

        assertEquals(1, result.size());
        assertEquals(DishType.BREAKFAST, result.get(0).getType());
    }

    @Test
    public void testGetLunchDishes_ShouldReturnLunchDishes() {
        Dish lunchDish = new Dish();
        lunchDish.setType(DishType.LUNCH);
        Set<Dish> userDishes = new HashSet<>(Collections.singletonList(lunchDish));

        List<Dish> result = dishService.getLunchDishes(userDishes);

        assertEquals(1, result.size());
        assertEquals(DishType.LUNCH, result.get(0).getType());
    }

    @Test
    public void testGetDinnerDishes_ShouldReturnDinnerDishes() {
        Dish dinnerDish = new Dish();
        dinnerDish.setType(DishType.DINNER);
        Set<Dish> userDishes = new HashSet<>(Collections.singletonList(dinnerDish));

        List<Dish> result = dishService.getDinnerDishes(userDishes);

        assertEquals(1, result.size());
        assertEquals(DishType.DINNER, result.get(0).getType());
    }

    @Test
    public void testFindDishById_ShouldReturnDish() {
        when(dishRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockDish));
        Dish foundDish = dishService.findDishNyId(mockDish.getId());
        assertEquals(mockDish, foundDish);
    }

    @Test
    public void testFindDishById_ShouldThrowExceptionWhenDishNotFound() {
        when(dishRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> dishService.findDishNyId(mockDish.getId()));
    }
    @Test
    public void testCreateDish_ShouldCreateDishSuccessfully() {

        Set<String> ingredientNames = new HashSet<>(Arrays.asList("Tomato", "Cheese"));
        CreateDishRequest request = CreateDishRequest.builder()
                .dishName("Cheese Pizza")
                .dishDescription("A tasty cheese pizza")
                .dishType(DishType.LUNCH)
                .ingredients(ingredientNames)
                .build();


        when(ingredientService.getmIngredientRepository().findByName("Tomato")).thenReturn(Optional.of(mockIngredient));
        when(ingredientService.getmIngredientRepository().findByName("Cheese")).thenReturn(Optional.of(mockIngredient));


        dishService.createDish(request, mockUser);


        verify(dishRepository, times(1)).save(any(Dish.class));
    }

    @Test
    public void testCreateDish_ShouldThrowExceptionWhenMissingDishName() {

        CreateDishRequest request = CreateDishRequest.builder()
                .dishName(null)
                .dishDescription("Valid description")
                .dishType(DishType.BREAKFAST)
                .ingredients(new HashSet<>())
                .build();

        assertThrows(CreateDishException.class, () -> dishService.createDish(request, mockUser));
    }

    @Test
    public void testEditDishRequest_ShouldRemoveIngredientsIfNoneAreProvided() {

        EditDishRequest request = new EditDishRequest("Updated Dish", DishType.DINNER, "Updated Description", new HashSet<>());
        when(dishRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockDish));


        dishService.editDishRequest(mockDish.getId(), request);

        assertTrue(mockDish.getIngredients().isEmpty());
        verify(dishRepository, times(1)).save(mockDish);
    }

    @Test
    public void testCreateDish_ShouldHandleNewIngredients() {

        Set<String> ingredientNames = new HashSet<>(Arrays.asList("Tomato", "Cheese"));
        CreateDishRequest request = CreateDishRequest.builder()
                .dishName("Cheese Pizza")
                .dishDescription("A tasty cheese pizza")
                .dishType(DishType.LUNCH)
                .ingredients(ingredientNames)
                .build();


        when(ingredientService.getmIngredientRepository().findByName("Tomato")).thenReturn(Optional.empty());
        when(ingredientService.getmIngredientRepository().findByName("Cheese")).thenReturn(Optional.empty());
        when(ingredientService.saveIngredientAndReturn(any(Ingredient.class))).thenReturn(mockIngredient);


        dishService.createDish(request, mockUser);

        verify(ingredientService, times(2)).saveIngredientAndReturn(any(Ingredient.class));
        verify(dishRepository, times(1)).save(any(Dish.class));
    }

    @Test
    void testMissingBreakfastDish() {

        Dish lunchDish = new Dish();
        lunchDish.setType(DishType.LUNCH);
        Dish dinnerDish = new Dish();
        dinnerDish.setType(DishType.DINNER);

        Set<Dish> dishes = new HashSet<>();
        dishes.add(lunchDish);
        dishes.add(dinnerDish);

        mockUser.setDishes(dishes);


        NotHaveEnoughDishes exception = assertThrows(NotHaveEnoughDishes.class, () -> dishService.checkIfYouHaveThreeDifferentDishes(mockUser));
        assertEquals("You are missing some dish types!", exception.getMessage());
    }

    @Test
    void testMissingLunchDish() {

        Dish breakfastDish = new Dish();
        breakfastDish.setType(DishType.BREAKFAST);
        Dish dinnerDish = new Dish();
        dinnerDish.setType(DishType.DINNER);

        Set<Dish> dishes = new HashSet<>();
        dishes.add(breakfastDish);
        dishes.add(dinnerDish);

        mockUser.setDishes(dishes);


        NotHaveEnoughDishes exception = assertThrows(NotHaveEnoughDishes.class, () -> dishService.checkIfYouHaveThreeDifferentDishes(mockUser));
        assertEquals("You are missing some dish types!", exception.getMessage());
    }

    @Test
    void testMissingDinnerDish() {

        Dish breakfastDish = new Dish();
        breakfastDish.setType(DishType.BREAKFAST);
        Dish lunchDish = new Dish();
        lunchDish.setType(DishType.LUNCH);

        Set<Dish> dishes = new HashSet<>();
        dishes.add(breakfastDish);
        dishes.add(lunchDish);

        mockUser.setDishes(dishes);


        NotHaveEnoughDishes exception = assertThrows(NotHaveEnoughDishes.class, () -> dishService.checkIfYouHaveThreeDifferentDishes(mockUser));
        assertEquals("You are missing some dish types!", exception.getMessage());
    }

    @Test
    void testNotEnoughDishes() {

        Dish otherDish = new Dish();
        otherDish.setType(DishType.LUNCH);

        Set<Dish> dishes = new HashSet<>();
        dishes.add(otherDish);

        mockUser.setDishes(dishes);


        NotHaveEnoughDishes exception = assertThrows(NotHaveEnoughDishes.class, () -> dishService.checkIfYouHaveThreeDifferentDishes(mockUser));
        assertEquals("You are missing some dish types!", exception.getMessage());
    }


}

