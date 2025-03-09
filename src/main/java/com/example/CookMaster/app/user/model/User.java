package com.example.CookMaster.app.user.model;

import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.shopping.model.Shopping;
import com.example.CookMaster.app.store.model.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    private String firstName;

    private String lastName;

    private Boolean isActive;

    private String url;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private LocalDate updatedAt;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Dish> dishes;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Shopping> shopping;

    @ManyToMany
    @JoinTable(
            name = "users_stores",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    private Set<Store> stores;

}
