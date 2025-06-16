package runly.online.bizscraper.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScrapeRequest {

    private List<String> inludedTypes;
    private int maxResultCount;
    private LocationRestriction locationRestriction;

    public ScrapeRequest(double latitude, double longitude, int radius, List<String> types, int maxResultCount) {
        this.inludedTypes = types;
        this.maxResultCount = maxResultCount;

        this.locationRestriction = new LocationRestriction(new Center(latitude, longitude), radius);
    }
}

