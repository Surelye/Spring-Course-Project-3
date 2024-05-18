package ru.borodin.springcourse.FirstRestApp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.borodin.springcourse.FirstRestApp.dto.SensorDTO;
import ru.borodin.springcourse.FirstRestApp.services.SensorsService;

@Component
public class SensorDTOValidator implements Validator {

    private final SensorsService sensorsService;

    @Autowired
    public SensorDTOValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return SensorDTO.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        SensorDTO sensorDTO = (SensorDTO) o;
        if (sensorsService.findByName(sensorDTO.getName()).isPresent()) {
            errors.rejectValue("name", "", "Sensor with name " + sensorDTO.getName() +
                    " already exists");
        }
    }
}
