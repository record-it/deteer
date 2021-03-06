package pl.recordit.deteer.service;

import org.springframework.core.io.Resource;
import pl.recordit.deteer.dto.FileDocumentDto;
import pl.recordit.deteer.entity.FileDocument;
import pl.recordit.deteer.entity.Product;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface FileDocumentService {

    Optional<FileDocument> save(FileDocumentDto fileDocument);

    FileDocument findBy(long id);

    Resource findByFileName(String filename);

    Stream<Path> findAllAsResource();

    Stream<FileDocument> findAll();

    Stream<FileDocument> findByProductId(long productId, Predicate<FileDocument> filter);

}
