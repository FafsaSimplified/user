package com.accenture.user.dto;

import com.accenture.user.validation.annotation.ValidSignUpDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@ValidSignUpDto
public class SignUpDto {
    private String firstName;
    private String middleName;
    private String lastName;
    @Size(min = 6, max = 30)
    @Pattern(regexp = "[a-zA-Z\\d]{6,30}")
    private String username;
    @Size(min = 8, max = 30)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z\\d@$!%*#?&]{8,30}$")
    private String password;
    @Pattern(regexp = "(\\d){9}")
    private String ssn;
    private String dob;
    @Email
    private String email;
    @Pattern(regexp = "(\\d){10}")
    private String cellPhone;
    //    @Pattern(regexp = "(^(\\d){10}$)")
    private String homePhone;
    private Boolean smsOptIn;
    private String streetAddress;
    private String city;
    private String state;
    @Pattern(regexp = "(\\d){5,9}")
    private String zipCode;
    private String cqas;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getSmsOptIn() {
        return smsOptIn;
    }

    public void setSmsOptIn(Boolean smsOptIn) {
        this.smsOptIn = smsOptIn;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCqas() {
        return cqas;
    }

    public void setCqas(String cqas) {
        this.cqas = cqas;
    }
}
