package engine.persistance;

import engine.service.UserSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserSecurity, Integer> {

    Optional<UserSecurity> findByUsername(String username);

}
