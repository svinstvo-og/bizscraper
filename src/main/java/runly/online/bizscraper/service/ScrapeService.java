package runly.online.bizscraper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import runly.online.bizscraper.dto.Place;
import runly.online.bizscraper.dto.ScrapeLocationResponse;
import runly.online.bizscraper.dto.ScrapeRequest;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import runly.online.bizscraper.model.Business;
import runly.online.bizscraper.model.BusinessType;
import runly.online.bizscraper.model.Idea;
import runly.online.bizscraper.repository.BusinessRepository;
import runly.online.bizscraper.repository.BusinessTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ScrapeService {

    final
    BusinessRepository businessRepository;

    final BusinessTypeRepository businessTypeRepository;

    @Value("${google.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://places.googleapis.com/v1/places:searchNearby";

    public ScrapeService(BusinessRepository businessRepository, BusinessTypeRepository businessTypeRepository) {
        this.businessRepository = businessRepository;
        this.businessTypeRepository = businessTypeRepository;
    }

    public ResponseEntity<String> searchNearby(ScrapeRequest scrapeRequest) throws JsonProcessingException {
        System.out.println(scrapeRequest.getIncludedTypes());
        log.info("Adding headers to request: {}", API_KEY);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", API_KEY);
        headers.set("X-Goog-FieldMask", "places.displayName,places.types,places.googleMapsUri,places.websiteUri,places.addressComponents");

        HttpEntity<ScrapeRequest> requestEntity = new HttpEntity<>(scrapeRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, requestEntity, String.class);

        List<Place> places = retrieveBusinessesFromResponse(response);
        saveBusinesses(places);

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
        Business business;
        for (Place place : places) {
            List<BusinessType> types = new ArrayList<>();
            business = new Business(place);
            for (String type : place.getTypes()) {
                BusinessType businessType = businessTypeRepository.findByName(type);
                if (businessType != null) {
                    types.add(businessType);
                }
                else {
                    log.info("Business type {} not found, creating one...", type);
                    BusinessType newBusinessType = new BusinessType(type);
                    types.add(newBusinessType);
                    businessTypeRepository.save(newBusinessType);
                }
                business.setTypes(types);
            }
            log.info("Assigned {} types to {}", types.size(), business.getName());
            businessRepository.save(business);
            log.info("Saved business: {}", place.getDisplayName().get("text"));
        }
    }
}
