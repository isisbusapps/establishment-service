package uk.ac.stfc.facilities.domains.department;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static uk.ac.stfc.facilities.domains.department.DepartmentTestConstants.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository depRepo;

    @Mock
    private  DepartmentLabelRepository linkRepo;

    private LabelRepository labelRepo;
    private DepartmentService service;

    @BeforeEach
    public void setUp() {
        labelRepo = new LabelRepositoryStub();
        service = new DepartmentServiceImpl(depRepo, labelRepo, linkRepo);
    }


    @Test
    public void test_addLabelsAutomatically_LabelExpected_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "Department of Physics", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabel> addedLinks = service.addDepartmentLabelsAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Physics").getLabelId(), addedLinks.getFirst().getLabelId(), "expected label Id not found");
    }

    @Test
    public void test_addLabelsAutomatically_TwoLabelsExpected_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "Department of Physics and Biology", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabel> addedLinks = service.addDepartmentLabelsAutomatically(dep.getDepartmentId());

        List<Long> addedLabelIds = addedLinks.stream().map(DepartmentLabel::getLabelId).toList();
        Assertions.assertEquals(2, addedLinks.size(), "Unexpected number of labels added");
        Assertions.assertTrue(addedLabelIds.contains(labelRepo.getByName("Biology").getLabelId()), "Biology label not found");
        Assertions.assertTrue(addedLabelIds.contains(labelRepo.getByName("Physics").getLabelId()), "Physics label not found");
    }

    @Test
    void test_addLabelsAutomatically_NoLabelMatch_ReturnsOtherLabel() {
        Department dep = new Department(mocKDeptId, "Ministry of Magic", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabel> addedLinks = service.addDepartmentLabelsAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Other").getLabelId(), addedLinks.getFirst().getLabelId(), "expected label Id not found");
    }

    @Test
    public void test_addLabelsAutomatically_CaseInsensitive_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "DEPARTMENT OF COMPUTER SCIENCE", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabel> addedLinks = service.addDepartmentLabelsAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Computer Science").getLabelId(), addedLinks.getFirst().getLabelId(), "expected label Id not found");
    }

    @Test
    public void test_addLabelsAutomatically_TypoInDepartment_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "Physiacial Sciences", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabel> addedLinks = service.addDepartmentLabelsAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Physics").getLabelId(), addedLinks.getFirst().getLabelId(), "expected label Id not found");
    }

    @Test
    public void test_addLabelsAutomatically_ManyStopWordsInDepartment_ReturnsDepartmentLabels() {
        Department dep = new Department(mocKDeptId, "Issaac Newton School and Department of Physiacial Sciences", mocKDEstId);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);
        
        List<DepartmentLabel> addedLinks = service.addDepartmentLabelsAutomatically(dep.getDepartmentId());

        Assertions.assertEquals(1, addedLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Physics").getLabelId(), addedLinks.getFirst().getLabelId(), "expected label Id not found");
    }

    @Test
    public void test_removeDepartmentLabel_MultipleLabelsLinked_RemovesLink() {
        Department dep = new Department(mocKDeptId, "Department of Physics", mocKDEstId);
        Label labelToRemove = labelRepo.getByName("Chemistry");
        List<Label> existingLabels = List.of(labelRepo.getByName("Physics"), labelRepo.getByName("Chemistry"));
        when(linkRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(existingLabels);

        boolean result = service.removeDepartmentLabel(dep.getDepartmentId(), labelToRemove.getLabelId());

        Assertions.assertTrue(result);
    }

    @Test
    public void test_removeDepartmentLabel_OnlyLabel_AddsFallback() {
        Department dep = new Department(mocKDeptId, "Ministry of Magic", mocKDEstId);
        Label labelToRemove = labelRepo.getByName("Physics");
        List<Label> existingLabels = List.of(labelRepo.getByName("Physics"));
        when(linkRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(existingLabels);

        boolean result = service.removeDepartmentLabel(dep.getDepartmentId(), labelToRemove.getLabelId());

        Assertions.assertTrue(result);
        Label other = labelRepo.getByName("Other");
        verify(linkRepo).persist(argThat((DepartmentLabel departmentLabel) ->
                departmentLabel.getDepartmentId().equals(mocKDeptId) &&
                        departmentLabel.getLabelId().equals(other.getLabelId())
        ));
    }

    @Test
    public void test_removeDepartmentLabel_LabelNotLinked_NoRemoval() {
        Department dep = new Department(mocKDeptId, "Department of Physics", mocKDEstId);
        Label labelToRemove = labelRepo.getByName("Biology");
        List<Label> existingLabels = List.of(labelRepo.getByName("Physics"));
        when(linkRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(existingLabels);

        boolean result = service.removeDepartmentLabel(dep.getDepartmentId(), labelToRemove.getLabelId());

        Assertions.assertFalse(result);
    }

    @Test
    public void test_addDepartmentLabels_LabelsNotAlreadyLinked_AddsLinks()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        List<Label> existingLabels = List.of(labelRepo.getByName("Physics"));
        when(linkRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(existingLabels);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabel> addedLinks = service.addDepartmentLabels(dep.getDepartmentId(), List.of(labelRepo.getByName("Biology").getLabelId(), labelRepo.getByName("Chemistry").getLabelId()));

        List<Long> addedLabelIds = addedLinks.stream().map(DepartmentLabel::getLabelId).toList();
        Assertions.assertEquals(2, addedLinks.size(), "Unexpected number of labels added");
        Assertions.assertTrue(addedLabelIds.contains(labelRepo.getByName("Biology").getLabelId()), "Biology label not found");
        Assertions.assertTrue(addedLabelIds.contains(labelRepo.getByName("Chemistry").getLabelId()), "Chemistry label not found");
    }

    @Test
    public void test_addDepartmentLabels_LabelAlreadyLinked_LinkNotAdded()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        List<Label> existingLabels = List.of(labelRepo.getByName("Physics"));
        when(linkRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(existingLabels);
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabel> addedLinks = service.addDepartmentLabels(dep.getDepartmentId(), List.of(labelRepo.getByName("Physics").getLabelId(), labelRepo.getByName("Chemistry").getLabelId()));

        List<Long> addedLabelIds = addedLinks.stream().map(DepartmentLabel::getLabelId).toList();
        Assertions.assertEquals(1, addedLinks.size(), "Unexpected number of labels added");
        Assertions.assertFalse(addedLabelIds.contains(labelRepo.getByName("Physics").getLabelId()), "Physics label should not be added again");
        Assertions.assertTrue(addedLabelIds.contains(labelRepo.getByName("Chemistry").getLabelId()), "Chemistry label not found");
    }

    @Test
    public void test_addDepartmentLabels_FallbackLabelAlreadyLinked_RemovesFallback()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        Label existingLabel = labelRepo.getByName("Other");
        Label newLabel = labelRepo.getByName("Physics");
        when(linkRepo.findLabelsLinkedToDepartment(mocKDeptId))
                .thenReturn(List.of(existingLabel))
                .thenReturn(List.of(existingLabel, newLabel));
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabel> addedLinks = service.addDepartmentLabels(dep.getDepartmentId(), List.of(newLabel.getLabelId()));

        Assertions.assertEquals(1, addedLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Physics").getLabelId(), addedLinks.getFirst().getLabelId(), "expected label Id not found");
        Label other = labelRepo.getByName("Other");
        verify(linkRepo).remove(argThat(departmentLabel ->
                departmentLabel.getDepartmentId().equals(mocKDeptId) &&
                        departmentLabel.getLabelId().equals(other.getLabelId())));

    }

    @Test
    public void test_addDepartmentLabels_AddFallbackWhereExistingLabel_FallbackNotAdded()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        Label existingLabel = labelRepo.getByName("Biology");
        Label newLabel = labelRepo.getByName("Other");
        when(linkRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(List.of(existingLabel));
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabel> addedLinks = service.addDepartmentLabels(dep.getDepartmentId(), List.of(newLabel.getLabelId()));

        Assertions.assertEquals(0, addedLinks.size(), "Unexpected number of labels added");

    }

    @Test
    public void test_addDepartmentLabels_AddFallbackWhereNoExistingLabel_FallbackAdded()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        Label newLabel = labelRepo.getByName("Other");
        when(linkRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(List.of());
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabel> addedLinks = service.addDepartmentLabels(dep.getDepartmentId(), List.of(newLabel.getLabelId()));

        Assertions.assertEquals(1, addedLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Other").getLabelId(), addedLinks.getFirst().getLabelId(), "expected label Id not found");

    }

    @Test
    public void test_addDepartmentLabels_AddDuplicateLabels_DuplicatesNotAdded()  {
        Department dep = new Department(mocKDeptId, "ISIS", mocKDEstId);
        List<Long> newLabelIds = List.of(labelRepo.getByName("Physics").getLabelId(),labelRepo.getByName("Physics").getLabelId());
        when(linkRepo.findLabelsLinkedToDepartment(mocKDeptId)).thenReturn(List.of());
        when(depRepo.findById(dep.getDepartmentId())).thenReturn(dep);

        List<DepartmentLabel> addedLinks = service.addDepartmentLabels(dep.getDepartmentId(), newLabelIds);

        Assertions.assertEquals(1, addedLinks.size(), "Unexpected number of labels added");
        Assertions.assertEquals(mocKDeptId, addedLinks.getFirst().getDepartmentId(), "expected department Id not found");
        Assertions.assertEquals(labelRepo.getByName("Physics").getLabelId(), addedLinks.getFirst().getLabelId(), "expected label Id not found");

    }

    }
