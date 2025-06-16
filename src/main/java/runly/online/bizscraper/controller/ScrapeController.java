package runly.online.bizscraper.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import runly.online.bizscraper.dto.ScrapeRequest;
import runly.online.bizscraper.service.ScrapeService;

@RestController
@RequestMapping("/api/v1.0/bizscraper")
public class ScrapeController {

    private final ScrapeService scrapeService;

    public ScrapeController(ScrapeService scrapeService) {
        this.scrapeService = scrapeService;
    }

    @PostMapping("/scrape")
    public void scrape(@RequestBody ScrapeRequest scrapeRequest) {
        scrapeService.searchNearby(scrapeRequest);
    }

}
