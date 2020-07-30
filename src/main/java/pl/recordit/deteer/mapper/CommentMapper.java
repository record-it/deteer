package pl.recordit.deteer.mapper;

import lombok.Builder;
import pl.recordit.deteer.dto.CommentDto;
import pl.recordit.deteer.entity.Comment;
import pl.recordit.deteer.entity.CommentStatus;
import pl.recordit.deteer.entity.Product;
import pl.recordit.deteer.entity.User;

import java.util.Optional;
import java.util.function.Function;

@Builder
public class CommentMapper {
    private final Function<Long, Product> productMap;
    private final Function<Long, User> authorMap;
    public Optional<Comment> fromDto(CommentDto dto) {
        //TODO dodać warunek, że autor nie może być null
        return  dto != null && dto.getContent() != null && dto.getProductId() != null
                ? Optional.of(Comment.builder()
                .content(dto.getContent())
                .status(dto.getStatus())
                .product(productMap != null ? productMap.apply(dto.getProductId()) : null)
                .author(authorMap != null && dto.getAuthorId() != null ? authorMap.apply(dto.getAuthorId()) : null)
                .status(CommentStatus.REGISTERED)
                .build())
                : Optional.empty();
    }
}
