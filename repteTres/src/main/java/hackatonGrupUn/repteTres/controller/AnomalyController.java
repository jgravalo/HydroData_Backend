package hackatonGrupUn.repteTres.controller;

import hackatonGrupUn.repteTres.service.AnomalyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("hydraulic/api/v1/averages")
public class AnomalyController {

    private final AnomalyService anomalyService;

    public AnomalyController(AnomalyService anomalyService) {
        this.anomalyService = anomalyService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Double>> getDistrictAverages() {
        Map<String, Double> averages = anomalyService.getDistrictAverages();
        return ResponseEntity.ok(averages);
    }
}