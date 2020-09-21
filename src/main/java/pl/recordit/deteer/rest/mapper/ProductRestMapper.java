package pl.recordit.deteer.rest.mapper;

import pl.recordit.deteer.entity.Product;
import pl.recordit.deteer.model.JsonMap;
import pl.recordit.deteer.rest.entity.ProductRest;
import java.util.Optional;

public class ProductRestMapper {
    private enum Mapper {
        INSTANCE;

        public Optional<ProductRest> mapTo(Product product) {
            if (product == null) {
                return Optional.empty();
            }
            return product.getProperties().toJson()
                    .flatMap(json -> Optional.of(
                            ProductRest.builder()
                                    .name(product.getName())
                                    .properties(json)
                                    .productId(product.getId())
                                    .build()
                            )
                    );
        }
    }

    public Optional<ProductRest> mapTo(Product product){
        return Mapper.INSTANCE.mapTo(product);
    }
}
