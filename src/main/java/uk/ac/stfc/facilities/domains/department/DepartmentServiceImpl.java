package uk.ac.stfc.facilities.domains.department;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

    public DepartmentServiceImpl() {}

    @Inject
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
    public boolean removeDepartmentLabel(Long departmentId, Long labelId) {

        DepartmentLabelId id = new DepartmentLabelId(departmentId, labelId);
        boolean removed = linkRepo.deleteById(id);

        if (!removed) {
            LOGGER.warn("Label with ID '{}' was not linked to department ID '{}'; no removal performed",
                    labelId, departmentId);
            return false;
        }

        Department department = depRepo.findById(departmentId);

        if (department == null) {
            LOGGER.warn("Deleted link was orphaned since no department exists with id: " + departmentId);
            return true;
        }

        long remainingLabels = linkRepo.count("id.departmentId", departmentId);
        if (remainingLabels == 0) {
            Label fallback = labelRepo.getByName(FALLBACK_LABEL_NAME);
            LOGGER.info("No labels remaining after removal; adding fallback label '{}'", FALLBACK_LABEL_NAME);
            linkRepo.persist(new DepartmentLabel(department, fallback));
        }

        return true;
    }

    @Override
    public List<DepartmentLabel> addDepartmentLabels(Long departmentId, List<Long> LabelIds) {

        Department department = depRepo.findById(departmentId);

        if (department == null) {
            LOGGER.warn("No department found with department id: " + departmentId);
            throw new NoResultException("No department found with department id: " + departmentId);
        }

        Set<Label> labelsToAdd = LabelIds.stream()
                .map(id -> {
                    Label label = labelRepo.findById(id);
                    if (label == null) {
                        throw new NoResultException("Label not found for id: " + id);
                    }
                    return label;
                })
                .collect(Collectors.toSet());

        Set<Label> existingLabels = new HashSet<>(linkRepo.findLabelsLinkedToDepartment(department.getDepartmentId()));

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
            removeDepartmentLabel(department.getDepartmentId(), other.getLabelId());
        }

        return linksToAdd;
    }

    @Override
    public List<DepartmentLabel> addDepartmentLabelsAutomatically(Long departmentId) {

        Department department = depRepo.findById(departmentId);

        if (department == null) {
            LOGGER.warn("No department found with department id: " + departmentId);
            throw new NoResultException("No department found with department id: " + departmentId);
        }

        String cleanDepartmentName = cleanName(department.getDepartmentName());
        List<Label> allLabels = labelRepo.getAll();
        List<Label> matchedLabels = fuzzySearch(cleanDepartmentName, DEPT_LABEL_CUTOFF, allLabels);

        List<Long> matchedLabelIds = matchedLabels.stream().map(Label::getLabelId).toList();
        Long otherId = labelRepo.getByName(FALLBACK_LABEL_NAME).getLabelId();

        return addDepartmentLabels(departmentId, matchedLabels.isEmpty() ? List.of(otherId) : matchedLabelIds);
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
