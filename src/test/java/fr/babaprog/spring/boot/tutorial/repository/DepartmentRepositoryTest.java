package fr.babaprog.spring.boot.tutorial.repository;

import fr.babaprog.spring.boot.tutorial.entity.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DepartmentRepositoryTest {

    // https://mohosinmiah1610.medium.com/spring-boot-unit-testing-repository-layer-492bd004d417

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department;

    @BeforeEach
    public void setUp() {
        departmentRepository.deleteAll();
        department = Department.builder()
                .departmentName("Mechanical Engineering")
                .departmentCode("ME-011")
                .departmentAddress("Delhi")
                .build();
    }

    @Test
    public void givenDepartmentObj_whenSave_thenReturnSavedDepartment() {
        Department saveDepartment = departmentRepository.save(department);

        assertThat(saveDepartment).isNotNull();
        assertThat(saveDepartment.getDepartmentId()).isGreaterThan(0);
    }

    @Test
    public void shouldReturnAllDepartmentKnown() {
        Department department1 = Department.builder()
                .departmentName("Mechanical Engineering")
                .departmentCode("ME-011")
                .departmentAddress("Delhi")
                .build();

        Department department2 = Department.builder()
                .departmentName("Mechanical Engineering V2")
                .departmentCode("ME-012")
                .departmentAddress("Delhi")
                .build();

        departmentRepository.save(department1);
        departmentRepository.save(department2);

        List<Department> departments = departmentRepository.findAll();

        assertThat(departments).isNotEmpty();
        assertThat(departments.size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnDepartmentWhenIdIsKnown() {
        departmentRepository.save(department);

        Department getDepartment = departmentRepository.findById(department.getDepartmentId()).get();

        assertThat(getDepartment).isNotNull();
    }

    @Test
    public void shouldReturnDepartmentWhenNameIsFound() {
        departmentRepository.save(department);

        Department getDepartmentByName = departmentRepository.findByDepartmentName("Mechanical Engineering");

        assertThat(getDepartmentByName).isNotNull();
        assertThat(getDepartmentByName.getDepartmentName()).isEqualTo("Mechanical Engineering");
    }

    @Test
    public void shouldUpdateDepartment() {
        departmentRepository.save(department);

        Department getDepartment = departmentRepository.findById(department.getDepartmentId()).get();
        getDepartment.setDepartmentAddress("France");
        getDepartment.setDepartmentCode("CG-011");
        getDepartment.setDepartmentName("Car Engineering");

        Department updatedDepartment = departmentRepository.save(getDepartment);

        assertThat(updatedDepartment).isNotNull();
        assertThat(updatedDepartment.getDepartmentName()).isEqualTo("Car Engineering");
    }

    @Test
    public void shouldDeleteTheGivenDepartment() {
        departmentRepository.save(department);

        departmentRepository.deleteById(department.getDepartmentId());
        Optional<Department> deleteDepartment = departmentRepository.findById(department.getDepartmentId());

        assertThat(deleteDepartment).isEmpty();
    }

}