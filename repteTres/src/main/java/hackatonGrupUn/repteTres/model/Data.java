package hackatonGrupUn.repteTres.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("barribcn")
    private String barrioBcn;

    @JsonProperty("sum_pobl20")
    private int sumPoblacion20;

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
