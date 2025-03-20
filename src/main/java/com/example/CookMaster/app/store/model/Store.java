package com.example.CookMaster.app.store.model;

import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
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

    //
    @OneToMany(mappedBy = "store", fetch = FetchType.EAGER)
    private Set<Ingredient> ingredients;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Store{")
                .append("id=").append(id)
                .append(", name='").append(name).append('\'')
                .append(", address='").append(address).append('\'')
                .append(", phone='").append(phone).append('\'')
                .append(", notes='").append(notes).append('\'')
                .append(", ingredients=[");

        // Логваме всяка съставка поотделно
        if (ingredients != null && !ingredients.isEmpty()) {
            for (Ingredient ingredient : ingredients) {
                builder.append(ingredient.getName()).append(", ");
            }
            builder.delete(builder.length() - 2, builder.length());  // Премахваме последната запетая и интервал
        }
        builder.append("], users=[");

        // Логваме всеки потребител
        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                builder.append(user.getUsername()).append(", ");
            }
            builder.delete(builder.length() - 2, builder.length());  // Премахваме последната запетая и интервал
        }
        builder.append("]}");

        return builder.toString();
    }
}
