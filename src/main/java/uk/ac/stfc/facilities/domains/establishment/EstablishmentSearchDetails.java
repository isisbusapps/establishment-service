package uk.ac.stfc.facilities.domains.establishment;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Holds information that each establishment should match during a search
 */
public class EstablishmentSearchDetails implements Serializable {
    @JsonProperty("orgName")
    private String organisationName;
    @JsonProperty("deptName")
    private String departmentName;

    public EstablishmentSearchDetails() {
    }

    public EstablishmentSearchDetails(String organisationName, String departmentName) {
        this.organisationName = organisationName;
        this.departmentName = departmentName;
    }
    
    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
