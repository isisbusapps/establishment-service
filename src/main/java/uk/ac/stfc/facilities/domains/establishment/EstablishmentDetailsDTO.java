package uk.ac.stfc.facilities.domains.establishment;

import uk.ac.stfc.facilities.BaseClasses.Dto;

import java.util.List;

public class EstablishmentDetailsDTO implements Dto {
    private EstablishmentDTO establishmentDto;
    private List<EstablishmentAliasDTO> aliasDtos;
    private List<CategoryDTO> categoryDtos;

    public EstablishmentDetailsDTO() {}

    public EstablishmentDetailsDTO(EstablishmentDTO establishmentDto, List<EstablishmentAliasDTO> aliasDtos, List<CategoryDTO> categoryDtos) {
        this.establishmentDto = establishmentDto;
        this.aliasDtos = aliasDtos;
        this.categoryDtos = categoryDtos;
    }

    public EstablishmentDTO getEstablishmentDto() {return establishmentDto;}

    public void setEstablishmentDto(EstablishmentDTO establishmentDto) {this.establishmentDto = establishmentDto;}

    public List<EstablishmentAliasDTO> getAliasDtos() {return aliasDtos;}

    public void setAliasDtos(List<EstablishmentAliasDTO> aliasDtos) {this.aliasDtos = aliasDtos;}

    public List<CategoryDTO> getCategoryDtos() {return categoryDtos;}

    public void setCategoryDtos(List<CategoryDTO> categoryDtos) {this.categoryDtos = categoryDtos;}
}
