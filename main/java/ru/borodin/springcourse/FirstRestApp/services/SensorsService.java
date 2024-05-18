package ru.borodin.springcourse.FirstRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.borodin.springcourse.FirstRestApp.models.Sensor;
import ru.borodin.springcourse.FirstRestApp.repositories.SensorsRepository;

import java.util.Optional;

@Service
public class SensorsService {

    private final SensorsRepository sensorsRepository;

    @Autowired
    public SensorsService(SensorsRepository sensorsRepository) {
        this.sensorsRepository = sensorsRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Sensor> findByName(String name) {
        return sensorsRepository.findByName(name);
    }

    @Transactional
    public void save(Sensor sensor) {
        sensorsRepository.save(sensor);
    }
}
