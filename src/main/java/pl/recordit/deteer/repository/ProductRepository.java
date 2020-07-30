package pl.recordit.deteer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.recordit.deteer.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
