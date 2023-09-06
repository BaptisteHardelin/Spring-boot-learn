package fr.babaprog.spring.boot.tutorial.service;

import fr.babaprog.spring.boot.tutorial.entity.Department;
import fr.babaprog.spring.boot.tutorial.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DepartmentImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> fetchDepartmentList() {
        return departmentRepository.findAll();
    }

    @Override
    public Department fetchDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId).get();
    }

    @Override
    public void deleteDepartmentById(Long departmentId) {
        departmentRepository.deleteById(departmentId);
    }

    @Override
    public Department updateDepartment(Long departmentId, Department department) {
        Department departmentDB = departmentRepository.findById(departmentId).get();

        if(Objects.nonNull(department.getDepartmentName()) && ! "".equalsIgnoreCase(department.getDepartmentName())) {
            departmentDB.setDepartmentName(department.getDepartmentName());
        }

        if(Objects.nonNull(department.getDepartmentCode()) && ! "".equalsIgnoreCase(department.getDepartmentCode())) {
            departmentDB.setDepartmentName(department.getDepartmentCode());
        }

        if(Objects.nonNull(department.getDepartmentAddress()) && ! "".equalsIgnoreCase(department.getDepartmentAddress())) {
            departmentDB.setDepartmentName(department.getDepartmentAddress());
        }

        return departmentRepository.save(departmentDB);
    }
}
