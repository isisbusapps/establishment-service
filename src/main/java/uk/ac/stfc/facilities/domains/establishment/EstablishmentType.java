package uk.ac.stfc.facilities.domains.establishment;

import jakarta.persistence.*;

@Entity
@Table(name = "ESTABLISHMENT_TYPE")
public class EstablishmentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RID")
    private Long rid;

    @Column(name = "ESTABLISHMENT_ID", nullable = false)
    private Long establishmentId;

    @Column(name = "ALIAS", nullable = false)
    private String type;

    public EstablishmentType() {
    }

    public EstablishmentType(Long establishmentId, String type) {
        this.establishmentId = establishmentId;
        this.type = type;
    }

    public EstablishmentType(Long RID, Long establishmentId, String type) {
        this.rid = rid;
        this.establishmentId = establishmentId;
        this.type = type;
    }

    public Long getRID() {return rid;}
    public void setRID(Long RID) {this.rid = rid;}
    public Long getEstablishmentId() {return establishmentId;}
    public void setEstablishmentId(Long establishmentId) {this.establishmentId = establishmentId;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
}