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

    /**
     * Lee un archivo GeoJSON del classpath y extrae una lista limpia de datos.
     * @param jsonFilePath Ruta relativa al classpath (e.g., /json/file.json).
     * @return Una lista de objetos Data.
     * @throws IOException Si hay un problema al leer el archivo.
     */
    public List<Data> loadWaterConsumptionData(String jsonFilePath) throws IOException {

        ClassPathResource resource = new ClassPathResource(jsonFilePath);

        Root rootObject = objectMapper.readValue(resource.getInputStream(), Root.class);

        return rootObject.getFeatures().stream()
                .map(FeatureWrapper::getData)
                .collect(Collectors.toList());
    }
}