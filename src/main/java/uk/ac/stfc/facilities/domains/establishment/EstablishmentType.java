package uk.ac.stfc.facilities.domains.establishment;

import jakarta.persistence.*;

@Entity
@Table(name = "ESTABLISHMENT_TYPE")
public class EstablishmentType {

    @Id
    @SequenceGenerator(name="ESTABLISHMENT_TYPE_RID_SEQ", sequenceName="ESTABLISHMENT_TYPE_RID_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTABLISHMENT_TYPE_RID_SEQ")
    @Column(name = "EST_TYPE_ID")
    private Long estTypeId;

    @Column(name = "ESTABLISHMENT_ID", nullable = false)
    private Long establishmentId;

    @Column(name = "TYPE", nullable = false)
    private String type;

    public EstablishmentType() {
    }

    public EstablishmentType(Long establishmentId, String type) {
        this.establishmentId = establishmentId;
        this.type = type;
    }

    public EstablishmentType(Long RID, Long establishmentId, String type) {
        this.estTypeId = estTypeId;
        this.establishmentId = establishmentId;
        this.type = type;
    }

    public Long getRID() {return estTypeId;}
    public void setRID(Long RID) {this.estTypeId = estTypeId;}
    public Long getEstablishmentId() {return establishmentId;}
    public void setEstablishmentId(Long establishmentId) {this.establishmentId = establishmentId;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
}