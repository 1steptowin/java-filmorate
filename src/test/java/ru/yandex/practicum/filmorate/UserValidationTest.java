package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserValidationTest {
    UserController controller;
    @BeforeEach
    void createController() {
        controller = new UserController();
    }

    void validateException (User user) {
        try {
            controller.create(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(),"Не прошла валидация");
            System.out.println("Валидация не прошла");
        }
    }

    @Test
    void validateIncorrectEmail() {
        final User user = new User("123mail.ru","123",LocalDate.of(2020,1,1));
        user.setId(1);
        user.setName("123");
        validateException(user);
    }
    @Test
    void validateBlancEmail() {
        final User user = new User("","123",LocalDate.of(2020,1,1));
        user.setId(1);
        user.setName("123");
        validateException(user);
    }

    @Test
    void validateIncorrectLogin() {
        final User user = new User("123@mail.ru","123 123",LocalDate.of(2020,1,1));
        user.setId(1);
        user.setName("123");
        validateException(user);
    }
    @Test
    void validateBlancLogin() {
        final User user = new User("123@mail.ru","",LocalDate.of(2020,1,1));
        user.setId(1);
        user.setName("123");
        validateException(user);
    }
    @Test
    void validateBlancName() {
        final User user = new User("123@mail.ru","123",LocalDate.of(2020,1,1));
        user.setId(1);
        validateException(user);
        assertEquals(user.getLogin(),user.getName());
    }
    @Test
    void validateBirthDayAfterNow() {
        final User user = new User("123@mail.ru","123",LocalDate.of(2024,1,1));
        user.setId(1);
        validateException(user);
    }

}
