package com.noriservices.norisales.domain.user;

public enum UserRole {
    BUYER("BUYER"),
    ADMIN("ADMIN");

    private String role;

     UserRole(String role){
        this.role = role;
    }

    public String getRole(){
         return role;
    }
}
