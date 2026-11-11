package com.enterpriseflow.orders;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="order_item")
public class OrderItem {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="order_id") private CustomerOrder order;
 @Column(nullable=false) private String description;
 @Column(nullable=false) private Integer quantity;
 @Column(name="unit_price",nullable=false) private BigDecimal unitPrice;
 protected OrderItem(){}
 public OrderItem(CustomerOrder order,String description,Integer quantity,BigDecimal unitPrice){
  this.order=order;this.description=description;this.quantity=quantity;this.unitPrice=unitPrice;
 }
}
