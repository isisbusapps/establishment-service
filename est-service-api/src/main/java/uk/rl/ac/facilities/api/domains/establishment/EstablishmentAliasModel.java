package uk.rl.ac.facilities.api.domains.establishment;

public class EstablishmentAliasModel {

    public EstablishmentAliasModel() {
    }

    public EstablishmentAliasModel(Long id, Long establishmentId, String alias) {
        this.id = id;
        this.establishmentId = establishmentId;
        this.alias = alias;
    }

    private Long id;

    private Long establishmentId;

    private String alias;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(Long establishmentId) {
        this.establishmentId = establishmentId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
