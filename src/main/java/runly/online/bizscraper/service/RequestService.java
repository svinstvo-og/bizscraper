package runly.online.bizscraper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import runly.online.bizscraper.dto.EmailGeneratedResponse;

@Slf4j
@Service
public class RequestService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:5678/webhook"; //-test

    public ResponseEntity<EmailGeneratedResponse> generateEmail(Long businessId, String url, String languageCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("business-id", String.valueOf(businessId));
        headers.add("url", url);
        headers.add("language-code", languageCode);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);


        ResponseEntity<EmailGeneratedResponse> response = restTemplate
                .exchange(baseUrl + "/outreach/generate", HttpMethod.POST, requestEntity, EmailGeneratedResponse.class);

        log.info("Email generation request sent. Status: {}", response.getStatusCode());
        return response;
    }
}