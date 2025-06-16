package runly.online.bizscraper.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import runly.online.bizscraper.dto.ScrapeRequest;

@Service
public class ScrapeService {

    @Value("${google.api.key}")
    private static String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public void searchNearby(ScrapeRequest scrapeRequest) {
        String url = UriComponentsBuilder.fromUriString("https://places.googleapis.com/v1/places:searchNearby")
                .queryParam("includedTypes", scrapeRequest.getIncludedTypes())
                .queryParam("maxResultCount", 100)
                .queryParam("key", apiKey)
                .toUriString();
    }
    public void formRequest(ScrapeRequest scrapeRequest) {}
}
