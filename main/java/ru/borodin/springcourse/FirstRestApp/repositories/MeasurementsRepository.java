package ru.borodin.springcourse.FirstRestApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.borodin.springcourse.FirstRestApp.models.Measurement;

public interface MeasurementsRepository extends JpaRepository<Measurement, Integer> {
    int countAllByRainingTrue();
}
