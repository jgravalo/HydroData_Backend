package hackatonGrupUn.repteTres.dto;

import java.time.LocalDateTime;

public class AnomalyDto {

    private String location;
    private String zone;
    private String description;
    private LocalDateTime timestamp;

    public AnomalyDto() {}

    public AnomalyDto(String location, String zone, String description, LocalDateTime timestamp) {
        this.location = location;
        this.zone = zone;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public String getZone() {
        return zone;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}