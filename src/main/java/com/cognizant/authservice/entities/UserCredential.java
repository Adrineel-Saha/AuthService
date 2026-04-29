package com.cognizant.authservice.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="User_Credential")
@Data
public class UserCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="User_Id")
    private int id;

    @Column(name="User_Name")
    private String name;

    @Column(name="Email")
    private String email;

    @Column(name="Password")
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
