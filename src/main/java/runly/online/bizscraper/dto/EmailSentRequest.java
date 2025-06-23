package runly.online.bizscraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailSentRequest {
    Long id;
    @JsonProperty("email-body")
    String emailBody;
}
