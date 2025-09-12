package uk.ac.rl.facilities.department;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.ac.rl.facilities.impl.domains.department.*;
import uk.ac.rl.facilities.impl.mappers.DepartmentMapper;
import uk.rl.ac.facilities.api.domains.department.DepartmentService;

import java.util.List;

import static org.mockito.Mockito.when;
import static uk.ac.rl.facilities.department.DepartmentTestConstants.mocKDEstId;
import static uk.ac.rl.facilities.department.DepartmentTestConstants.mocKDeptId;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository depRepo;

    @Mock
    private DepartmentLabelLinkRepository depLabelRepo;

    @Mock
    private DepartmentMapper deptMapper;

    private LabelRepository labelRepo;
    private DepartmentService service;

    @BeforeEach
    public void setUp() {
        labelRepo = new LabelRepositoryStub();
        service = new DepartmentServiceImpl(depRepo, labelRepo, depLabelRepo,  deptMapper);
    }


    @Test
    public void test_addLabelLinksAutomatically_LabelExpected_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "Department of Physics", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<String> addedDepLabelLinks = service.addDepartmentLabelLinksAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(labelRepo.getByName("Physics").getLabelName(), addedDepLabelLinks.getFirst(), "expected label name not found");
    }

    @Test
    public void test_addLabelLinksAutomatically_TwoLabelsExpected_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "Department of Physics and Biology", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<String> addedDepLabelLinks = service.addDepartmentLabelLinksAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(2, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertTrue(addedDepLabelLinks.contains(labelRepo.getByName("Biology").getLabelName()), "Biology label not found");
        Assertions.assertTrue(addedDepLabelLinks.contains(labelRepo.getByName("Physics").getLabelName()), "Physics label not found");
    }

    @Test
    void test_addLabelLinksAutomatically_NoLabelMatch_ReturnsOtherLabel() {
        Department dep = new Department(mocKDeptId, "Ministry of Magic", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<String> addedDepLabelLinks = service.addDepartmentLabelLinksAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(labelRepo.getByName("Other").getLabelName(), addedDepLabelLinks.getFirst(), "expected label name not found");
    }

    @Test
    public void test_addLabelLinksAutomatically_CaseInsensitive_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "DEPARTMENT OF COMPUTER SCIENCE", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<String> addedDepLabelLinks = service.addDepartmentLabelLinksAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(labelRepo.getByName("Computer Science").getLabelName(), addedDepLabelLinks.getFirst(), "expected label name not found");
    }

    @Test
    public void test_addLabelLinksAutomatically_TypoInDepartment_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "Physiacial Sciences", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<String> addedDepLabelLinks = service.addDepartmentLabelLinksAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(labelRepo.getByName("Physics").getLabelName(), addedDepLabelLinks.getFirst(), "expected label name not found");
    }

    @Test
    public void test_addLabelLinksAutomatically_ManyStopWordsInDepartment_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "Issaac Newton School and Department of Physiacial Sciences", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);
        
        List<String> addedDepLabelLinks = service.addDepartmentLabelLinksAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(labelRepo.getByName("Physics").getLabelName(), addedDepLabelLinks.getFirst(), "expected label name not found");
    }

    @Test
    public void test_addDepartmentLabelLinks_LabelLinksNotAlreadyLinked_AddsLinks()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        List<Label> existingLabels = List.of(labelRepo.getByName("Physics"));
        when(depLabelRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(existingLabels);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<String> addedDepLabelLinks = service.addDepartmentLabelLinks(dep.getDepartmentId(), List.of(labelRepo.getByName("Biology").getLabelId(), labelRepo.getByName("Chemistry").getLabelId()));

        Assertions.assertEquals(2, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertTrue(addedDepLabelLinks.contains(labelRepo.getByName("Biology").getLabelName()), "Biology label not found");
        Assertions.assertTrue(addedDepLabelLinks.contains(labelRepo.getByName("Chemistry").getLabelName()), "Chemistry label not found");
    }

    @Test
    public void test_addDepartmentLabelLinks_LabelAlreadyLinked_LinkNotAdded()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        List<Label> existingLabels = List.of(labelRepo.getByName("Physics"));
        when(depLabelRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(existingLabels);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<String> addedDepLabelLinks = service.addDepartmentLabelLinks(dep.getDepartmentId(), List.of(labelRepo.getByName("Physics").getLabelId(), labelRepo.getByName("Chemistry").getLabelId()));

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertFalse(addedDepLabelLinks.contains(labelRepo.getByName("Physics").getLabelName()), "Physics label should not be added again");
        Assertions.assertTrue(addedDepLabelLinks.contains(labelRepo.getByName("Chemistry").getLabelName()), "Chemistry label not found");
    }

    @Test
    public void test_addDepartmentLabelLinks_AddFallbackWhereExistingLabel_FallbackNotAdded()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        Label existingLabel = labelRepo.getByName("Biology");
        Label newLabel = labelRepo.getByName("Other");
        when(depLabelRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(List.of(existingLabel));
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<String> addedDepLabelLinks = service.addDepartmentLabelLinks(dep.getDepartmentId(), List.of(newLabel.getLabelId()));

        Assertions.assertEquals(0, addedDepLabelLinks.size(), "Unexpected number of labels added");

    }

    @Test
    public void test_addDepartmentLabelLinks_AddFallbackWhereNoExistingLabel_FallbackAdded()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        Label newLabel = labelRepo.getByName("Other");
        when(depLabelRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(List.of());
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<String> addedDepLabelLinks = service.addDepartmentLabelLinks(dep.getDepartmentId(), List.of(newLabel.getLabelId()));

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(labelRepo.getByName("Other").getLabelName(), addedDepLabelLinks.getFirst(), "expected label name not found");

    }

    @Test
    public void test_addDepartmentLabelLinks_AddDuplicateLabelLinks_DuplicatesNotAdded()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        List<Long> newLabelIds = List.of(labelRepo.getByName("Physics").getLabelId(),labelRepo.getByName("Physics").getLabelId());
        when(depLabelRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(List.of());
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<String> addedDepLabelLinks = service.addDepartmentLabelLinks(dep.getDepartmentId(), newLabelIds);

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(labelRepo.getByName("Physics").getLabelName(), addedDepLabelLinks.getFirst(), "expected label name not found");

    }

    }
