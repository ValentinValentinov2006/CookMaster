package com.example.CookMaster.app.calendar.service;

import com.example.CookMaster.app.calendar.model.DayOfWeek;
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

        Menu isMenuExist =  findMenuByDay(menuRequest.getDayOfWeek());
        if (isMenuExist != null) {
            isMenuExist.setBreakfast(breakfast);
            isMenuExist.setLunch(lunch);
            isMenuExist.setDinner(dinner);
            isMenuExist.setDate(LocalDate.now());
            menuRepository.save(isMenuExist);
            return ;
        }

        menu.setUser(user);
        menu.setBreakfast(breakfast);
        menu.setLunch(lunch);
        menu.setDinner(dinner);
        menu.setDate(LocalDate.now());

        menuRepository.save(menu);

        log.info("Saving menu for %s".formatted(menu.getDayOfWeek().name()));
    }

    public Menu findMenuByDay(String day) {
        return menuRepository.findByDayOfWeek(DayOfWeek.valueOf(day.toUpperCase()));
    }

    @Transactional
    public void removeDishFromMenu(Dish dish) {

        var menus = menuRepository.findAll();

        for (Menu menu : menus) {
            boolean updated = false;

            if (menu.getBreakfast() != null && menu.getBreakfast().equals(dish)) {
                menu.setBreakfast(null);
                updated = true;
            }
            if (menu.getLunch() != null && menu.getLunch().equals(dish)) {
                menu.setLunch(null);
                updated = true;
            }
            if (menu.getDinner() != null && menu.getDinner().equals(dish)) {
                menu.setDinner(null);
                updated = true;
            }

            if (updated) {
                menuRepository.save(menu);
            }
        }
    }
}
