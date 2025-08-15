package uk.ac.stfc.facilities.domains.department;

import java.util.ArrayList;
import java.util.List;


/**
 * In-memory stub implementation of {@link LabelRepository}.
 * Used for testing purposes only.
 */

public class LabelRepositoryStub implements LabelRepository {

    private List<Label> labels = new ArrayList<>();

    public LabelRepositoryStub() {
        labels.add(new Label(1L, "Physics"));
        labels.add(new Label(2L, "Chemistry"));
        labels.add(new Label(3L, "Mathematics"));
        labels.add(new Label(4L, "Biology"));
        labels.add(new Label(5L, "Computer Science"));
        labels.add(new Label(6L, "Engineering"));
        labels.add(new Label(7L, "Electrical Engineering"));
        labels.add(new Label(8L, "Mechanical Engineering"));
        labels.add(new Label(9L, "Civil Engineering"));
        labels.add(new Label(10L, "Psychology"));
        labels.add(new Label(11L, "Sociology"));
        labels.add(new Label(12L, "Anthropology"));
        labels.add(new Label(13L, "Political Science"));
        labels.add(new Label(14L, "Economics"));
        labels.add(new Label(15L, "Business"));
        labels.add(new Label(16L, "Law"));
        labels.add(new Label(17L, "Medicine"));
        labels.add(new Label(18L, "Public Health"));
        labels.add(new Label(19L, "Pharmacy"));
        labels.add(new Label(20L, "Nursing"));
        labels.add(new Label(21L, "Education"));
        labels.add(new Label(22L, "English"));
        labels.add(new Label(23L, "History"));
        labels.add(new Label(24L, "Philosophy"));
        labels.add(new Label(25L, "Linguistics"));
        labels.add(new Label(26L, "Environmental Science"));
        labels.add(new Label(27L, "Geography"));
        labels.add(new Label(28L, "Geology"));
        labels.add(new Label(29L, "Astronomy"));
        labels.add(new Label(30L, "Architecture"));
        labels.add(new Label(31L, "Fine Arts"));
        labels.add(new Label(32L, "Music"));
        labels.add(new Label(33L, "Statistics"));
        labels.add(new Label(34L, "Materials Science"));
        labels.add(new Label(35L, "Veterinary Science"));
        labels.add(new Label(36L, "Agricultural Science"));
        labels.add(new Label(37L, "Other"));  // fallback label
    }

    @Override
    public List<Label> getAll() {
        return labels;
    }

    @Override
    public Label getByName(String name) {
        return labels.stream()
                .filter(label -> label.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Label not found: " + name));
    }

}
