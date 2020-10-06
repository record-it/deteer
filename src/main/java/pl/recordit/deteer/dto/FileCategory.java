package pl.recordit.deteer.dto;

import pl.recordit.deteer.entity.FileDocument;
import pl.recordit.deteer.entity.Product;
import java.util.function.BiConsumer;

public enum FileCategory {
    ENERGY_LABEL("etykieta energetyczna", Product::setEnergyLabel),
    OPERATING_MANUAL("instrukcja obsługi", Product::setOperatingManual),
    PRODUCT_SHEET("karta produktu", Product::setProductSheet),
    OTHER("inny", null);

    private final String polishName;

    private final BiConsumer<Product, FileDocument> binder;

    FileCategory(String polishName, BiConsumer<Product, FileDocument> binder) {
        this.polishName = polishName;
        this.binder = binder;
    }

    public String getPolishName() {
        return polishName;
    }

    public void bind(Product product, FileDocument document){
        if (binder != null){
            binder.accept(product, document);
        }
    }
}
