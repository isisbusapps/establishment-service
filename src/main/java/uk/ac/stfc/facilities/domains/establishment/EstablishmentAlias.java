package uk.ac.stfc.facilities.domains.establishment;

import jakarta.persistence.*;

@Entity
@Table(name = "ESTABLISHMENT_ALIAS")
public class EstablishmentAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RID")
    private Long rid;

    @Column(name = "ESTABLISHMENT_ID", nullable = false)
    private Long establishmentId;

    @Column(name = "ALIAS", nullable = false)
    private String alias;

    public EstablishmentAlias() {
    }

    public EstablishmentAlias(Long establishmentId, String alias) {
        this.establishmentId = establishmentId;
        this.alias = alias;
    }

    public EstablishmentAlias(Long RID, Long establishmentId, String alias) {
        this.rid = rid;
        this.establishmentId = establishmentId;
        this.alias = alias;
    }

    public Long getRID() {return rid;}
    public void setRID(Long RID) {this.rid = rid;}
    public Long getEstablishmentId() {return establishmentId;}
    public void setEstablishmentId(Long establishmentId) {this.establishmentId = establishmentId;}
    public String getAlias() {return alias;}
    public void setAlias(String alias) {this.alias = alias;}
}
