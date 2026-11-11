package com.enterpriseflow.orders;
import com.enterpriseflow.customer.Customer;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="customer_order")
public class CustomerOrder {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="customer_id") private Customer customer;
 @Column(nullable=false,unique=true) private String reference;
 @Column(nullable=false) private String status;
 @Column(nullable=false) private BigDecimal total;
 @Column(name="created_at") private LocalDateTime createdAt=LocalDateTime.now();
 protected CustomerOrder(){}
 public CustomerOrder(Customer c,String ref,BigDecimal total){customer=c;reference=ref;this.total=total;status="DRAFT";}
 public Long getId(){return id;} public Customer getCustomer(){return customer;} public String getReference(){return reference;}
 public String getStatus(){return status;} public BigDecimal getTotal(){return total;} public LocalDateTime getCreatedAt(){return createdAt;}
 public void setStatus(String status){this.status=status;}
}
