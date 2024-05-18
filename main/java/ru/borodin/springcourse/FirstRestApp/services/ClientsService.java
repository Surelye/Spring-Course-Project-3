package ru.borodin.springcourse.FirstRestApp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.borodin.springcourse.FirstRestApp.dto.MeasurementDTO;
import ru.borodin.springcourse.FirstRestApp.dto.SensorDTO;

import java.util.Random;
import java.util.stream.IntStream;

@Service
public class ClientsService {

    private final int NUM_QUERIES = 1000;
    private final int MIN_TEMPERATURE = -100;
    private final int MAX_TEMPERATURE = 100;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Random random;

    @Autowired
    public ClientsService(RestTemplate restTemplate, ObjectMapper objectMapper, Random random) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.random = random;
    }

    public void addSensor(SensorDTO sensorDTO) {
        restTemplate.postForObject("http://localhost:8080/sensors/registration", sensorDTO,
                String.class);
    }

    public void addOneThousandRandomMeasurements(SensorDTO sensorDTO) {
        for (int i = 0; i != NUM_QUERIES; i++) {
            restTemplate.postForObject(
                    "http://localhost:8080/measurements/add",
                    generateRandomMeasurementsDTO(sensorDTO),
                    String.class
            );
        }
    }

    public MeasurementDTO generateRandomMeasurementsDTO(SensorDTO sensorDTO) {
        return new MeasurementDTO(
                getRandomDoubleInRange(),
                random.nextBoolean(),
                sensorDTO
        );
    }

    public double getRandomDoubleInRange() {
        return MIN_TEMPERATURE + (MAX_TEMPERATURE - MIN_TEMPERATURE) * random.nextDouble();
    }

    public double[] getMeasurementTemperatures() throws JsonProcessingException {
        String measurements = restTemplate.getForObject("http://localhost:8080/measurements",
                String.class);
        JsonNode measurementsJson = objectMapper.readTree(measurements);
        return IntStream.range(Math.max(0, measurementsJson.size() - NUM_QUERIES),
                        measurementsJson.size())
                .mapToDouble(i -> measurementsJson.get(i).get("value").asDouble())
                .toArray();
    }

    public void showChart(double[] temperatures) {
        double[] doubleStream = IntStream.range(0, temperatures.length).asDoubleStream().toArray();
        XYChart chart = QuickChart.getChart("Measurements Temperatures",
                "Measurement", "Temperature", "f",
                doubleStream, temperatures);
        new SwingWrapper<>(chart).displayChart();
    }
}
