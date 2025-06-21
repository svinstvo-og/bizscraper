package runly.online.bizscraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Voxel extends Point {
    @JsonProperty("radius-km")
    private Double radiusKm;
    @JsonProperty("max-result-count")
    private int maxResultCount;
    @JsonProperty("included-types")
    private List<String> includedTypes;

    public Voxel(Double latitude, Double longitude, Double radiusKm, int maxResultCount, List<String> includedTypes) {
        this.radiusKm = radiusKm;
        this.maxResultCount = maxResultCount;
        this.includedTypes = includedTypes;
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }

    public double shiftLatitude(double km) {
        // Earth's radius in km
        final double R = 6371.0;

        // Convert km to degrees (1° ≈ 111.32 km)
        double deltaDegrees = (km / R) * (180 / Math.PI);
        double newLat = this.getLatitude() + deltaDegrees;

        // Ensure latitude stays within valid range [-90, 90]
        newLat = Math.max(-90.0, Math.min(90.0, newLat));

        this.setLatitude(newLat);
        return newLat;
    }

    /**
     * Shifts the longitude by specified kilometers (east/west)
     * @param km Distance in kilometers (positive = east, negative = west)
     * @return New longitude value
     */
    public double shiftLongitude(double km) {
        // Earth's radius in km
        final double R = 6371.0;

        // Current latitude in radians
        double latRad = Math.toRadians(this.getLatitude());

        // Convert km to degrees (adjusting for latitude)
        // At equator: 1° ≈ 111.32 km
        // At poles: longitude shifts become infinite (handled by cos(latRad))
        double deltaDegrees = (km / (R * Math.cos(latRad))) * (180 / Math.PI);
        double newLon = this.getLongitude() + deltaDegrees;

        // Normalize longitude to [-180, 180]
        newLon = (newLon + 540) % 360 - 180;

        this.setLongitude(newLon);
        return newLon;
    }

    /**
     * Shifts both latitude and longitude
     * @param latKm Distance to shift latitude (north+ / south-)
     * @param lonKm Distance to shift longitude (east+ / west-)
     * @return This Voxel instance for method chaining
     */
    public Voxel shift(double latKm, double lonKm) {
        this.shiftLatitude(latKm);
        this.shiftLongitude(lonKm);
        return this;
    }
}
