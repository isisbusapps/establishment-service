package uk.ac.stfc.facilities.domains.establishment;

public class CategoryRepositoryImpl implements CategoryRepository {
    @Override
    public Category getByName(String name) {
        return find("categoryName", name).firstResult();
    }
}
