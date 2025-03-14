package com.example.CookMaster.app.ingredient.model;

import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.store.model.Store;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;
import jakarta.persistence.Entity;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    private String name;



    @ManyToOne(fetch = FetchType.EAGER)
    private Store store;
}
