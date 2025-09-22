package uk.rl.ac.facilities.api.domains.establishment;

public class AliasModel {

    private Long id;

    private String alias;

    private Long establishmentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Long getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(Long establishmentId) {
        this.establishmentId = establishmentId;
    }
}
