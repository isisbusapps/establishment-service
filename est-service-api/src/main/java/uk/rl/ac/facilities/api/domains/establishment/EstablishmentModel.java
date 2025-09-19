package uk.rl.ac.facilities.api.domains.establishment;

import java.net.URL;
import java.time.Instant;
import java.util.List;

public class EstablishmentModel {

    public EstablishmentModel() {
    }

    public EstablishmentModel(Long id, String rorID, String name, String country, URL url, Instant fromDate, Instant thruDate, Boolean verified) {
        this.id = id;
        this.rorID = rorID;
        this.name = name;
        this.country = country;
        this.url = url;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.verified = verified;
    }

    private Long id;

    private String rorID;

    private String name;

    private String country;

    private URL url;

    private Instant fromDate;

    private Instant thruDate;

    private Boolean verified;

    private List<CategoryModel> categories;

    private List<AliasModel> aliases;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRorID() {
        return rorID;
    }

    public void setRorID(String rorID) {
        this.rorID = rorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
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

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }

    public List<AliasModel> getAliases() {
        return aliases;
    }

    public void setAliases(List<AliasModel> aliases) {
        this.aliases = aliases;
    }
}
