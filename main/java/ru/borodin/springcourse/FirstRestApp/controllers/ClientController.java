package ru.borodin.springcourse.FirstRestApp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.borodin.springcourse.FirstRestApp.dto.SensorDTO;
import ru.borodin.springcourse.FirstRestApp.services.ClientsService;
import ru.borodin.springcourse.FirstRestApp.services.SensorsService;
import ru.borodin.springcourse.FirstRestApp.util.SensorDTOValidator;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/client")
public class ClientController {

    private final ClientsService clientsService;
    private final SensorsService sensorsService;
    private final SensorDTOValidator sensorDTOValidator;

    @Autowired
    public ClientController(ClientsService clientsService, SensorsService sensorsService,
                            SensorDTOValidator sensorDTOValidator) {
        this.clientsService = clientsService;
        this.sensorsService = sensorsService;
        this.sensorDTOValidator = sensorDTOValidator;
    }

    @GetMapping("/addData")
    public String addSensorPage(@ModelAttribute("sensorDTO") SensorDTO sensorDTO) {
        return "/client/addData";
    }

    @PostMapping("/addSensor")
    public String addSensor(@Valid @ModelAttribute("sensorDTO") SensorDTO sensorDTO,
                            BindingResult bindingResult) {
        sensorDTOValidator.validate(sensorDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/client/addData";
        }
        clientsService.addSensor(sensorDTO);
        return "redirect:/client/addData";
    }

    @PostMapping("/addMeasurements")
    public String addMeasurements(@Valid @ModelAttribute("sensorDTO") SensorDTO sensorDTO,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/client/addData";
        } else if (sensorsService.findByName(sensorDTO.getName()).isPresent()) {
            clientsService.addOneThousandRandomMeasurements(sensorDTO);
            return "redirect:/client/addData";
        }
        bindingResult.rejectValue("name", "",
                "Sensor with name " + sensorDTO.getName() + " doest not exist");
        return "/client/addData";
    }

    @GetMapping("/measurements")
    public String getMeasurements(Model model) throws JsonProcessingException {
        double[] temperatures = clientsService.getMeasurementTemperatures();
        model.addAttribute("measurements", Arrays.toString(temperatures));
        clientsService.showChart(temperatures);
        return "/client/showMeasurements";
    }
}
