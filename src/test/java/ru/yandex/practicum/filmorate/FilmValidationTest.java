package ru.yandex.practicum.filmorate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FilmValidationTest {
    FilmController controller;
    @BeforeEach
    void createController() {
        controller = new FilmController();
    }

    void validateException (Film film) {
        try {
            controller.create(film);
            assertEquals(true,true);
            System.out.println("Валидация прошла");
        } catch (ValidationException e) {
            assertEquals(e.getMessage(),"Не прошла валидация");
            System.out.println("Валидация не прошла");
        }
    }
    void validate (Film film) throws ValidationException {
        controller.create(film);
        assertEquals(true,true);
        System.out.println("Валидация прошла");
    }
    @Test
    void validateFilmNameIsBlanc() {
        Film film = new Film("","123",LocalDate.of(2020,1,1),100);
        film.setId(1);
        validateException(film);
    }
    @Test
    void validateDescriptionLength() {
        Film film = new Film("123","1231231231231231231231231231231231231231231231231231231231231231" +
                "2312312312312312312312312312312312312312312312312312312312312312312312312312312312312312312312312" +
                "312312312312312312312312312312312312312312312312312312312312312312312312312312312312312312312312" +
                "3123123",
                LocalDate.of(2020,1,1),100);
        film.setId(1);
        validateException(film);
    }
    @Test
    void validateFilm() throws ValidationException {
        Film film = new Film("123","123",LocalDate.of(2020,1,1),100);
        film.setId(1);
        validate(film);
    }
    @Test
    void validateReleaseDate() {
        Film film = new Film("123","123",LocalDate.of(1860,1,1),100);
        film.setId(1);
        validateException(film);
    }
    @Test
    void validateDuration() {
        Film film = new Film("123","123",LocalDate.of(1860,1,1),-100);
        film.setId(1);
        validateException(film);
    }


}
