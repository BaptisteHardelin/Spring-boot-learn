package fr.babaprog.spring.boot.tutorial.controller;

import fr.babaprog.spring.boot.tutorial.entity.Department;
import fr.babaprog.spring.boot.tutorial.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService mockDepartmentService;

    private Department department;

    @BeforeEach
    void setUp() {
        department = Department.builder()
                .departmentName("Mechanical Engineering")
                .departmentAddress("France")
                .departmentCode("FR-01")
                .departmentId(1L)
                .build();
    }

    @Test
    public void saveDepartment() throws Exception {
        Department inputDepartment = Department.builder()
                .departmentName("Mechanical Engineering")
                .departmentAddress("France")
                .departmentCode("FR-01")
                .build();

        Mockito.when(mockDepartmentService.saveDepartment(inputDepartment)).thenReturn(department);

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "departmentName": "Mechanical Engineering",
                                    "departmentAddress": "France",
                                    "departmentCode": "FR-01"
                                }"""))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void fetchDepartmentList() throws Exception {
        Department department2 = Department.builder()
                .departmentName("Mechanical Engineering V2")
                .departmentAddress("France")
                .departmentCode("FR-02")
                .build();

        List<Department> departments = new ArrayList<>();
        departments.add(department);
        departments.add(department2);

        Mockito.when(mockDepartmentService.fetchDepartmentList()).thenReturn(departments);

        mockMvc.perform(get("/departments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void fetchDepartmentById() throws Exception {
        Mockito.when(mockDepartmentService.fetchDepartmentById(1L)).thenReturn(department);

        mockMvc.perform(get("/departments/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.departmentName").value(department.getDepartmentName()))
                .andExpect(jsonPath("$.departmentAddress").value(department.getDepartmentAddress()))
                .andExpect(jsonPath("$.departmentCode").value(department.getDepartmentCode()));
    }

    @Test
    public void deleteDepartment() throws Exception {
        Mockito.doNothing().when(mockDepartmentService).deleteDepartmentById(1L);

        mockMvc.perform(delete("/departments/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(mockDepartmentService, Mockito.times(1)).deleteDepartmentById(1L);
    }

    @Test
    public void updateDepartment() throws Exception {
        Department updatedDepartment = Department.builder()
                .departmentId(1L)
                .departmentName("Cars Engineering")
                .departmentAddress("France")
                .departmentCode("FR-02")
                .build();

        Mockito.when(mockDepartmentService.updateDepartment(1L, updatedDepartment))
                .thenReturn(updatedDepartment);

        mockMvc.perform(put("/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "departmentId": 1,
                                    "departmentName": "Cars Engineering",
                                    "departmentAddress": "France",
                                    "departmentCode": "FR-02"
                                }"""))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(mockDepartmentService, Mockito.times(1)).updateDepartment(1L, updatedDepartment);
    }

    @Test
    public void fetchDepartmentByName() throws Exception {
        Mockito.when(mockDepartmentService.fetchDepartmentByName("Mechanical Engineering")).thenReturn(department);

        mockMvc.perform(get("/departments/name/Mechanical Engineering").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.departmentName").value(department.getDepartmentName()))
                .andExpect(jsonPath("$.departmentAddress").value(department.getDepartmentAddress()))
                .andExpect(jsonPath("$.departmentCode").value(department.getDepartmentCode()));
    }
}
