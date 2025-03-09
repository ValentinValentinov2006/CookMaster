package com.example.CookMaster.app.calendar.model;

import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;


    private String dayOfWeek;


    private String time;
}
