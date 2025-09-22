package uk.ac.rl.facilities.impl.domains.establishment;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ESTABLISHMENT_NEW")
public class Establishment {

    @Id
    @SequenceGenerator(name="ESTABLISHMENT_NEW_RID_SEQ", sequenceName="ESTABLISHMENT_NEW_RID_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTABLISHMENT_NEW_RID_SEQ")
    @Column(name = "ID")
    private Long establishmentId;

    @Column(name = "ESTABLISHMENT_NAME", nullable = false)
    private String establishmentName;

    @Column(name = "ROR_ID")
    private String rorId;

    @Column(name = "COUNTRY_NAME")
    private String countryName;

    @Column(name = "ESTABLISHMENT_URL")
    private String establishmentUrl;

    @Column(name = "FROM_DATE")
    private Instant fromDate;

    @Column(name = "THRU_DATE")
    private Instant thruDate;

    @Column(name = "VERIFIED", nullable = false, columnDefinition = "NUMBER(1) DEFAULT 0")
    private Boolean verified;

    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<EstablishmentAlias> aliases = new ArrayList<>();

    @OneToMany(mappedBy = "establishment", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<EstablishmentCategoryLink> categoryLinks = new HashSet<>();

    public Establishment() {
    }

    public Establishment(String establishmentName) {
        this.establishmentName = establishmentName;
    }

    public Establishment(Long establishmentId, String establishmentName) {
        this.establishmentId = establishmentId;
        this.establishmentName = establishmentName;
    }

    public Establishment(Long establishmentId, String establishmentName, String rorId, String countryName, String establishmentUrl, Instant fromDate, Instant thruDate, Boolean verified) {
        this.establishmentId = establishmentId;
        this.establishmentName = establishmentName;
        this.rorId = rorId;
        this.countryName = countryName;
        this.establishmentUrl = establishmentUrl;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.verified = verified;
    }

    public Long getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(Long establishmentId) {
        this.establishmentId = establishmentId;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public void setEstablishmentName(String establishmentName) {
        this.establishmentName = establishmentName;
    }

    public String getRorId() {
        return rorId;
    }

    public void setRorId(String rorId) {
        this.rorId = rorId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getEstablishmentUrl() {
        return establishmentUrl;
    }

    public void setEstablishmentUrl(String establishmentUrl) {
        this.establishmentUrl = establishmentUrl;
    }

    public Instant getFromDate() {
        return fromDate;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getThruDate() {
        return thruDate;
    }

    public void setThruDate(Instant thruDate) {
        this.thruDate = thruDate;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public List<EstablishmentAlias> getAliases() {
        return aliases;
    }

    public void setAliases(List<EstablishmentAlias> aliases) {
        this.aliases = aliases;
    }

    public Set<EstablishmentCategoryLink> getCategoryLinks() {
        return categoryLinks;
    }

    public void setCategoryLinks(Set<EstablishmentCategoryLink> categoryLinks) {
        this.categoryLinks = categoryLinks;
    }
}
