package hackatonGrupUn.repteTres.controller;

import hackatonGrupUn.repteTres.model.Data;
import hackatonGrupUn.repteTres.repository.DataRepository;
import hackatonGrupUn.repteTres.service.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class DataController {

    private final DataService dataService;
    private final DataRepository dataRepository;

    public DataController(DataService dataService, DataRepository dataRepository) {
        this.dataService = dataService;
        this.dataRepository = dataRepository;
    }

    @GetMapping("hydraulic/api/v1/data")
    public List<Data> getAllData() {
            return dataRepository.findAll();
    }
}