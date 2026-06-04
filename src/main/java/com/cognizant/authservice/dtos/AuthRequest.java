package com.cognizant.authservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message="User Name cannot be blank")
    @Size(min = 3, max = 50, message = "User Name must be between 3 to 50 characters")
    private String userName;

    @NotBlank(message="Password cannot be blank")
    private String password;

//    public String getUserName() {
//        return userName;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
}
