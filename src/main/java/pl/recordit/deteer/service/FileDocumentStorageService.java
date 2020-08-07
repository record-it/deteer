package pl.recordit.deteer.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pl.recordit.deteer.dto.FileDocumentDto;
import pl.recordit.deteer.mapper.FileDocumentMapper;
import pl.recordit.deteer.entity.FileDocument;
import pl.recordit.deteer.repository.FileDocumentRepository;
import pl.recordit.deteer.repository.ProductRepository;
import pl.recordit.deteer.storage.StorageService;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FileDocumentStorageService implements FileDocumentService {
    private final StorageService storageService;
    private final FileDocumentRepository fileRepo;
    private final FileDocumentMapper newDocumentMapper;
    private final ProductRepository prodRepo;


    public FileDocumentStorageService(StorageService storageService, FileDocumentRepository repository, ProductRepository productRepository) {
        this.storageService = storageService;
        this.fileRepo = repository;
        this.prodRepo = productRepository;
        newDocumentMapper = FileDocumentMapper.builder()
                .ownerMap(id -> productRepository.findById(id).orElse(null))
                .build();
    }

    @Override
    public Optional<FileDocument> save(FileDocumentDto fileDocument) {
        if (fileDocument == null || fileDocument.getFile() == null || fileDocument.getFile().isEmpty()){
            return Optional.empty();
        }
        storageService.store(fileDocument.getFile());
        return Optional.of(fileRepo.save(newDocumentMapper.fromDto(fileDocument)));
    }

    @Override
    public FileDocument findBy(long id) {
        return null;
    }

    @Override
    public Resource findByFileName(String filename) {
        return storageService.loadAsResource(filename);
    }

    @Override
    public Stream<Path> findAllAsResource() {
        return storageService.loadAll();
    }

    @Override
    public Stream<FileDocument> findAll() {
        return fileRepo.findAll().stream();
    }

    @Override
    public Stream<FileDocument> findByProductId(long productId) {
        return fileRepo.findByProductId(productId).stream();
    }
}
