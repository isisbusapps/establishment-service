package uk.ac.stfc.facilities.domains.establishment;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryRepositoryImpl implements CategoryRepository {
    @Override
    public Category getByName(String name) {
        return find("categoryName", name).firstResult();
    }
}
