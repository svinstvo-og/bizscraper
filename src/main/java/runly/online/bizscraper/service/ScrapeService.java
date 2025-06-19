package runly.online.bizscraper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import runly.online.bizscraper.dto.ScrapeRequest;
import org.springframework.http.*;

@Slf4j
@Service
public class ScrapeService {

    @Value("${google.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://places.googleapis.com/v1/places:searchNearby";

    public ResponseEntity<String> searchNearby(ScrapeRequest scrapeRequest) {
        System.out.println(scrapeRequest.getIncludedTypes());
        log.info("Adding headers to request: {}", API_KEY);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", API_KEY);
        headers.set("X-Goog-FieldMask", "places.displayName,places.types,places.googleMapsUri,places.websiteUri,places.addressComponents");

        HttpEntity<ScrapeRequest> requestEntity = new HttpEntity<>(scrapeRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, requestEntity, String.class);

        retrieveBusinessesFromResponse(response);

        return response;
    }

    public void retrieveBusinessesFromResponse(ResponseEntity<String> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Got 200 response: {}", response.getBody());
            String body = response.getBody();
            return ;
        }
        else {
            log.info("Got 500 response: {}", response.getBody());
        }
    }
}
