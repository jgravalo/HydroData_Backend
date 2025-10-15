package hackatonGrupUn.repteTres.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "anomaly_config")
public class Alert {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "section_id", nullable = false)
    private String sectionId;

    @Column(name = "threshold_percent", nullable = false)
    private Double thresholdPercent;

    @Column(name = "notify_email", nullable = false)
    private Boolean notifyEmail;
}