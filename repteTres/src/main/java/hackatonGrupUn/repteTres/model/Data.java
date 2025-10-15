package hackatonGrupUn.repteTres.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {

    @JsonProperty("barribcn")
    private String barrioBcn;

    @JsonProperty("sum_pobl2024")
    private int sumPoblacion2024;

    @JsonProperty("dtebcnnom")
    private String dteBcnNom;

    @JsonProperty("sum_consum")
    private double sumConsumo;

    @JsonProperty("sum_pobest")
    private int sumPoblacionEstimada;

    @JsonProperty("cons_hab")
    private double consHab;

    @JsonProperty("lhabdia")
    private double lHabDia;

}
