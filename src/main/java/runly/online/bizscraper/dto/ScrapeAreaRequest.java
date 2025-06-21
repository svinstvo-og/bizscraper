package runly.online.bizscraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScrapeAreaRequest {
    @JsonProperty("area-width-km")
    private Double areaWidth;
    @JsonProperty("center-latitude")
    private double centerLatitude;
    @JsonProperty("center-longitude")
    private double centerLongitude;
    @JsonProperty("included-types")
    private List<String> includedTypes;
}
