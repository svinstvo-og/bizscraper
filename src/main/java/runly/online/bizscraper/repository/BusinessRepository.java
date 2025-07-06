package runly.online.bizscraper.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import runly.online.bizscraper.model.Business;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    @Query("SELECT MIN(id) FROM Business WHERE status = 'pending'")
    Long findAnyPending();

    @Query("SELECT b FROM Business b WHERE b.status = :status")
    List<Business> findTopNByStatus(@Param("status") String status, Pageable pageable);

    @Query("SELECT DISTINCT b.email FROM Business b")
    Set<String> findDistinctEmails();

    List<Business> findByCountry(String country);
}
