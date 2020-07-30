package pl.recordit.deteer.service;

import pl.recordit.deteer.dto.CommentDto;
import pl.recordit.deteer.entity.Comment;

import java.util.Optional;
import java.util.stream.Stream;

public interface CommentService {
    Optional<Comment> create(CommentDto dto);
    Stream<Comment> findAll();
    Stream<Comment> findByProduct(Long id);
}
