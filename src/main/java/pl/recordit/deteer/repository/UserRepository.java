package pl.recordit.deteer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.recordit.deteer.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
