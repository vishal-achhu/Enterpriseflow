package com.enterpriseflow.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="app_user")
public class AppUser {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String name;
    @Column(nullable=false, unique=true) private String email;
    @Column(name="password_hash", nullable=false) private String passwordHash;
    @Column(name="created_at") private LocalDateTime createdAt = LocalDateTime.now();
    protected AppUser() {}
    public AppUser(String name, String email, String passwordHash) {
        this.name=name; this.email=email; this.passwordHash=passwordHash;
    }
    public Long getId(){return id;} public String getName(){return name;}
    public String getEmail(){return email;} public String getPasswordHash(){return passwordHash;}
}
