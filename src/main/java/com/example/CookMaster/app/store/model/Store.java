package com.example.CookMaster.app.store.model;

import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String address;

    private String phone;

    private String notes;

    @ManyToMany(mappedBy = "stores")
    private Set<User> users;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private Set<Ingredient> ingredients;
}
