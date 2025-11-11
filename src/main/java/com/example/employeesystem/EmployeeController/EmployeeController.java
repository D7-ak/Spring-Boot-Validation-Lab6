package com.example.employeesystem.EmployeeController;

import com.example.employeesystem.ApiResponse.ApiResponse;
import com.example.employeesystem.EmployeeModel.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    ArrayList<Employee> employees = new ArrayList<>();

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "The IDENTIFIER you entered is invalid, as it should contain only numbers.";
        return  ResponseEntity.status(400).body(message);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody @Valid Employee employee, Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        System.out.println(employee.getAge()+"sadd");
        employee.setOnLeave(false);
        this.employees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("employee added successfully"));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getEmployees(){
        if(this.employees.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("there is no Employees yet"));
        }
        return ResponseEntity.status(200).body(this.employees);
    }
    @PutMapping("/update/{index}")
    public ResponseEntity<?> updateEmployee(@PathVariable int index,@RequestBody @Valid Employee employee,Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        index+=1;
        if(index<0||index>this.employees.size()){
            return ResponseEntity.status(400).body(new ApiResponse("there is no employee"));
        }
        index-=1;
        this.employees.set(index,employee);
        return ResponseEntity.status(200).body(new ApiResponse("employee updated successfully"));
    }

    @DeleteMapping("/delete/{index}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int index){
        index+=1;
        if(index<0||index>this.employees.size()){
            return ResponseEntity.status(400).body(new ApiResponse("there is no employee"));
        }
        index-=1;
        this.employees.remove(index);
        return ResponseEntity.status(200).body(new ApiResponse("employee deleted successfully"));
    }

    @GetMapping("/get-by-position/{position}")
    public ResponseEntity<?> getByPosition(@PathVariable String position){
        if(position.matches("^(supervisor|coordinator)$")){
            ArrayList<Employee> byPosition = new ArrayList<>();
            for(Employee e:this.employees){
                if(e.getPosition().equals(position)){
                    byPosition.add(e);
                }
            }
            if(byPosition.isEmpty()){
                return ResponseEntity.status(400).body(new ApiResponse("there is no employees with this position: '"+position+"'"));
            }
            return ResponseEntity.status(200).body(byPosition);
        }
        return ResponseEntity.status(400).body(new ApiResponse("position must be 'supervisor' or 'coordinator'"));
    }

    @GetMapping("/get-by-age-range/{min}/{max}")
    public ResponseEntity<?> getByAgeRange(@PathVariable int min,@PathVariable int max){
        ArrayList<Employee> inRange = new ArrayList<>();
        for(Employee e:this.employees){
            if(e.getAge()>=min&&e.getAge()<=max){
                inRange.add(e);
            }
        }
        if(inRange.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("there is no employees in this range"));
        }
        return ResponseEntity.status(200).body(inRange);
    }

    @PutMapping("/apply/{index}")
    public ResponseEntity<?> applyEmployee(@PathVariable int index){
        index+=1;
        if(index<0||index>this.employees.size()){
            return ResponseEntity.status(400).body(new ApiResponse("there is no employee"));
        }
        index-=1;
        if(this.employees.get(index).isOnLeave()){
            return ResponseEntity.status(400).body(new ApiResponse("this employee is on leave"));
        }
        if(this.employees.get(index).getAnnualLeave()<1){
            return ResponseEntity.status(400).body(new ApiResponse("this employee has no annual leave"));
        }
        this.employees.get(index).setOnLeave(true);
        int annual = this.employees.get(index).getAnnualLeave();
        this.employees.get(index).setAnnualLeave(annual-1);
        return ResponseEntity.status(200).body(new ApiResponse("employee application accepted"));
    }

    @GetMapping("/no-annual")
    public ResponseEntity<?> getEmployeeWithoutAnnual(){
        ArrayList<Employee> noAnnual = new ArrayList<>();
        for(Employee e:this.employees){
            if(e.getAnnualLeave()<1){
                noAnnual.add(e);
            }
        }
        if(noAnnual.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("no employees without annual leave"));
        }
        return ResponseEntity.status(200).body(noAnnual);
    }

    @PutMapping("/promote/{index}/{id}")
    public ResponseEntity<?> promoteEmployee(@PathVariable int index,@PathVariable String id){
        index+=1;
        if(index<0||index>this.employees.size()){
            return ResponseEntity.status(400).body(new ApiResponse("there is no employee"));
        }
        index-=1;
        if(this.employees.get(index).getPosition().equals("supervisor")){
            for (Employee e:this.employees){
                if(e.getId().equals(id)&&e.getAge()>=30&&!e.isOnLeave()){
                    e.setPosition("supervisor");
                    return ResponseEntity.status(200).body(new ApiResponse("employee promoted"));
                }
            }

        }
        return ResponseEntity.status(400).body(new ApiResponse("employee was not promoted due to incomplete requirement "));
    }
}
