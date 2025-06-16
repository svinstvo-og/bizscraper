package runly.online.bizscraper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import runly.online.bizscraper.dto.ScrapeRequest;
import org.springframework.http.*;

@Slf4j
@Service
public class ScrapeService {

    @Value("${google.api.key}")
    private static String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://places.googleapis.com/v1/places:searchNearby";

    public ResponseEntity<String> searchNearby(ScrapeRequest scrapeRequest) {
        log.info("Adding headers to request: {}", apiKey);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", apiKey);
        headers.set("X-Goog-FieldMask", "places.displayName,places.businessStatus,places.formatedStatus,places.googleMapsUri,places.WebsiteUri");

        HttpEntity<ScrapeRequest> requestEntity = new HttpEntity<>(scrapeRequest, headers);

        return restTemplate.exchange(BASE_URL, HttpMethod.POST, requestEntity, String.class);
    }
}
