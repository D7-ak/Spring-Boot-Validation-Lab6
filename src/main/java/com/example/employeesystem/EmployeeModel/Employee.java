package com.example.employeesystem.EmployeeModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

@Data
@AllArgsConstructor
public class Employee {
    @NotEmpty(message = "ID must not be empty")
    @Size(min = 3,message = "ID must be at least 3 characters ")
    private String id;
    @NotEmpty(message = "name must not be empty")
    @Size(min = 5,message = "name must be at least 5 litters")
    @Pattern(regexp = "^[A-Za-z]*$",message = "name must be litters only")
    private String name;
    @Email
    private String email;
    @Pattern(regexp = "^05\\d{8}$",message = "phone number must start with 05 and must be 10 numbers ")
    private String phoneNumber;
    @NotNull(message = "age must not be empty")
    @Min(value = 26,message = "age must be at least 26")
    private Integer age;
    @NotEmpty(message = "position must not be empty")
    @Pattern(regexp = "^(supervisor|coordinator)$",message = "position must be 'supervisor' or 'coordinator' ")
    private String position;
    private boolean onLeave;
    @JsonFormat(pattern="yyyy-MM-dd")
    @PastOrPresent(message = "date must be past or present")
    private Date hireDate;
    @NotNull(message = "annual Leave must not be empty")
    @Positive(message = "annual Leave must be a positive number")
    private int annualLeave;


}
