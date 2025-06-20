package runly.online.bizscraper.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ideas")
public class Idea {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String title;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;
}
