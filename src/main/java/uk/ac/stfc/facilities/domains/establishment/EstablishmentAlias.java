package uk.ac.stfc.facilities.domains.establishment;

import jakarta.persistence.*;

@Entity
@Table(name = "ESTABLISHMENT_ALIAS")
public class EstablishmentAlias {

    @Id
    @SequenceGenerator(name="ESTABLISHMENT_ALIAS_RID_SEQ", sequenceName="ESTABLISHMENT_ALIAS_RID_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTABLISHMENT_ALIAS_RID_SEQ")
    @Column(name = "ALIAS_ID")
    private Long aliasId;

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
        this.aliasId = aliasId;
        this.establishmentId = establishmentId;
        this.alias = alias;
    }

    public Long getRID() {return aliasId;}
    public void setRID(Long RID) {this.aliasId = aliasId;}
    public Long getEstablishmentId() {return establishmentId;}
    public void setEstablishmentId(Long establishmentId) {this.establishmentId = establishmentId;}
    public String getAlias() {return alias;}
    public void setAlias(String alias) {this.alias = alias;}
}
