package uk.ac.rl.facilities.impl.domains.department;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface LabelRepository extends PanacheRepository<Label> {
    List<Label> getAll();
    Label getByName(String name);
}
