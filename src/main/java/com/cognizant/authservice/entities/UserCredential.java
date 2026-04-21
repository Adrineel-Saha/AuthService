package com.cognizant.authservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="User_Credential")
public class UserCredential {
    private int id;
    private String name;
    private String email;
    private String password;
}
