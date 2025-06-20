package runly.online.bizscraper.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import runly.online.bizscraper.dto.Place;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "businesses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String websiteUrl;
    private String googleMapsUrl;
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

    public Business(Place place) {
        this.name = place.getDisplayName().get("text");
        this.websiteUrl = place.getWebsiteUri();
        this.googleMapsUrl = place.getGoogleMapsUri();
        this.status = "pending";
        this.emailSent = false;
        this.country = place.getAddressComponents().get(place.getAddressComponents().size()-2).getShortText();
    }
}
