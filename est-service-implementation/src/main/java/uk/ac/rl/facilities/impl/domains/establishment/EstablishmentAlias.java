package uk.ac.rl.facilities.impl.domains.establishment;

import jakarta.persistence.*;

@Entity
@Table(name = "ESTABLISHMENT_ALIAS")
public class EstablishmentAlias {

    @Id
    @SequenceGenerator(name="ESTABLISHMENT_ALIAS_RID_SEQ", sequenceName="ESTABLISHMENT_ALIAS_RID_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTABLISHMENT_ALIAS_RID_SEQ")
    @Column(name = "ALIAS_ID")
    private Long aliasId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTABLISHMENT_ID", nullable = false)
    private Establishment establishment;

    @Column(name = "ALIAS", nullable = false)
    private String alias;

    public EstablishmentAlias() {
    }

    public EstablishmentAlias(Establishment establishment, String alias) {
        this.establishment = establishment;
        this.alias = alias;
    }

    public EstablishmentAlias(Long aliasId, Establishment establishment, String alias) {
        this.aliasId = aliasId;
        this.establishment = establishment;
        this.alias = alias;
    }

    public Long getAliasId() {
        return aliasId;
    }

    public void setAliasId(Long aliasId) {
        this.aliasId = aliasId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }
}
