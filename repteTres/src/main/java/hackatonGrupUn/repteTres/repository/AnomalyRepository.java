package hackatonGrupUn.repteTres.repository;

import hackatonGrupUn.repteTres.model.Anomaly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly, Long> {
}