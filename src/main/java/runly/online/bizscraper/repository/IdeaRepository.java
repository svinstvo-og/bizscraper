package runly.online.bizscraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import runly.online.bizscraper.model.Idea;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {
}
