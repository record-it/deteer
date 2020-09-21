package pl.recordit.deteer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.recordit.deteer.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsPublic(boolean isPublic);
}
