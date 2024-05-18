package ru.borodin.springcourse.FirstRestApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.borodin.springcourse.FirstRestApp.dto.SensorDTO;
import ru.borodin.springcourse.FirstRestApp.models.Sensor;
import ru.borodin.springcourse.FirstRestApp.services.SensorsService;
import ru.borodin.springcourse.FirstRestApp.util.ErrorResponse;
import ru.borodin.springcourse.FirstRestApp.util.SensorNotAddedException;
import ru.borodin.springcourse.FirstRestApp.util.SensorDTOValidator;

import javax.validation.Valid;

@RestController
@RequestMapping("/sensors")
public class SensorsController {

    private final SensorsService sensorsService;
    private final ModelMapper modelMapper;
    private final SensorDTOValidator sensorValidator;

    @Autowired
    public SensorsController(SensorsService sensorsService, ModelMapper modelMapper, SensorDTOValidator sensorDTOValidator) {
        this.sensorsService = sensorsService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorDTOValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> addSensor(@Valid @RequestBody SensorDTO sensorDTO,
                                                BindingResult bindingResult) {
        sensorValidator.validate(sensorDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessage.append(error.getField()).append(": ")
                        .append(error.getDefaultMessage()).append(";");
            }
            throw new SensorNotAddedException(errorMessage.toString());
        }
        sensorsService.save(convertToSensor(sensorDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @ExceptionHandler({SensorNotAddedException.class})
    private ResponseEntity<ErrorResponse> handleException(SensorNotAddedException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }
}
