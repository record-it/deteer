package pl.recordit.deteer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.recordit.deteer.entity.FileDocument;

import java.util.List;

@Repository
public interface FileDocumentRepository extends JpaRepository<FileDocument, Long> {
  List<FileDocument> findByProductId(long id);
}
