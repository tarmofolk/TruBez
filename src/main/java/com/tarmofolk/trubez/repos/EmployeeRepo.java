package com.tarmofolk.trubez.repos;

import com.tarmofolk.trubez.domain.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// позволяет получить полный список полей или найти по идентификатору

public interface EmployeeRepo extends CrudRepository<Employee, Long> {
    List<Employee> findByFirm(String firm);
}
