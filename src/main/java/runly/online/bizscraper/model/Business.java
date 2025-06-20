package runly.online.bizscraper.model;



import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "businesses")
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String url;
    private String email;
    private String status;
    private Boolean emailSent;
    private LocalDateTime sentAt;
    private String emailBody;
    private String country;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Idea> ideas = new ArrayList<>();

    @ManyToMany//(mappedBy = "businesses")
    @JoinTable(
            name = "business_business_type",
            joinColumns = @JoinColumn(name = "business_id"),
            inverseJoinColumns = @JoinColumn(name = "business_type_id")
    )
    private List<BusinessType> types;
}
