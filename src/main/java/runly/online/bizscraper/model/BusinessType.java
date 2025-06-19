package runly.online.bizscraper.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "business_types")
public class BusinessType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "businesses")
    private List<Business> businesses;
}
