package uk.ac.stfc.facilities.domains.department;
import java.util.List;

public interface LabelRepository {
    List<Label> getAll();
    Label getByName(String name);
}
