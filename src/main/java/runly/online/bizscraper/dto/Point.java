package runly.online.bizscraper.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    private Double longitude;
    private Double latitude;

    public Point shift(Double xKm, Double yKm) {
        final double R = 6371.0;

        double latRad = Math.toRadians(this.latitude);
        double lonRad = Math.toRadians(this.longitude);

        // Calculate new latitude (north/south shift)
        double newLat = this.latitude + (yKm / R) * (180 / Math.PI);

        // Calculate new longitude (east/west shift)
        // Adjust for latitude - degrees of longitude get closer at higher latitudes
        double newLon = this.longitude + (xKm / (R * Math.cos(latRad))) * (180 / Math.PI);

        return new Point(newLat, newLon);
    }
//
//    public Integer kilometersBetween(Point p1, Point p2) {
//
//    }
}
