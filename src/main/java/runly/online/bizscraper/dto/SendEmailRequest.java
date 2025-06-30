package runly.online.bizscraper.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
public class SendEmailRequest {
    private String fromAddress;
    private String toAddress;
    private String subject;
    private String body;
}
