package com.example.CookMaster.app.schedule;

import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.dish.repository.DishRepository;
import com.example.CookMaster.app.user.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserReportService {

    private final DishRepository dishRepository;

    public UserReportService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void generateUserReport() {

        LocalDate today = LocalDate.now();

        List<Dish> dishesCreatedToday = dishRepository.findByCreatedAtBetween(today, today);

        String report = "📊 User Report - " + LocalDateTime.now() + "\n" +
                "Total Users Who Created Dishes Today: " + dishesCreatedToday.size() + "\n\n";

        for (Dish dish : dishesCreatedToday) {
            User user = dish.getUser();
            report += "👤 " + user.getUsername() + " (" + user.getEmail() + ") - Created Dish: " + dish.getName() + "\n";
        }

        saveReportToFile(report);
        System.out.println("✅ User report generated successfully!");
    }

    private void saveReportToFile(String report) {
        try (FileWriter writer = new FileWriter("user_dish_report.txt")) {
            writer.write(report);
            System.out.println("Report saved to file: user_report.txt");
        } catch (IOException e) {
            System.err.println("Error saving report: " + e.getMessage());
        }
    }
}
