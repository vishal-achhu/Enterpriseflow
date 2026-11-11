package com.enterpriseflow.customer;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="customer")
public class Customer {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String name;
 @Column(nullable=false) private String email;
 private String company;
 @Column(name="created_at") private LocalDateTime createdAt=LocalDateTime.now();
 protected Customer(){}
 public Customer(String name,String email,String company){this.name=name;this.email=email;this.company=company;}
 public Long getId(){return id;} public String getName(){return name;} public String getEmail(){return email;}
 public String getCompany(){return company;} public LocalDateTime getCreatedAt(){return createdAt;}
}
