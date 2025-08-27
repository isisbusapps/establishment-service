package uk.ac.stfc.facilities.domains.department;

import java.util.List;

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
