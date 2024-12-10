package com.example.ezlawbackend.Auth.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.persistence.Column;

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

    @Column(nullable = true)
    private String stripeCustomerId;


    @Column(nullable = true)
    private String currency = "thb";

    @Column(nullable = true)
    private boolean isMember = false;

    public User(String firstname, String lastname, String email, String hashedPassword,
                String role, String phone, String gender, String stripeCustomerId,
                String currency, boolean isMember
    ) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
        this.phone = phone;
        this.gender = gender;
        this.stripeCustomerId = stripeCustomerId;
        this.currency = currency;
        this.isMember = isMember;
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

    public void setRole(String role) {this.role = role;}

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

    public boolean isMember() {return isMember;}
    public void setMember(boolean member) {isMember = member;}

    public String getCurrency() {return currency;}
    public void setCurrency(String currency) {this.currency = currency;}

    public String getStripeCustomerId() {return stripeCustomerId;}
    public void setStripeCustomerId(String stripeCustomerId) {this.stripeCustomerId = stripeCustomerId;}
}