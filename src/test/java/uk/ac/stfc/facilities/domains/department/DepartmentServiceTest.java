package uk.ac.stfc.facilities.domains.department;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static uk.ac.stfc.facilities.domains.department.DepartmentTestConstants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository depRepo;

    @Mock
    private DepartmentLabelLinkRepository depLabelRepo;

    private LabelRepository labelRepo;
    private DepartmentService service;

    @BeforeEach
    public void setUp() {
        labelRepo = new LabelRepositoryStub();
        service = new DepartmentServiceImpl(depRepo, labelRepo, depLabelRepo);
    }


    @Test
    public void test_addLabelLinksAutomatically_LabelExpected_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "Department of Physics", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabelLink> addedDepLabelLinks = service.addDepartmentLabelLinksAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedDepLabelLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Physics").getLabelId(), addedDepLabelLinks.getFirst().getLabelId(), "expected label Id not found");
    }

    @Test
    public void test_addLabelLinksAutomatically_TwoLabelsExpected_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "Department of Physics and Biology", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabelLink> addedDepLabelLinks = service.addDepartmentLabelLinksAutomatically(dep.getDepartmentId());

        List<Long> addedLabelIds = addedDepLabelLinks.stream().map(DepartmentLabelLink::getLabelId).toList();
        Assertions.assertEquals(2, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertTrue(addedLabelIds.contains(labelRepo.getByName("Biology").getLabelId()), "Biology label not found");
        Assertions.assertTrue(addedLabelIds.contains(labelRepo.getByName("Physics").getLabelId()), "Physics label not found");
    }

    @Test
    void test_addLabelLinksAutomatically_NoLabelMatch_ReturnsOtherLabel() {
        Department dep = new Department(mocKDeptId, "Ministry of Magic", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabelLink> addedDepLabelLinks = service.addDepartmentLabelLinksAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedDepLabelLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Other").getLabelId(), addedDepLabelLinks.getFirst().getLabelId(), "expected label Id not found");
    }

    @Test
    public void test_addLabelLinksAutomatically_CaseInsensitive_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "DEPARTMENT OF COMPUTER SCIENCE", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabelLink> addedDepLabelLinks = service.addDepartmentLabelLinksAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedDepLabelLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Computer Science").getLabelId(), addedDepLabelLinks.getFirst().getLabelId(), "expected label Id not found");
    }

    @Test
    public void test_addLabelLinksAutomatically_TypoInDepartment_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "Physiacial Sciences", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabelLink> addedDepLabelLinks = service.addDepartmentLabelLinksAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedDepLabelLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Physics").getLabelId(), addedDepLabelLinks.getFirst().getLabelId(), "expected label Id not found");
    }

    @Test
    public void test_addLabelLinksAutomatically_ManyStopWordsInDepartment_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "Issaac Newton School and Department of Physiacial Sciences", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);
        
        List<DepartmentLabelLink> addedDepLabelLinks = service.addDepartmentLabelLinksAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedDepLabelLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Physics").getLabelId(), addedDepLabelLinks.getFirst().getLabelId(), "expected label Id not found");
    }

    @Test
    public void test_addDepartmentLabelLinks_LabelLinksNotAlreadyLinked_AddsLinks()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        List<Label> existingLabels = List.of(labelRepo.getByName("Physics"));
        when(depLabelRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(existingLabels);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabelLink> addedDepLabelLinks = service.addDepartmentLabelLinks(dep.getDepartmentId(), List.of(labelRepo.getByName("Biology").getLabelId(), labelRepo.getByName("Chemistry").getLabelId()));

        List<Long> addedLabelIds = addedDepLabelLinks.stream().map(DepartmentLabelLink::getLabelId).toList();
        Assertions.assertEquals(2, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertTrue(addedLabelIds.contains(labelRepo.getByName("Biology").getLabelId()), "Biology label not found");
        Assertions.assertTrue(addedLabelIds.contains(labelRepo.getByName("Chemistry").getLabelId()), "Chemistry label not found");
    }

    @Test
    public void test_addDepartmentLabelLinks_LabelAlreadyLinked_LinkNotAdded()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        List<Label> existingLabels = List.of(labelRepo.getByName("Physics"));
        when(depLabelRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(existingLabels);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabelLink> addedDepLabelLinks = service.addDepartmentLabelLinks(dep.getDepartmentId(), List.of(labelRepo.getByName("Physics").getLabelId(), labelRepo.getByName("Chemistry").getLabelId()));

        List<Long> addedLabelIds = addedDepLabelLinks.stream().map(DepartmentLabelLink::getLabelId).toList();
        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertFalse(addedLabelIds.contains(labelRepo.getByName("Physics").getLabelId()), "Physics label should not be added again");
        Assertions.assertTrue(addedLabelIds.contains(labelRepo.getByName("Chemistry").getLabelId()), "Chemistry label not found");
    }

    @Test
    public void test_addDepartmentLabelLinks_AddFallbackWhereExistingLabel_FallbackNotAdded()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        Label existingLabel = labelRepo.getByName("Biology");
        Label newLabel = labelRepo.getByName("Other");
        when(depLabelRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(List.of(existingLabel));
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabelLink> addedDepLabelLinks = service.addDepartmentLabelLinks(dep.getDepartmentId(), List.of(newLabel.getLabelId()));

        Assertions.assertEquals(0, addedDepLabelLinks.size(), "Unexpected number of labels added");

    }

    @Test
    public void test_addDepartmentLabelLinks_AddFallbackWhereNoExistingLabel_FallbackAdded()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        Label newLabel = labelRepo.getByName("Other");
        when(depLabelRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(List.of());
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabelLink> addedDepLabelLinks = service.addDepartmentLabelLinks(dep.getDepartmentId(), List.of(newLabel.getLabelId()));

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedDepLabelLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Other").getLabelId(), addedDepLabelLinks.getFirst().getLabelId(), "expected label Id not found");

    }

    @Test
    public void test_addDepartmentLabelLinks_AddDuplicateLabelLinks_DuplicatesNotAdded()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        List<Long> newLabelIds = List.of(labelRepo.getByName("Physics").getLabelId(),labelRepo.getByName("Physics").getLabelId());
        when(depLabelRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(List.of());
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabelLink> addedDepLabelLinks = service.addDepartmentLabelLinks(dep.getDepartmentId(), newLabelIds);

        Assertions.assertEquals(1, addedDepLabelLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedDepLabelLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Physics").getLabelId(), addedDepLabelLinks.getFirst().getLabelId(), "expected label Id not found");

    }

    }
