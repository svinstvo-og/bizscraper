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

    public ScrapeRequest(ScrapeNearbyRequest request) {
        this.includedTypes = request.getIncludedTypes();
        this.maxResultCount = request.getMaxResultCount();

        Map<String, Double> circle = new HashMap<>();
        circle.put("latitude", request.getLatitude());
        circle.put("longitude", request.getLongitude());

        this.locationRestriction = new LocationRestriction(new Circle(circle, request.getRadiusKm()*1000));
    }

    public ScrapeRequest(Voxel voxel) {
        this.includedTypes = voxel.getIncludedTypes();
        this.maxResultCount = 20;

        Map<String, Double> circle = new HashMap<>();
        circle.put("latitude", voxel.getLatitude());
        circle.put("longitude", voxel.getLongitude());

        this.locationRestriction = new LocationRestriction(new Circle(circle, 1000.0));
    }
}

