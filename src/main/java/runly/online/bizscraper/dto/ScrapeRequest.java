package runly.online.bizscraper.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
public class ScrapeRequest {

    private List<String> includedTypes;
    private int maxResultCount;
    private LocationRestriction locationRestriction;

    public ScrapeRequest(double latitude, double longitude, Double radius, List<String> types, int maxResultCount) {
        this.includedTypes = types;
        this.maxResultCount = maxResultCount;

        Map<String, Double> circle = new HashMap<>();
        circle.put("latitude", latitude);
        circle.put("longitude", longitude);

        this.locationRestriction = new LocationRestriction(new Circle(circle, radius));
    }
}

