package com.example.CookMaster.app.shopping.model;

import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.user.model.User;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Shopping {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "shopping")
    private List<Dish> dish;

    @ManyToOne
    private User user;

    private Boolean isBought;


    private LocalDate createdAt;


}
