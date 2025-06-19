package runly.online.bizscraper.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScrapeLocationRequest {
    private Map<String, String> displayName;
    private String websiteUri;
    private String googleMapsUri;
    private List<Map<String, String>> addressComponents;
}
