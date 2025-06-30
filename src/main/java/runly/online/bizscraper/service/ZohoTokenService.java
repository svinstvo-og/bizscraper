package runly.online.bizscraper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import runly.online.bizscraper.dto.ZohoTokenResponse;

@Slf4j
@Service
public class ZohoTokenService {

    private final RestClient zohoClient = RestClient.builder()
            .baseUrl("https://accounts.zoho.eu/oauth/v2")
            .build();

    private static String bearer;

    @Value("${zoho.api.refresh-token}")
    private String refreshToken;
    @Value("${zoho.api.client-secret}")
    private String clientSecret;
    @Value("${zoho.api.client-id}")
    private String clientId;

    public String getBearer() {
        if (bearer == null) {
            log.info("ZohoTokenService instantiated: bearer - {}, refresh - {}, clientSecret - {}, clientId - {}", bearer, refreshToken, clientSecret, clientId);
            bearer = refreshToken().getAccessToken();
            log.info("ZohoTokenService instantiated: bearer - {}", bearer);
        }
        return bearer;
    }

    private ZohoTokenResponse refreshToken() {
        try {
            log.info("Refreshing Zoho access token");

            ResponseEntity<ZohoTokenResponse> response = zohoClient
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/token")
                            .queryParam("refresh_token", refreshToken)
                            .queryParam("grant_type", "refresh_token")
                            .queryParam("client_id", clientId)
                            .queryParam("client_secret", clientSecret)
                            .build())
                    .retrieve()
                    .toEntity(ZohoTokenResponse.class);

            ZohoTokenResponse tokenResponse = response.getBody();

            if (tokenResponse != null) {
                log.info("Token refreshed successfully. Expires in: {} seconds", tokenResponse.getExpiresIn());
                return tokenResponse;
            } else {
                throw new RuntimeException("Empty response from Zoho token refresh");
            }

        } catch (RestClientException e) {
            log.error("Failed to refresh Zoho token: {}", e.getMessage());
            throw new RuntimeException("Token refresh failed", e);
        }
    }
}