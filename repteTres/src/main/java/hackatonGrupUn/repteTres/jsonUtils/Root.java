package hackatonGrupUn.repteTres.jsonUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Root {

    private List<FeatureWrapper> features;

    // Getter y Setter
    public List<FeatureWrapper> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatureWrapper> features) {
        this.features = features;
    }
}