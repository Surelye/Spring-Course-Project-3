package ru.borodin.springcourse.FirstRestApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.borodin.springcourse.FirstRestApp.dto.MeasurementDTO;
import ru.borodin.springcourse.FirstRestApp.models.Measurement;
import ru.borodin.springcourse.FirstRestApp.services.MeasurementsService;
import ru.borodin.springcourse.FirstRestApp.util.ErrorResponse;
import ru.borodin.springcourse.FirstRestApp.util.MeasurementNotAddedException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementsService measurementsService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementsController(MeasurementsService measurementsService, ModelMapper modelMapper) {
        this.measurementsService = measurementsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<MeasurementDTO> index() {
        return measurementsService.findAll()
                .stream()
                .map(this::convertToMeasurementDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/rainyDaysCount")
    public Map<String, Integer> rainyDaysCount() {
        return Map.of(
                "rainyDaysCount",
                measurementsService.countAllByRainingTrue()
        );
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@Valid @RequestBody MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.append(error.getField()).append(": ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new MeasurementNotAddedException(errors.toString());
        }
        Measurement measurement = convertToMeasurement(measurementDTO);
        measurementsService.save(measurement);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @ExceptionHandler(MeasurementNotAddedException.class)
    public ResponseEntity<ErrorResponse> handleException(MeasurementNotAddedException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    public MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}
