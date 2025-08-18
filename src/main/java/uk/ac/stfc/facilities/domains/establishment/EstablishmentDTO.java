package uk.ac.stfc.facilities.domains.establishment;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.time.ZonedDateTime;

public class EstablishmentDTO {
    @Schema(
            description = "The unique identifier for the establishment",
            example = "1",
            required = true,
            readOnly = true
    )
    private Long establishmentId;

    @Schema(
            description = "The name of the establishment",
            example = "Science and Technology Facilities Council",
            required = true,
            nullable = true
    )
    private String establishmentName;

    @Schema(
            description = "The Research Organization Registry (ROR) identifier of the establishment",
            example = "https://ror.org/057g20z61",
            nullable = true
    )
    private String rorId;

    @Schema(
            description = "The country where the establishment is located",
            example = "United Kingdom",
            required = true,
            nullable = true
    )
    private String countryName;

    @Schema(
            description = "The official website URL of the establishment",
            example = "https://www.ukri.org/councils/stfc/",
            nullable = true
    )
    private String establishmentUrl;

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
}
