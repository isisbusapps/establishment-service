package uk.rl.ac.facilities.api.dto;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.List;

public class EstablishmentDTO {
    @Schema(
            description = "The unique identifier for the establishment",
            example = "1",
            required = true,
            readOnly = true
    )
    private Long id;

    @Schema(
            description = "The name of the establishment",
            example = "Science and Technology Facilities Council",
            required = true,
            nullable = true
    )
    private String name;

    @Schema(
            description = "The Research Organization Registry (ROR) identifier of the establishment",
            example = "https://ror.org/057g20z61",
            nullable = true
    )
    private String rorID;

    @Schema(
            description = "The country where the establishment is located",
            example = "United Kingdom",
            required = true,
            nullable = true
    )
    private String country;

    @Schema(
            description = "The official website URL of the establishment",
            example = "https://www.ukri.org/councils/stfc/",
            nullable = true
    )
    private String url;

    @Schema(
            description = "The date the establishment record became active",
            example = "2021-01-01T00:00:00Z",
            readOnly = true,
            implementation = String.class,
            type = SchemaType.STRING,
            format = "date-time",
            nullable = true
    )
    private ZonedDateTime fromDate;

    @Schema(
            description = "The date the establishment record stopped being active",
            example = "2023-01-01T00:00:00Z",
            readOnly = true,
            implementation = String.class,
            type = SchemaType.STRING,
            format = "date-time",
            nullable = true
    )
    private ZonedDateTime thruDate;

    @Schema(
            description = "The verification status of the establishment",
            example = "true",
            nullable = true
    )
    private Boolean verified;

    private List<String> categories;

    private List<String> aliases;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRorID() {
        return rorID;
    }

    public void setRorID(String rorID) {
        this.rorID = rorID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ZonedDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public ZonedDateTime getThruDate() {
        return thruDate;
    }

    public void setThruDate(ZonedDateTime thruDate) {
        this.thruDate = thruDate;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> alises) {
        this.aliases = alises;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
