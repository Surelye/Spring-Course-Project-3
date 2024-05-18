package ru.borodin.springcourse.FirstRestApp.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class MeasurementDTO {

    @NotNull
    @Range(min = -100, max = 100)
    private Double value;

    @NotNull
    private Boolean raining;

    @NotNull
    private SensorDTO sensor;

    public MeasurementDTO() {}

    public MeasurementDTO(Double value, Boolean raining) {
        this.value = value;
        this.raining = raining;
    }

    public MeasurementDTO(Double value, Boolean raining, SensorDTO sensor) {
        this.value = value;
        this.raining = raining;
        this.sensor = sensor;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
