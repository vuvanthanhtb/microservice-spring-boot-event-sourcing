package com.thanhvv.employee.query.controller;

import com.thanhvv.common.model.EmployeeResponseCommonModel;
import com.thanhvv.employee.query.queries.GetAllEmployeeQuery;
import com.thanhvv.common.queries.GetDetailEmployeeQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee Query")
@Slf4j
public class EmployeeQueryController {
    @Autowired
    private QueryGateway queryGateway;

    @Operation(
            method = "GET",
            summary = "Get list employee",
            description = "Get endpoint for employee with filter",
            responses = {
                    @ApiResponse(
                            description = "success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized/Invalid token"
                    )
            }
    )
    @GetMapping
    public List<EmployeeResponseCommonModel> getAllEmployees(@RequestParam Boolean isDisciplined) {
        log.info("Calling to getAllEmployees");
        return queryGateway.query(
                new GetAllEmployeeQuery(isDisciplined),
                ResponseTypes.multipleInstancesOf(EmployeeResponseCommonModel.class)
        ).join();
    }

    @GetMapping("/{employeeId}")
    public EmployeeResponseCommonModel getEmployeeDetail(@PathVariable String employeeId) {
        return queryGateway.query(
                new GetDetailEmployeeQuery(employeeId),
                ResponseTypes.instanceOf(EmployeeResponseCommonModel.class)
        ).join();
    }
}
