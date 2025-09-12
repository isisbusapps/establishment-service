package uk.ac.rl.facilities.impl.domains.department;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.rl.facilities.impl.mappers.DepartmentMapper;
import uk.rl.ac.facilities.api.domains.department.DepartmentModel;
import uk.rl.ac.facilities.api.domains.department.DepartmentService;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class DepartmentServiceImpl implements DepartmentService {

    public static final Set<String> DEPT_STOPWORDS = new HashSet<>(Arrays.asList(
            "department", "dept", "division", "office", "school", "faculty", "laboratory", "center", "centre",
            "institute", "facility", "research", "ltd", "plc",
            "of", "for", "and", "the", "a", "an", "in", "on", "with", "to", "by", "under", "from",
            "le", "an", "des", "et", "institut", "departamento", "di", "de", "du"
    ));
    public static final int DEPT_LABEL_CUTOFF = 80; // minimum similarity score (0-100) for a match
    public static final String FALLBACK_LABEL_NAME = "Other";

    private static final Logger LOGGER = LogManager.getLogger();

    private DepartmentRepository depRepo;
    private LabelRepository labelRepo;
    private DepartmentLabelLinkRepository depLabelLinkRepo;
    private DepartmentMapper deptMapper;

    public DepartmentServiceImpl() {}

    @Inject
    public DepartmentServiceImpl(DepartmentRepository depRepo, LabelRepository labelRepo, DepartmentLabelLinkRepository depLabelLinkRepo, DepartmentMapper deptMapper) {
        this.depRepo = depRepo;
        this.labelRepo = labelRepo;
        this.depLabelLinkRepo = depLabelLinkRepo;
        this.deptMapper = deptMapper;
    }

    @Override
    public DepartmentModel createDepartment(String name, Long establishmentId) {

        Department existingDep = depRepo.findByNameAndEstablishmentId(name, establishmentId);
        if (existingDep != null) {
            throw new IllegalArgumentException(
                    "Department with name '" + name + "' already exists for establishment with id + " + establishmentId
            );
        }

        Department dep = new Department(name, establishmentId);
        depRepo.persist(dep);
        return deptMapper.toModel(dep);
    }

    @Override
    public List<DepartmentModel> getAllDepartments() {
        return deptMapper.toModel(depRepo.listAll());
    }

    @Override
    public DepartmentModel getDepartment(Long departmentId) {
        return deptMapper.toModel(depRepo.findById(departmentId));
    }

    @Override
    public List<DepartmentModel> getDepartmentsByEstablishmentId(Long establishmentId) {
        return deptMapper.toModel(depRepo.list("establishmentId", establishmentId));
    }

    @Override
    public void deleteDepartment(Long depId) throws NoResultException {
        depLabelLinkRepo.delete("id.departmentId", depId);
        depRepo.deleteById(depId);
    }

    @Override
    public String getDepartmentLabelLink(Long departmentId, Long labelId) {
        DepartmentLabelLinkId id = new DepartmentLabelLinkId(departmentId, labelId);
        return depLabelLinkRepo.findById(id).getLabel().getLabelName();
    }

    @Override
    public void deleteDepartmentLabelLink(Long departmentId, Long labelId) {
        DepartmentLabelLinkId id = new DepartmentLabelLinkId(departmentId, labelId);
        depLabelLinkRepo.deleteById(id);
    }

    @Override
    public boolean addFallbackLabelIfNeeded(Long departmentId) {
        Department department = depRepo.findById(departmentId);

        if (department == null) {
            LOGGER.warn("No fallback added since no department exists with id: {}", departmentId);
            return false;
        }

        long labelCount = depLabelLinkRepo.count("id.departmentId", departmentId);
        if (labelCount > 0) {
            LOGGER.warn("No fallback added since department already has labels");
            return false;
        }

        Label fallback = labelRepo.getByName(FALLBACK_LABEL_NAME);
        LOGGER.info("No labels attached to department; adding fallback label '{}'", FALLBACK_LABEL_NAME);
        depLabelLinkRepo.persist(new DepartmentLabelLink(department, fallback));
        return true;
    }


    @Override
    public List<String> addDepartmentLabelLinks(Long departmentId, List<Long> LabelIds) {
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

        Set<Label> existingLabels = new HashSet<>(depLabelLinkRepo.findLabelsLinkedToDepartment(department.getDepartmentId()));

        Label other = labelRepo.getByName(FALLBACK_LABEL_NAME);

        labelsToAdd.removeAll(existingLabels);

        if (labelsToAdd.contains(other) && (!existingLabels.isEmpty() || labelsToAdd.size() > 1)) {
            LOGGER.info("Cannot have fallback label with other labels; will not add '{}'", FALLBACK_LABEL_NAME);
            labelsToAdd.remove(other);
        }

        List<DepartmentLabelLink> depLabelLinksToAdd = labelsToAdd.stream()
                .map(label -> new DepartmentLabelLink(department, label))
                .toList();

        if (!depLabelLinksToAdd.isEmpty()) {
            depLabelLinkRepo.persist(depLabelLinksToAdd);
        }

        if (existingLabels.contains(other) && (existingLabels.size() > 1 || !labelsToAdd.isEmpty())) {
            LOGGER.info("Cannot have fallback label with other labels; removing '{}'", FALLBACK_LABEL_NAME);
            this.deleteDepartmentLabelLink(department.getDepartmentId(), other.getLabelId());
        }

        return depLabelLinksToAdd.stream().map(DepartmentLabelLink::getLabel).map(Label::getLabelName).toList();
    }

    @Override
    public List<String> addDepartmentLabelLinksAutomatically(Long departmentId) {
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

        return addDepartmentLabelLinks(departmentId, matchedLabels.isEmpty() ? List.of(otherId) : matchedLabelIds);
    }

    @Override
    public List<String> getLabelsForDepartment(Long departmentId) {
        return depLabelLinkRepo.findLabelsLinkedToDepartment(departmentId).stream().map(Label::getLabelName).toList();
    }

    private List<Label> fuzzySearch(String departmentName, Integer cutoff, List<Label> labels) {
        List<Label> matchedLabels = new ArrayList<>();

        for (Label label : labels) {
            int matchScore = FuzzySearch.partialRatio(departmentName, label.getLabelName(),String::toLowerCase);

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
