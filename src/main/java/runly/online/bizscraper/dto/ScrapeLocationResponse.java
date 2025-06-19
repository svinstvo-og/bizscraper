package runly.online.bizscraper.dto;


import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScrapeLocationResponse {
    private List<Place> places;
}
