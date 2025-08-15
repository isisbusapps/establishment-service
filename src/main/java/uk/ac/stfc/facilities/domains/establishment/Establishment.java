package uk.ac.stfc.facilities.domains.establishment;

import java.time.Instant;

public class Establishment {
    private Long establishmentId;
    private String establishmentName;
    private String rorId;
    private String countryName;
    private String establishmentUrl;
    private Instant fromDate;
    private Instant thruDate;
    private Boolean verified;

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
    public String getEstablishmentName() {return establishmentName; }
    public void setEstablishmentName(String establishmentName) {this.establishmentName = establishmentName; }
    public String getRorId() {return rorId; }
    public void setRorId(String rorId) {this.rorId = rorId; }
    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
    public String getEstablishmentUrl() { return establishmentUrl; }
    public void setEstablishmentUrl(String establishmentUrl) {this.establishmentUrl = establishmentUrl; }
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
    public Boolean getVerified() {return verified; }
    public void setVerified(Boolean verified) {this.verified = verified; }
}
