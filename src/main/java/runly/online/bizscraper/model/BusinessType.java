package runly.online.bizscraper.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "business_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "types")
    private List<Business> businesses = new ArrayList<>();

    public BusinessType(String type) {
        this.name = type;
    }
}
