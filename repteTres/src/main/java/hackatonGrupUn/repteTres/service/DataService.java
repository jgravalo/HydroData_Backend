package hackatonGrupUn.repteTres.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hackatonGrupUn.repteTres.model.Data;
import hackatonGrupUn.repteTres.jsonUtils.FeatureWrapper;
import hackatonGrupUn.repteTres.jsonUtils.Root;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service; // 👈 Importante
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataService {


    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Data> loadWaterConsumptionData(String jsonFilePath) throws IOException {

        ClassPathResource resource = new ClassPathResource(jsonFilePath);

        Root rootObject = objectMapper.readValue(resource.getInputStream(), Root.class);

        return rootObject.getFeatures().stream()
                .map(FeatureWrapper::getData)
                .collect(Collectors.toList());
    }
}