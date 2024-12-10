package com.example.ezlawbackend.Auth.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
public class User {

    @Id
    private String Userid;

    private String firstname;
    private String lastname;
    private String email;
    private String hashedPassword;
    private String role;
    private String phone;
    private String gender;

    public User(String firstname, String lastname, String email, String hashedPassword, String role, String phone, String gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
        this.phone = phone;
        this.gender = gender;
    }
    public String getId() {
        return Userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getGender(){
        return gender;
    }

    public void setGender(String gender){
        this.gender = gender;
    }
}