package hackatonGrupUn.repteTres.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hackatonGrupUn.repteTres.model.Data;
import hackatonGrupUn.repteTres.jsonUtils.FeatureWrapper;
import hackatonGrupUn.repteTres.jsonUtils.Root;
import hackatonGrupUn.repteTres.repository.DataRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataRepository dataRepository;

    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<Data> loadInitialData(String jsonFilePath) throws IOException {

        ClassPathResource resource = new ClassPathResource(jsonFilePath);

        Root rootObject = objectMapper.readValue(resource.getInputStream(), Root.class);

        List<Data> dataList = rootObject.getFeatures().stream()
                .map(FeatureWrapper::getData)
                .collect(Collectors.toList());

        return dataRepository.saveAll(dataList);
    }

    public List<Data> getAllData() {
        return dataRepository.findAll();
    }
}