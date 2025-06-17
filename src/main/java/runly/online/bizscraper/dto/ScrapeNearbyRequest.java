package runly.online.bizscraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScrapeNearbyRequest {
    @JsonProperty("radius-km")
    private Double radiusKm;
    private double latitude;
    private double longitude;
    @JsonProperty("max-result-count")
    private int maxResultCount;
    @JsonProperty("included-types")
    private List<String> includedTypes;
}
