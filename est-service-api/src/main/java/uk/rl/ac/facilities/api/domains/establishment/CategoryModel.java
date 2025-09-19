package uk.rl.ac.facilities.api.domains.establishment;

import java.util.List;

public class CategoryModel {

    private Long id;

    private String name;

    private List<Long> establishmentIDs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getEstablishmentIDs() {
        return establishmentIDs;
    }

    public void setEstablishmentIDs(List<Long> establishmentIDs) {
        this.establishmentIDs = establishmentIDs;
    }
}
