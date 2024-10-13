package com.thanhvv.employee.query.projection;

import com.thanhvv.employee.command.data.Employee;
import com.thanhvv.employee.command.data.EmployeeRepository;
import com.thanhvv.common.model.EmployeeResponseCommonModel;
import com.thanhvv.employee.query.queries.GetAllEmployeeQuery;
import com.thanhvv.common.queries.GetDetailEmployeeQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeProjection {
    @Autowired
    private EmployeeRepository employeeRepository;

    @QueryHandler
    public List<EmployeeResponseCommonModel> handle(GetAllEmployeeQuery query) {
        List<Employee> employees = employeeRepository.findAllByIsDisciplined(query.getIsDisciplined());
        return employees.stream().map(employee -> {
            EmployeeResponseCommonModel mode = new EmployeeResponseCommonModel();
            BeanUtils.copyProperties(employee, mode);
            return mode;
        }).toList();
    }

    @QueryHandler
    public EmployeeResponseCommonModel handle(GetDetailEmployeeQuery query) throws Exception {
        Employee employee = employeeRepository.findById(query.getId()).orElseThrow(() -> new Exception("Employee not found"));
        EmployeeResponseCommonModel mode = new EmployeeResponseCommonModel();
        BeanUtils.copyProperties(employee, mode);
        return mode;
    }
}
