package hackatonGrupUn.repteTres.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnomalyDto {

    @JsonProperty("location")
    private String location;
    @JsonProperty("zone")
    private String zone;
    @JsonProperty("description")
    private String description;
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

}