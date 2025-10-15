package hackatonGrupUn.repteTres.controller;

import hackatonGrupUn.repteTres.model.Data;
import hackatonGrupUn.repteTres.service.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("hydraulic/api/v1/data")
    public List<Data> getConsumptionData() {
        try {
            return dataService.loadWaterConsumptionData("/json/2015_consum_aigua.json");
        } catch (IOException e) {
            System.err.println("Error reading JSON file:");
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}