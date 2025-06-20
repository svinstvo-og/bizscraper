package runly.online.bizscraper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import runly.online.bizscraper.dto.Place;
import runly.online.bizscraper.dto.ScrapeLocationResponse;
import runly.online.bizscraper.dto.ScrapeRequest;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Slf4j
@Service
public class ScrapeService {

    @Value("${google.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://places.googleapis.com/v1/places:searchNearby";

    public ResponseEntity<String> searchNearby(ScrapeRequest scrapeRequest) throws JsonProcessingException {
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

    private List<Place> retrieveBusinessesFromResponse(ResponseEntity<String> response) throws JsonProcessingException {
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Got 200 response: {}", response.getBody());
            String body = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            ScrapeLocationResponse locationResponse = objectMapper.readValue(body, ScrapeLocationResponse.class);
            log.info("Mapped {} objects", locationResponse.getPlaces().size());
            return locationResponse.getPlaces();
            //System.out.println(locationResponse.getPlaces());
        }
        else {
            log.info("Got 500 response: {}", response.getBody());
        }
        return null;
    }

    public void saveBusinesses(List<Place> places) {
        log.info("Saving places...");

    }
}
