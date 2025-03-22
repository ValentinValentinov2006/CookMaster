package com.example.CookMaster.app.ingredient.service;


import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.ingredient.repository.IngredientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
@Slf4j
public class IngredientService {

    private final IngredientRepository ingredientRepository;


    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;

    }


    public IngredientRepository getmIngredientRepository() {
        return ingredientRepository;
    }

    public void saveIngredient(Ingredient ingredient){
        ingredientRepository.save(ingredient);
    }
    public Ingredient saveIngredientAndReturn(Ingredient ingredient){
        return ingredientRepository.save(ingredient);
    }
    public Ingredient findIngredientByName(String name){
        return ingredientRepository.getIngredientByName(name);
    }

    // f @Transactional
    // f public void removeIngredientFromDish(UUID dishId, String ingredientName) {
    //      log.info("Entering removeIngredientFromDish with dishId: {} and ingredientName: {}", dishId, ingredientName);
    //      Optional<Ingredient> ingredientOpt = ingredientRepository.findByName(ingredientName);
    //      if (ingredientOpt.isEmpty()) {
    //          throw new DomainException("Ingredient not found: " + ingredientName);
    //      }
    //
    //      Ingredient ingredient = ingredientOpt.get();
    //      Dish dish = dishService.findDishNyId(dishId);
    //
    //
    //      boolean removed = dish.getIngredients().remove(ingredient);
    //
    //      if (!removed) {
    //          log.warn("Ingredient {} was not found in dish {}", ingredientName, dish.getName());
    //          return;
    //      }
    //
    //
    //      dishService.getMDishRepository().save(dish);
    //
    //      log.info("Successfully removed ingredient {} from dish {}", ingredientName, dish.getName());
    //
    //
    //          Store store = ingredient.getStore();
    //          log.info("Ingredient %s with Store %s".formatted(ingredientName, store.getName()));
    //
    //
    //              store.getIngredients().remove(ingredient);
    //              ingredient.setStore(null);
    //              storeService.getStoreRepository().saveAndFlush(store);
    //              ingredientRepository.saveAndFlush(ingredient);
    //
    //              log.info("Ingredient {} removed from store {}", ingredientName, store.getName());
    //
    //  }
}
