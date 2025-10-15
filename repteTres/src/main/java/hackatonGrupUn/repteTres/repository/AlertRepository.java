package hackatonGrupUn.repteTres.repository;

import hackatonGrupUn.repteTres.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByUserId(String userId);
    List<Alert> findBySectionId(String sectionId);
}
