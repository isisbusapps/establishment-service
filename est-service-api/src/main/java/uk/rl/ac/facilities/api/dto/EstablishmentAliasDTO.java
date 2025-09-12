package uk.rl.ac.facilities.api.dto;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class EstablishmentAliasDTO {

    @Schema(
            description = "The unique identifier for the establishment alias",
            example = "1",
            required = true,
            readOnly = true
    )
    private Long aliasId;

    @Schema(
            description = "The ID of the establishment this alias belongs to",
            example = "10",
            required = true,
            nullable = false
    )
    private Long establishmentId;

    @Schema(
            description = "The alias name of the establishment",
            example = "STFC",
            required = true,
            nullable = false
    )
    private String alias;

    public Long getAliasId() {return aliasId;}

    public void setAliasId(Long aliasId) {this.aliasId = aliasId;}

    public Long getEstablishmentId() {return establishmentId;}

    public void setEstablishmentId(Long establishmentId) {this.establishmentId = establishmentId;}

    public String getAlias() {return alias;}

    public void setAlias(String alias) {this.alias = alias;}
}
