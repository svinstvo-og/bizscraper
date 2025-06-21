package runly.online.bizscraper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import runly.online.bizscraper.dto.*;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import runly.online.bizscraper.model.Business;
import runly.online.bizscraper.model.BusinessType;
import runly.online.bizscraper.model.Idea;
import runly.online.bizscraper.repository.BusinessRepository;
import runly.online.bizscraper.repository.BusinessTypeRepository;

import java.util.*;

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
        log.info("Adding headers to request: {}", API_KEY);
        log.info("Scrape Requst: types = {}, max results = {}, circle center = {}, center coordinates = {},", scrapeRequest.getIncludedTypes(),
                scrapeRequest.getMaxResultCount(), scrapeRequest.getLocationRestriction().getCircle().getCenter(),
                scrapeRequest.getLocationRestriction().getCircle().getCenter().toString());
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
        if (response.getStatusCode().is2xxSuccessful() && response.getBody().length() > 3) {
            log.info("Got 200 response: {}", response.getBody());
            String body = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            ScrapeLocationResponse locationResponse = objectMapper.readValue(body, ScrapeLocationResponse.class);
            log.info("Mapped {} objects", locationResponse.getPlaces().size());
            //System.out.println(locationResponse.getPlaces() + "JOOOOOGGAHHH");
            return locationResponse.getPlaces();
        }
        else {
            log.info("Got 500 response: {}", response.getBody());
        }
        return null;
    }

    private void saveBusinesses(List<Place> places) {
        log.info("Saving places...");
        Business business;
        if (places == null || places.isEmpty()) {
            log.info("No places found while saving");
            return;
        }
        for (Place place : places) {
            if (place == null) {
                log.info("Place is null");
                continue;
            }
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

    @Transactional
    public void deleteRepeatedBusinesses() {
        log.info("Deleting repeated businesses...");
        List<Business> businesses = businessRepository.findAll();
        HashMap<String, String> uniqueBusinesses = new HashMap<>();
        for (Business business : businesses) {
            if (uniqueBusinesses.containsKey(business.getName())) {
                log.info("Deleting repeated business: {}", business.getName());
                uniqueBusinesses.put(business.getName(), business.getGoogleMapsUrl());
                businessRepository.delete(business);
            }
            else {
                uniqueBusinesses.put(business.getName(), business.getGoogleMapsUrl());
            }
        }
        log.info("Deleted repeated businesses: {} deleted, {} initial, {} left", businesses.size() - uniqueBusinesses.size(), businesses.size(), uniqueBusinesses.size());
    }

    public void scrapeArea(ScrapeAreaRequest request) throws JsonProcessingException {
        Point center = new Point(request.getCenterLongitude(), request.getCenterLatitude());
        Double eastBound = center.shift(request.getAreaWidth() / 2.0, 0.0).getLatitude();
        Double westBound = center.shift(request.getAreaWidth() / -2.0, 0.0).getLatitude();
        Double northBound = center.shift(0.0, request.getCenterLongitude() / 2.0).getLongitude();
        Double southBound = center.shift(0.0, request.getCenterLongitude() / -2.0).getLongitude();
        log.info("Calculated area bounds: west = {}, east = {}, north = {}, south = {}", westBound, eastBound, northBound, southBound);
//
//        Double latPointer = westBound;
//        Double longPointer = northBound;
        int latResetCounter = 0;
        int iterationCounter = 0;

        Voxel voxel = new Voxel(westBound, southBound, 1.0, 20, request.getIncludedTypes());

        while (voxel.getLongitude() < northBound) {
            log.info("Iteraton by Y {}", latResetCounter);
            while(voxel.getLatitude() < eastBound) {
                searchNearby(new ScrapeRequest(voxel));
                log.info("Old latitude = {}", voxel.getLatitude());
                voxel.setLatitude(voxel.shiftLatitude(1.0));
                log.info("New latitude = {}", voxel.getLatitude());
                iterationCounter++;
                log.info("Iteration by X {}: voxel latitude = {}, voxel longitude = {}, east bound = {}", iterationCounter, voxel.getLatitude(), voxel.getLongitude(), eastBound);
            }
            log.warn("REsetting Lat");
            voxel.setLatitude(westBound);
            if (latResetCounter % 2 == 0) {
                voxel.setLatitude(voxel.shiftLatitude(0.5));
            }
            latResetCounter++;
            log.info("Old long = {}", voxel.getLongitude());
            voxel.setLongitude(voxel.shiftLongitude(0.5));
            log.info("New long = {}", voxel.getLongitude());
        }
        log.info("Scraping complete!");
    }
}
