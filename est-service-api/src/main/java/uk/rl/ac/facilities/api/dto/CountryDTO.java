package uk.rl.ac.facilities.api.dto;


import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class CountryDTO {

    @Schema(
            description = "The unique identifier for the country",
            example = "1",
            required = true,
            readOnly = true
    )
    private Long id;

    @Schema(
            description = "The name of the country",
            example = "United Kingdom",
            required = true,
            nullable = true
    )
    private String name;

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

}
