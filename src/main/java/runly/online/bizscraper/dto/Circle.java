package runly.online.bizscraper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data
@AllArgsConstructor
@Getter
@Setter
public class Circle {
    private Map<String, Double> center;
    private Double radius;
}
