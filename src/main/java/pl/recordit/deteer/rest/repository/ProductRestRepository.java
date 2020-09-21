package pl.recordit.deteer.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.recordit.deteer.rest.entity.ProductRest;

@Repository
public interface ProductRestRepository extends JpaRepository<ProductRest, Long> {
}
