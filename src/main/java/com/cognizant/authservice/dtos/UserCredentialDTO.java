package com.cognizant.authservice.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCredentialDTO {

    private int id;

    @NotBlank(message="User Name cannot be blank")
    @Size(min = 3, max = 50, message = "User Name must be between 3 to 50 characters")
    private String userName;

    @Email(message="Please enter a valid email")
    @NotBlank(message="Email cannot be blank")
    private String email;

    private String password;

    private String role;

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
}
