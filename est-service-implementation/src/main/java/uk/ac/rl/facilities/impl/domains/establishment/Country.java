package uk.ac.rl.facilities.impl.domains.establishment;

import jakarta.persistence.*;

@Entity
@Table(name = "COUNTRY_NEW")
public class Country {

    @Id
    @SequenceGenerator(name = "COUNTRY_RID_SEQ", sequenceName = "COUNTRY_RID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COUNTRY_RID_SEQ")
    @Column(name = "ID")
    private Long countryId;

    @Column(name = "COUNTRY_NAME")
    private String countryName;

    public Country() {
    }

    public Country(Long countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    public Long getCountryId() {return countryId;}

    public void setCountryId(Long countryId) {this.countryId = countryId;}

    public String getCountryName() {return countryName;}

    public void setCountryName(String countryName) {this.countryName = countryName;}
}