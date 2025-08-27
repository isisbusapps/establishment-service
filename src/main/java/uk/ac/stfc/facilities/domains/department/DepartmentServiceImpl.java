package uk.ac.stfc.facilities.domains.department;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;
import java.util.stream.Collectors;

import static uk.ac.stfc.facilities.helpers.Constants.*;

@ApplicationScoped
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger LOGGER = LogManager.getLogger();

    private DepartmentRepository depRepo;
    private LabelRepository labelRepo;
    private DepartmentLabelRepository linkRepo;

    public DepartmentServiceImpl() {
    }

    public DepartmentServiceImpl(DepartmentRepository depRepo, LabelRepository labelRepo, DepartmentLabelRepository linkRepo) {
        this.depRepo = depRepo;
        this.labelRepo = labelRepo;
        this.linkRepo = linkRepo;
    }

    @Override
    public Department createDepartment(Department department) {
        return null;
    }

    @Override
    public List<Department> getAllDepartments() {
        return List.of();
    }

    @Override
    public Department getDepartmentsById(Long depId) {
        return null;
    }

    @Override
    public List<Department> getDepartmentsByEstablishmentId(Long establishmentId) {
        return List.of();
    }

    @Override
    public Department deleteDepartment(Long depId) throws NoResultException {
        return null;
    }

    @Override
    public boolean removeDepartmentLabel(Department department, Label labelToRemove) {
        try {
            List<Label> existingLabels = linkRepo.findLabelsLinkedToDepartment(department.getDepartmentId());

            if (!existingLabels.contains(labelToRemove)) {
                LOGGER.warn("Label '{}' was not not linked to department '{}'; no removal performed", labelToRemove.getName(), department.getDepartmentName());
                return false;
            }

            linkRepo.remove(new DepartmentLabel(department, labelToRemove));

            if (existingLabels.size() == 1) {
                Label other = labelRepo.getByName(FALLBACK_LABEL_NAME);
                LOGGER.info("No labels remaining after removal; adding fallback label '{}'", FALLBACK_LABEL_NAME);
                linkRepo.persist(new DepartmentLabel(department, other));
            }

            return true;

        } catch (Exception e) {
            LOGGER.error("Error removing labels linked to department: {}", department.getDepartmentName(), e);
            throw new RuntimeException("Failed to remove labels linked to department", e);
        }
    }

    @Override
    public List<DepartmentLabel> addDepartmentLabelsManually(Department department, List<Label> newLabels) {

        try {
            Set<Label> existingLabels = new HashSet<>(linkRepo.findLabelsLinkedToDepartment(department.getDepartmentId()));
            Set<Label> labelsToAdd = new HashSet<>(newLabels);
            Label other = labelRepo.getByName(FALLBACK_LABEL_NAME);

            labelsToAdd.removeAll(existingLabels);

            if (labelsToAdd.contains(other) && (!existingLabels.isEmpty() || labelsToAdd.size() > 1)) {
                LOGGER.info("Cannot have fallback label with other labels; will not add '{}'", FALLBACK_LABEL_NAME);
                labelsToAdd.remove(other);
            }

            List<DepartmentLabel> linksToAdd = labelsToAdd.stream()
                    .map(label -> new DepartmentLabel(department, label))
                    .toList();
            
            if (!linksToAdd.isEmpty()) {
                linkRepo.persist(linksToAdd);
            }

            if (existingLabels.contains(other) && (existingLabels.size() > 1 || !labelsToAdd.isEmpty())) {
                LOGGER.info("Cannot have fallback label with other labels; removing '{}'", FALLBACK_LABEL_NAME);
                removeDepartmentLabel(department, other);
            }

            return linksToAdd;

        } catch (Exception e) {
            LOGGER.error("Error adding labels to department: {}", department.getDepartmentName(), e);
            throw new RuntimeException("Failed to add labels to department", e);
        }
    }

    @Override
    public List<DepartmentLabel> addDepartmentLabelsAutomatically(Long departmentId) {

        Department department = depRepo.findById(departmentId);

        if (department == null) {
            LOGGER.warn("No department found with department id: " + departmentId);
            throw new NoResultException("No department found with department id: " + departmentId);
        }

        try {
            List<Label> linkedLabels = linkRepo.findLabelsLinkedToDepartment(department.getDepartmentId());

            if (!linkedLabels.isEmpty()) {
                throw new RuntimeException("Department already has labels added");
            }

            String cleanDepartmentName = cleanName(department.getDepartmentName());

            List<Label> allLabels = labelRepo.getAll();
            List<Label> matchedLabels = fuzzySearch(cleanDepartmentName, DEPT_LABEL_CUTOFF, allLabels);

            Label other = labelRepo.getByName(FALLBACK_LABEL_NAME);
            List<DepartmentLabel> linksToAdd = new ArrayList<>();

            if (matchedLabels.isEmpty()) {
                linksToAdd.add(new DepartmentLabel(department, other));
            } else {
                for (Label match : matchedLabels) {
                    linksToAdd.add(new DepartmentLabel(department, match));
                }
            }

            linkRepo.persist(linksToAdd);

            return linksToAdd;

        } catch (Exception e) {
            LOGGER.error("Error adding labels to department: {}", department.getDepartmentName(), e);
            throw new RuntimeException("Failed to add labels to department", e);
        }
    }

    private List<Label> fuzzySearch(String departmentName, Integer cutoff, List<Label> labels) {
        List<Label> matchedLabels = new ArrayList<>();

        for (Label label : labels) {
            int matchScore = FuzzySearch.partialRatio(departmentName, label.getName(),String::toLowerCase);

            if (matchScore >= cutoff) {
                matchedLabels.add(label);
            }
        }

        return matchedLabels;
    }

    private static String cleanName(String input) {
        return Arrays.stream(input.split("\\s+"))
                .filter(word -> !DEPT_STOPWORDS.contains(word.toLowerCase()))
                .collect(Collectors.joining(" "))
                .toLowerCase();
    }

}
