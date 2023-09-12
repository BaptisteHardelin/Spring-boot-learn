package fr.babaprog.spring.boot.tutorial.service;

import fr.babaprog.spring.boot.tutorial.entity.Department;
import fr.babaprog.spring.boot.tutorial.error.DepartmentNotFoundException;
import fr.babaprog.spring.boot.tutorial.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@SpringBootTest
class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @MockBean
    private DepartmentRepository mockDepartmentRepository;

    @BeforeEach
    void setUp() {
        Department department =
                Department.builder()
                        .departmentName("FR")
                        .departmentAddress("FRANCE")
                        .departmentCode("FR-01")
                        .departmentId(1L)
                        .build();
        Department department2 =
                Department.builder()
                        .departmentName("IT")
                        .departmentAddress("ITALIA")
                        .departmentCode("IT-02")
                        .departmentId(2L)
                        .build();

        List<Department> departmentsList = new ArrayList<>();
        departmentsList.add(department);
        departmentsList.add(department2);

        Mockito.when(mockDepartmentRepository.findByDepartmentNameIgnoreCase("FR")).thenReturn(department);
        Mockito.when(mockDepartmentRepository.save(any(Department.class))).thenReturn(department);
        Mockito.when(mockDepartmentRepository.findAll()).thenReturn(departmentsList);
        Mockito.when(mockDepartmentRepository.findById(1L)).thenReturn(Optional.ofNullable(department));
        Mockito.when(mockDepartmentRepository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void shouldReturnDepartmentByValidDepartmentName() {
        String departmentName = "FR";
        Department found = departmentService.fetchDepartmentByName(departmentName);

        assertEquals(departmentName, found.getDepartmentName());
    }

    @Test
    public void shouldSaveDepartment() {
        Department departmentToSave = Department.builder()
                .departmentName("FR")
                .departmentAddress("FRANCE")
                .departmentCode("FR-01")
                .departmentId(1L)
                .build();

        Department savedDepartment = departmentService.saveDepartment(departmentToSave);
        assertEquals(departmentToSave, savedDepartment);
    }

    @Test
    public void shouldReturnAllDepartments() {
        List<Department> allDepartments = departmentService.fetchDepartmentList();

        assertFalse(allDepartments.isEmpty());
        assertEquals(allDepartments.size(), 2);
        assertEquals(allDepartments.get(0).getDepartmentId(), 1);
        assertEquals(allDepartments.get(0).getDepartmentName(), "FR");
        assertEquals(allDepartments.get(0).getDepartmentAddress(), "FRANCE");
        assertEquals(allDepartments.get(0).getDepartmentCode(), "FR-01");

        assertEquals(allDepartments.get(1).getDepartmentId(), 2);
        assertEquals(allDepartments.get(1).getDepartmentName(), "IT");
        assertEquals(allDepartments.get(1).getDepartmentAddress(), "ITALIA");
        assertEquals(allDepartments.get(1).getDepartmentCode(), "IT-02");
    }

    @Test
    public void shouldReturnDepartmentById() throws DepartmentNotFoundException {
        Long departmentId = 1L;
        Department found = departmentService.fetchDepartmentById(departmentId);

        assertEquals(departmentId, found.getDepartmentId());

        Long departmentIdNotFound = 5L;
        Exception exception = assertThrows(DepartmentNotFoundException.class, () -> {
            departmentService.fetchDepartmentById(departmentIdNotFound);
        });

        String expectedMessage = "Department Not Available";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldDeleteDepartment() {
        Long departmentIdToDelete = 1L;

        departmentService.deleteDepartmentById(departmentIdToDelete);

        verify(mockDepartmentRepository).deleteById(departmentIdToDelete);
    }

    @Test
    public void shouldUpdateDepartment() {
        Long departmentIdToUpdate = 1L;

        Department updatedDepartment = Department.builder()
                .departmentName("Updated Name")
                .departmentCode("Updated Code")
                .departmentAddress("Updated Address")
                .build();

        Department existingDepartment = Department.builder()
                .departmentName("Old Name")
                .departmentCode("Old Code")
                .departmentAddress("Old Address")
                .build();

        Mockito.when(mockDepartmentRepository.findById(departmentIdToUpdate)).thenReturn(Optional.of(existingDepartment));
        Mockito.when(mockDepartmentRepository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Department updated = departmentService.updateDepartment(departmentIdToUpdate, updatedDepartment);

        Mockito.verify(mockDepartmentRepository).save(updatedDepartment);

        assertEquals(updatedDepartment, updated);
    }

}