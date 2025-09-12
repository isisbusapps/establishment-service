package uk.ac.rl.facilities.impl.domains.establishment;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface CategoryRepository extends PanacheRepository<Category> {
    Category getByName(String name);
}
