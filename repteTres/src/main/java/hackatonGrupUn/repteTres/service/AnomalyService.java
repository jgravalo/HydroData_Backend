package hackatonGrupUn.repteTres.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hackatonGrupUn.repteTres.dto.AnomalyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnomalyService {

    private static final Logger logger = LoggerFactory.getLogger(AnomalyService.class);
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public AnomalyService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = new ObjectMapper();
    }

    public List<AnomalyDto> getAllAnomalies() {
        List<AnomalyDto> anomalies = new ArrayList<>();
        try {
            InputStream inputStream = resourceLoader.getResource("classpath:json/2015_consum_aigua.json").getInputStream();
            JsonNode rootNode = objectMapper.readTree(inputStream);

            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    String location = node.path("barribcn").asText();
                    String zone = node.path("dtebcnnom").asText();
                    String description = "Consumo: " + node.path("lhabDia").asText() + " litros/día";
                    LocalDateTime timestamp = LocalDateTime.now();

                    anomalies.add(new AnomalyDto(location, zone, description, timestamp));
                }
            }
        } catch (IOException e) {
            logger.error("Error reading or processing JSON file: {}", e.getMessage(), e);
        }
        return anomalies;
    }
}