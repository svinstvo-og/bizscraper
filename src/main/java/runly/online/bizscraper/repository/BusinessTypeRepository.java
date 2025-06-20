package runly.online.bizscraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import runly.online.bizscraper.model.BusinessType;

public interface BusinessTypeRepository extends JpaRepository<BusinessType, Long> {
    public BusinessType findByName(String name);
    public boolean existsByName(String name);
}
