package hackatonGrupUn.repteTres.jsonUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import hackatonGrupUn.repteTres.model.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureWrapper {

    @JsonProperty("properties")
    private Data data;

    // Getter y Setter
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}