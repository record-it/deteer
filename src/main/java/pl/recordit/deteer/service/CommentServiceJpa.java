package pl.recordit.deteer.service;

import org.springframework.stereotype.Service;
import pl.recordit.deteer.dto.CommentDto;
import pl.recordit.deteer.entity.Comment;
import pl.recordit.deteer.mapper.CommentMapper;
import pl.recordit.deteer.repository.CommentRepository;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CommentServiceJpa implements CommentService{
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ProductService productService;
    private final CommentMapper commentMapper;

    public CommentServiceJpa(CommentRepository commentRepository, UserServiceJpa userService, ProductServiceJpa productService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.productService = productService;
        commentMapper = CommentMapper.builder()
                .authorMap(id -> userService.findById(id).orElse(null))
                .productMap(id -> productService.findBy(id).orElse(null))
                .build();
    }

    @Override
    public Optional<Comment> create(CommentDto dto) {
        return commentMapper.fromDto(dto)
                .flatMap(entity -> Optional.of(commentRepository.save(entity)));
    }

    @Override
    public Stream<Comment> findAll() {
        return commentRepository.findAll().stream().sorted();
    }

    @Override
    public Stream<Comment> findByProduct(Long id) {
        return productService.findBy(id)
                .flatMap(entity -> Optional.of(commentRepository.findByProduct(entity).stream().sorted()))
                .orElse(Stream.empty());
    }
}
