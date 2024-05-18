package ru.borodin.springcourse.FirstRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.borodin.springcourse.FirstRestApp.models.Measurement;
import ru.borodin.springcourse.FirstRestApp.models.Sensor;
import ru.borodin.springcourse.FirstRestApp.repositories.MeasurementsRepository;
import ru.borodin.springcourse.FirstRestApp.util.MeasurementNotAddedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MeasurementsService {

    private final MeasurementsRepository measurementsRepository;
    private final SensorsService sensorsService;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository, SensorsService sensorsService) {
        this.measurementsRepository = measurementsRepository;
        this.sensorsService = sensorsService;
    }

    @Transactional(readOnly = true)
    public List<Measurement> findAll() {
        return measurementsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public int countAllByRainingTrue() {
        return measurementsRepository.countAllByRainingTrue();
    }

    @Transactional
    public void save(Measurement measurement) {
        Optional<Sensor> sensor = sensorsService.findByName(measurement.getSensor().getName());
        if (sensor.isPresent()) {
            measurement.setSensor(sensor.get());
            measurement.setMeasuredAt(LocalDateTime.now());
            measurementsRepository.save(measurement);
            return;
        }
        throw new MeasurementNotAddedException("Sensor not found");
    }
}
