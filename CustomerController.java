package com.enterpriseflow.customer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/customers")
public class CustomerController {
 private final CustomerRepository repo;
 public CustomerController(CustomerRepository repo){this.repo=repo;}
 public record CustomerRequest(@NotBlank String name,@Email @NotBlank String email,String company){}
 public record CustomerResponse(Long id,String name,String email,String company){}
 @GetMapping public List<CustomerResponse> list(){return repo.findAll().stream().map(this::dto).toList();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public CustomerResponse create(@Valid @RequestBody CustomerRequest r){
  return dto(repo.save(new Customer(r.name(),r.email(),r.company())));
 }
 private CustomerResponse dto(Customer c){return new CustomerResponse(c.getId(),c.getName(),c.getEmail(),c.getCompany());}
}
