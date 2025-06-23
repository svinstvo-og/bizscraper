package runly.online.bizscraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import runly.online.bizscraper.model.Business;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    @Query("SELECT MIN(id) FROM Business WHERE status = 'pending'")
    Long findAnyPending();
}
