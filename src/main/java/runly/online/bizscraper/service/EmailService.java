package runly.online.bizscraper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import runly.online.bizscraper.dto.SendEmailRequest;

@Slf4j
@Service
public class EmailService {

    private final ZohoTokenService zohoTokenService;

    private

    public EmailService(ZohoTokenService zohoTokenService) {
        this.zohoTokenService = zohoTokenService;
    }

    private final RestClient zohoMailClient = RestClient.builder()
            .baseUrl("https://mail.zoho.eu/api")
            .build();

    public int sendEmail(String accountId, String accessToken, String toAddress, String subject, String body) {
        try {
            log.info("Sending email via Zoho Mail API to: {}", toAddress);

            SendEmailRequest request = new SendEmailRequest("support@runly.online", toAddress, subject, body);

            ResponseEntity<Void> response = zohoMailClient
                    .post()
                    .uri("/accounts/{accountId}/messages", accountId)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, "Zoho-oauthtoken " + accessToken)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

            int statusCode = response.getStatusCode().value();
            log.info("Email sent. Status code: {}", statusCode);
            return statusCode;

        } catch (RestClientException e) {
            log.error("Failed to send email via Zoho Mail API: {}", e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
