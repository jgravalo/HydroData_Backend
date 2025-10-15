package hackatonGrupUn.repteTres.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnomalyDto {

    private String location;
    private String zone;
    private String description;
    private LocalDateTime timestamp;

}