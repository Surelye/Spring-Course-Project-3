package ru.borodin.springcourse.FirstRestApp.util;

public class SensorNotAddedException extends RuntimeException {

    public SensorNotAddedException(String message) {
        super(message);
    }
}
