package uk.ac.stfc.facilities.domains.establishment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stfc.UserOffice.BaseClasses.Dto;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;

/**
 * Holds all information related to an Establishment and Address for use with the Authentication/Manage Details application
 */
@Schema(description = "Please be aware values of fromDate, thruDate always null")
public class EstablishmentDTO implements Dto {

    @Expose
    @SerializedName("id")
    @Schema(description = "The unique identifier for the establishment", example = "1", required = true,
        readOnly = true)
    private Long establishmentId;

    @Expose
    @SerializedName("name")
    @Schema(description = "The name of the establishment", example = "Science and Technology Facilities Council",
        required = true, nullable = true)
    private String orgName;
    @Schema(description = "The acronym of the establishment", example = "STFC",
         nullable = true)
    private String orgAcronym;
    @Expose
    @SerializedName("dept")
    @Schema(description = "The name of the department", example = "Scientific Computing Department",
        required = true, nullable = true)
    private String deptName;
    @Schema(description = "The acronym of the department", example = "SCD",
         nullable = true)
    private String deptAcronym;
    @Schema(description = "The site of the establishment", example = "RAL",
         nullable = true)
    private String site;
    @Schema(description = "PO Box", example = "PO Box 123",
         nullable = true)
    private String poBox;
    @Schema(description = "Building number", example = "1",
         nullable = true)
    private String buildingNumber;
    @Schema(description = "Building name", example = "Building one",
         nullable = true)
    private String buildingName;
    @Schema(description = "Street", example = "Road 1",
         nullable = true)
    private String street;
    @Schema(description = "District", example = "District 1",
         nullable = true)
    private String district;
    @Schema(description = "City or town", example = "Didcot",
         nullable = true)
    private String cityTown;
    @Schema(description = "County, province or state", example = "Oxfordshire",
         nullable = true)
    private String countyProvinceState;
    @Schema(description = "Postal code", example = "OX11 0QX",
         nullable = true)
    private String postalCode;
    @Schema(description = "Country", example = "United Kingdom",
        required = true, nullable = true)
    private String country;
    @Schema(description = "The id of the postal address record that is associated with this establishment", example = "1",
         nullable = true)
    private Long postalAddressId;
    @Schema(description = "The date the address record became active", example = "2021-01-01T00:00:00Z",
        readOnly = true, implementation = String.class, type = SchemaType.STRING, format = "date-time", nullable = true)
    private ZonedDateTime fromDate;
    @Schema(description = "The date the address record stopped being active", example = "2021-01-01T00:00:00Z",
        readOnly = true, implementation = String.class, type = SchemaType.STRING, format = "date-time", nullable = true)
    private ZonedDateTime thruDate;
    @Schema(description = "The verification status of the establishment", enumeration = {"Yes", "No"},
         nullable = true)
    private String verified;

    public Long getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(Long establishmentId) {
        this.establishmentId = establishmentId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptAcronym() {
        return deptAcronym;
    }

    public void setDeptAcronym(String deptAcronym) {
        this.deptAcronym = deptAcronym;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getPoBox() {
        return poBox;
    }

    public void setPoBox(String poBox) {
        this.poBox = poBox;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCityTown() {
        return cityTown;
    }

    public void setCityTown(String cityTown) {
        this.cityTown = cityTown;
    }

    public String getCountyProvinceState() {
        return countyProvinceState;
    }

    public void setCountyProvinceState(String countyProvinceState) {
        this.countyProvinceState = countyProvinceState;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOrgAcronym() {
        return orgAcronym;
    }

    public void setOrgAcronym(String orgAcronym) {
        this.orgAcronym = orgAcronym;
    }

    public Long getPostalAddressId() { return postalAddressId; }

    public void setPostalAddressId(Long postalAddressId) { this.postalAddressId = postalAddressId; }

    public ZonedDateTime getFromDate() { return fromDate; }

    public void setFromDate(ZonedDateTime fromDate) { this.fromDate = fromDate; }

    public ZonedDateTime getThruDate() { return thruDate; }

    public void setThruDate(ZonedDateTime thruDate) { this.thruDate = thruDate; }

    public String getVerified() { return verified; }

    public void setVerified(String verified) { this.verified = verified; }
}
