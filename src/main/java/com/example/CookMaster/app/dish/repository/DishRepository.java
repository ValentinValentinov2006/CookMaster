package com.example.CookMaster.app.dish.repository;

import com.example.CookMaster.app.dish.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DishRepository extends JpaRepository<Dish, UUID> {


    Optional<Dish> findByNameIgnoreCase(String name);
}
