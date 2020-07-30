package pl.recordit.deteer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.recordit.deteer.entity.Comment;
import pl.recordit.deteer.entity.Product;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProduct(Product product);
}
