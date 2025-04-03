package com.example.CookMaster.app.dish.model;

import com.example.CookMaster.app.ingredient.model.Ingredient;

import com.example.CookMaster.app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    private String name;


    private String description;


    @Enumerated(EnumType.STRING)
    private DishType type;


    private LocalDate createdAt;


    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "dish_ingredient",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> ingredients;

    public Dish(String chooseOption, String dinner) {
        this.name = chooseOption;
        this.description = chooseOption;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }


}
