package runly.online.bizscraper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import runly.online.bizscraper.dto.ScrapeNearbyRequest;
import runly.online.bizscraper.dto.ScrapeRequest;
import runly.online.bizscraper.service.ScrapeService;

@Slf4j
@RestController
@RequestMapping("runly/api/v1.0/bizscraper")
public class ScrapeController {

    private final ScrapeService scrapeService;

    public ScrapeController(ScrapeService scrapeService) {
        this.scrapeService = scrapeService;
    }

    @PostMapping("/scrape/nearby")
    public void scrapeNearby(@RequestBody ScrapeNearbyRequest nearbyRequest) throws JsonProcessingException {
        log.info("scrape request: {}, {} results", nearbyRequest.getIncludedTypes(), nearbyRequest.getMaxResultCount());
        ScrapeRequest request = new ScrapeRequest(nearbyRequest.getLatitude(), nearbyRequest.getLongitude(),
                nearbyRequest.getRadiusKm()*1000, nearbyRequest.getIncludedTypes(), nearbyRequest.getMaxResultCount());
        scrapeService.searchNearby(request);
    }

    @DeleteMapping("/repeated")
    public void deleteRepeated() {
        log.info("Accepted delete repeated request");
        scrapeService.deleteRepeatedBusinesses();
    }
}
