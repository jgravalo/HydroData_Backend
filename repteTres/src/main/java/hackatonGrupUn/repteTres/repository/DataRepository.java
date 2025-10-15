package hackatonGrupUn.repteTres.repository;

import hackatonGrupUn.repteTres.model.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends JpaRepository<Data, Long> {
}
