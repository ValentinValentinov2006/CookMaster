package com.example.CookMaster.app.calendar.model;

import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "breakfast_id")
    private Dish breakfast;

    @ManyToOne
    @JoinColumn(name = "lunch_id")
    private Dish lunch;

    @ManyToOne
    @JoinColumn(name = "dinner_id")
    private Dish dinner;

    private LocalDate date;
}
