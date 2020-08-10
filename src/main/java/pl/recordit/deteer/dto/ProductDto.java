package pl.recordit.deteer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String properties;
    private String name;
    private Long parentId;
    private Long operatingManualId;
    private Long energyLabelId;
    private Long productSheetId;
}
