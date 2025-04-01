package com.example.CookMaster.app.dish.repository;

import com.example.CookMaster.app.dish.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DishRepository extends JpaRepository<Dish, UUID> {


    List<Dish> findByCreatedAtBetween(LocalDate start, LocalDate end);
}
