package runly.online.bizscraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import runly.online.bizscraper.model.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
}
