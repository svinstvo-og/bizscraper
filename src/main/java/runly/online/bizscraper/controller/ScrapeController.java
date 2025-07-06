package runly.online.bizscraper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import runly.online.bizscraper.dto.ScrapeAreaRequest;
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
        ScrapeRequest request = new ScrapeRequest(nearbyRequest);
        scrapeService.searchNearby(request);
    }

    @DeleteMapping("/repeated")
    public void deleteRepeated() {
        log.info("Accepted delete repeated request");
        scrapeService.deleteRepeatedBusinesses();
    }

    @PostMapping("/scrape/area")
    public void scrapeArea(@RequestBody ScrapeAreaRequest request) throws JsonProcessingException {
        log.info("Accepted scrape area request");
        scrapeService.scrapeArea(request);
    }

    @DeleteMapping("/no-url")
    public void deleteWebsiteless() {
        log.info("Accepted delete websiteless request");
        scrapeService.deleteWebsiteless();
        log.info("Deleting websiteless businesses completed");
    }

    @DeleteMapping("/pending")
    public void deletePending() {
        log.info("Accepted delete pending request");
        scrapeService.deletePending();
    }

    @DeleteMapping("/region/{region}")
    public void deleteByRegion(@PathVariable String region) {
        log.info("Accepted delete region request");
        scrapeService.deleteByRegion(region);
    }
}
