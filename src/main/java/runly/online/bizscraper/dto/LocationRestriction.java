package runly.online.bizscraper.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LocationRestriction {
    private Center center;
    private int radius;
}
