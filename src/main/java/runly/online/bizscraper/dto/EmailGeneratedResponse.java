package runly.online.bizscraper.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailGeneratedResponse {
    private String email;
    private String subject;
    private String body;
}
