package uk.ac.rl.facilities.impl.domains.department;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class LabelRepositoryImpl implements LabelRepository {
    @Override
    public List<Label> getAll() {
        return listAll();
    }

    @Override
    public Label getByName(String name) {
        return find("labelName", name).firstResult();
    }
}
