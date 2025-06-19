package runly.online.bizscraper.model;



import jakarta.persistence.*;

import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "businesses")
    private List<Idea> ideas;

    @ManyToMany(mappedBy = "businesses")
    private List<BusinessType> types;
}
