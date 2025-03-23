package com.example.CookMaster.app.calendar.service;

import com.example.CookMaster.app.calendar.model.Menu;
import com.example.CookMaster.app.calendar.repository.MenuRepository;
import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.web.dto.MenuRequest;
import com.example.CookMaster.app.web.mapper.DtoMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class MenuService {
    private final MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Transactional
    public void createMenu(Dish breakfast, Dish lunch, Dish dinner, User user, MenuRequest menuRequest) {
        Menu menu = DtoMapper.mapMenuRequestToMenu(menuRequest);

        menu.setUser(user);
        menu.setBreakfast(breakfast);
        menu.setLunch(lunch);
        menu.setDinner(dinner);
        menu.setDate(LocalDate.now());

        menuRepository.save(menu);

        log.info("Saving menu for %s".formatted(menu.getDayOfWeek().name()));
    }
}
