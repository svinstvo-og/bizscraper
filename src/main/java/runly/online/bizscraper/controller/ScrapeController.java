package runly.online.bizscraper.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import runly.online.bizscraper.dto.ScrapeNearbyRequest;
import runly.online.bizscraper.dto.ScrapeRequest;
import runly.online.bizscraper.service.ScrapeService;

@Slf4j
@RestController
@RequestMapping("/api/v1.0/bizscraper")
public class ScrapeController {

    private final ScrapeService scrapeService;

    public ScrapeController(ScrapeService scrapeService) {
        this.scrapeService = scrapeService;
    }

    @PostMapping("/scrape/nearby")
    public void scrapeNearby(@RequestBody ScrapeNearbyRequest nearbyRequest) {
        log.info("scrape request: {}, {} results", nearbyRequest.getIncludedTypes(), nearbyRequest.getMaxResultCount());
        ScrapeRequest request = new ScrapeRequest(nearbyRequest.getLatitude(), nearbyRequest.getLongitude(),
                nearbyRequest.getRadiusKm()*1000, nearbyRequest.getIncludedTypes(), nearbyRequest.getMaxResultCount());
        System.out.println(scrapeService.searchNearby(request));
    }

}
