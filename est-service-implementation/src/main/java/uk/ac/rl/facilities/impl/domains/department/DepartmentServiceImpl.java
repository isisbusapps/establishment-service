package uk.ac.rl.facilities.impl.domains.department;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.rl.facilities.impl.mappers.DepartmentMapper;
import uk.ac.rl.facilities.impl.mappers.LabelMapper;
import uk.rl.ac.facilities.api.domains.department.DepartmentModel;
import uk.rl.ac.facilities.api.domains.department.DepartmentService;
import uk.rl.ac.facilities.api.domains.department.LabelModel;
import uk.rl.ac.facilities.api.exceptions.EntityNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class DepartmentServiceImpl implements DepartmentService {

    public static final Set<String> DEPT_STOPWORDS = new HashSet<>(Arrays.asList(
            "department", "dept", "division", "office", "school", "faculty", "center", "centre",
            "institute", "facility", "research", "ltd", "plc", "undergraduate", "graduate",
            "of", "for", "and", "the", "a", "an", "in", "on", "with", "to", "by", "under", "from",
            "le", "an", "des", "et", "institut", "departamento", "di", "de", "du"
    ));
    public static final int DEPT_LABEL_CUTOFF = 80; // minimum similarity score (0-100) for a match
    public static final String FALLBACK_LABEL_NAME = "Other";

    private static final Logger LOGGER = LogManager.getLogger();

    private DepartmentRepository depRepo;
    private LabelRepository labelRepo;
    private LabelKeywordRepository labelKeywordRepo;
    private DepartmentLabelLinkRepository depLabelLinkRepo;
    private DepartmentMapper deptMapper;
    private LabelMapper labelMapper;

    public DepartmentServiceImpl() {}

    @Inject
    public DepartmentServiceImpl(DepartmentRepository depRepo,
                                 LabelRepository labelRepo,
                                 LabelKeywordRepository labelKeywordRepo,
                                 DepartmentLabelLinkRepository depLabelLinkRepo,
                                 DepartmentMapper deptMapper,
                                 LabelMapper labelMapper) {
        this.depRepo = depRepo;
        this.labelRepo = labelRepo;
        this.labelKeywordRepo = labelKeywordRepo;
        this.depLabelLinkRepo = depLabelLinkRepo;
        this.deptMapper = deptMapper;
        this.labelMapper = labelMapper;
    }

    @Override
    public DepartmentModel createDepartment(String name, Long establishmentId) {

        Department existingDep = depRepo.findByNameAndEstablishmentId(name, establishmentId);
        if (existingDep != null) {
            LOGGER.info("Department '{}' already exists for establishment {}. Returning existing department.", name, establishmentId);
            return deptMapper.toModel(existingDep);
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
        return deptMapper.toModel(depRepo.findByIdOptional(departmentId).orElseThrow(() -> new EntityNotFoundException("Department", departmentId)));
    }

    @Override
    public List<DepartmentModel> getDepartmentsByEstablishmentId(Long establishmentId) {
        return deptMapper.toModel(depRepo.list("establishmentId", establishmentId));
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void deleteDepartment(Long depId) throws NoResultException {
        depRepo.deleteById(depId);
        depRepo.flush();
    }

    @Override
    public DepartmentModel deleteDepartmentLabel(Long departmentId, Long labelId) {
        DepartmentLabelLinkId departmentLabelLinkId  = new DepartmentLabelLinkId(departmentId, labelId);
        DepartmentLabelLink link = depLabelLinkRepo.findByIdOptional(departmentLabelLinkId).orElseThrow(() -> new EntityNotFoundException("Label", labelId));
        depLabelLinkRepo.delete(link);
        depLabelLinkRepo.flush();
        return deptMapper.toModel(depRepo.findById(departmentId));
    }

    @Override
    public DepartmentModel deleteDepartmentLabel(Long departmentId) {
        Department department = depRepo.findByIdOptional(departmentId).orElseThrow(() -> new RuntimeException("No department found with id " + departmentId));
        department.getDepartmentLabelLinks().forEach(depLabelLink -> {
            depLabelLinkRepo.deleteById(depLabelLink.getId());
        });
        return deptMapper.toModel(depRepo.findById(departmentId));
    }

    @Override
    public DepartmentModel addFallbackLabelIfNeeded(Long departmentId) {
        Department department = depRepo.findById(departmentId);

        if (department == null) {
            LOGGER.warn("No fallback added since no department exists with id: {}", departmentId);
            throw new RuntimeException("No fallback added since no department exists");
        }

        long labelCount = depLabelLinkRepo.count("id.departmentId", departmentId);
        if (labelCount > 0) {
            LOGGER.warn("No fallback added since department already has labels");
            throw new RuntimeException("No fallback added since department already has labels");
        }

        Label fallback = labelRepo.getByName(FALLBACK_LABEL_NAME);
        LOGGER.info("No labels attached to department; adding fallback label '{}'", FALLBACK_LABEL_NAME);
        depLabelLinkRepo.persist(new DepartmentLabelLink(department, fallback));
        return deptMapper.toModel(department);
    }


    @Override
    public DepartmentModel addDepartmentLabelLinks(Long departmentId, List<Long> LabelIds) {
        Department department = depRepo.findByIdOptional(departmentId).orElseThrow(() -> new  EntityNotFoundException("No department found with department id: " + departmentId));

        Set<Label> labelsToAdd = LabelIds.stream()
                .map(id -> labelRepo.findByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Label not found for id: " + id)))
                .collect(Collectors.toSet());

        Set<Label> existingLabels = new HashSet<>(depLabelLinkRepo.findLabelsLinkedToDepartment(department.getDepartmentId()));

        Label other = labelRepo.getByName(FALLBACK_LABEL_NAME);

        labelsToAdd.removeAll(existingLabels);

        if (labelsToAdd.contains(other) && (!existingLabels.isEmpty() || labelsToAdd.size() > 1)) {
            LOGGER.info("Cannot have fallback label with other labels; will not add '{}'", FALLBACK_LABEL_NAME);
            labelsToAdd.remove(other);
        }

        Set<DepartmentLabelLink> depLabelLinksToAdd = labelsToAdd.stream()
                .map(label -> new DepartmentLabelLink(department, label)).collect(Collectors.toSet());

        if (!depLabelLinksToAdd.isEmpty()) {
            depLabelLinkRepo.persist(depLabelLinksToAdd);
            depLabelLinkRepo.flush();
            depRepo.getEntityManager().refresh(department);
        }

        if (existingLabels.contains(other) && (existingLabels.size() > 1 || !labelsToAdd.isEmpty())) {
            LOGGER.info("Cannot have fallback label with other labels; removing '{}'", FALLBACK_LABEL_NAME);
            this.deleteDepartmentLabel(department.getDepartmentId(), other.getLabelId());
        }
        return deptMapper.toModel(department);
    }

    @Override
    public DepartmentModel addDepartmentLabelLinksAutomatically(Long departmentId) {
        Department department = depRepo.findByIdOptional(departmentId).orElseThrow(() -> new  EntityNotFoundException("No department found with department id: " + departmentId));

        String cleanDepartmentName = cleanName(department.getDepartmentName());

        List<LabelKeyword> allLabelKeywords = labelKeywordRepo.listAll();
        List<LabelKeyword> matchedLabelKeywords = fuzzySearch(cleanDepartmentName, DEPT_LABEL_CUTOFF, allLabelKeywords);

        List<Long> matchedLabelIds = matchedLabelKeywords.stream()
                .map(LabelKeyword::getLabel)
                .map(Label::getLabelId)
                .distinct()
                .toList();

        Long otherId = labelRepo.getByName(FALLBACK_LABEL_NAME).getLabelId();

        return addDepartmentLabelLinks(departmentId, matchedLabelIds.isEmpty() ? List.of(otherId) : matchedLabelIds);
    }

    @Override
    public List<LabelModel> getLabelsForDepartment(Long departmentId) {
        return labelMapper.toModel(depLabelLinkRepo.findLabelsLinkedToDepartment(departmentId).stream().toList());
    }

    private List<LabelKeyword> fuzzySearch(String departmentName, Integer cutoff, List<LabelKeyword> labelKeywords) {
        List<LabelKeyword> matchedLabelKeywords = new ArrayList<>();

        for (LabelKeyword labelKeyword : labelKeywords) {
            int matchScore = FuzzySearch.weightedRatio(departmentName, labelKeyword.getKeyword(),String::toLowerCase);

            if (matchScore >= cutoff) {
                matchedLabelKeywords.add(labelKeyword);
            }
        }

        return matchedLabelKeywords;
    }

    private static String cleanName(String input) {
        return Arrays.stream(input.split("\\s+"))
                .filter(word -> !DEPT_STOPWORDS.contains(word.toLowerCase()))
                .collect(Collectors.joining(" "))
                .toLowerCase();
    }

}
