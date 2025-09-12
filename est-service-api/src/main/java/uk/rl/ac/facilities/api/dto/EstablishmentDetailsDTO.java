package uk.rl.ac.facilities.api.dto;

import java.util.List;

public class EstablishmentDetailsDTO {
    private EstablishmentDTO establishmentDto;
    private List<String> aliases;
    private List<String> categories;

    public EstablishmentDetailsDTO() {}

    public EstablishmentDetailsDTO(EstablishmentDTO establishmentDto, List<String> aliases, List<String> categories) {
        this.establishmentDto = establishmentDto;
        this.aliases = aliases;
        this.categories = categories;
    }

    public EstablishmentDTO getEstablishmentDto() {return establishmentDto;}

    public void setEstablishmentDto(EstablishmentDTO establishmentDto) {this.establishmentDto = establishmentDto;}

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
}
