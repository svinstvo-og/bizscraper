package runly.online.bizscraper.dto;


import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressComponent {
    private String longText;
    private String shortText;
    private List<String> types;
    private String languageCode;
}
