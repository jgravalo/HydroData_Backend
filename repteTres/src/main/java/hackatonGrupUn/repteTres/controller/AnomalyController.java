package hackatonGrupUn.repteTres.controller;

import hackatonGrupUn.repteTres.dto.AnomalyDto;
import hackatonGrupUn.repteTres.service.AnomalyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/anomalies")
public class AnomalyController {

    private final AnomalyService anomalyService;

    // Inyección de dependencias a través del constructor
    public AnomalyController(AnomalyService anomalyService) {
        this.anomalyService = anomalyService;
    }

    @GetMapping
    public ResponseEntity<List<AnomalyDto>> getAllAnomalies() {
        // Llama al servicio para obtener la lista de anomalías
        List<AnomalyDto> anomalies = anomalyService.getAllAnomalies();
        return ResponseEntity.ok(anomalies);
    }
}