package engine.persistance;

import engine.service.SolvedQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolvedQuestionRepository extends JpaRepository<SolvedQuestion, Integer> {

    Page<SolvedQuestion> findAllByOwner(String owner, Pageable pageable);

}
