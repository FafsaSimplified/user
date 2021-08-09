package com.accenture.user.entity;

import com.accenture.user.dto.SignUpDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    @JsonIgnore()
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Column(name = "ssn", nullable = false, unique = true)
    private String ssn;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "cell_phone", nullable = false)
    private String cellPhone;
    @Column(name = "home_phone", nullable = true)
    private String homePhone;
    @Column(name = "language", nullable = false)
    private String language;
    @Column(name = "dob", nullable = false)
    private LocalDate dob;
    @Column(name = "street_address", nullable = false)
    private String streetAddress;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "state", nullable = false)
    private String state;
    @Column(name = "zip_code", nullable = false)
    private String zipCode;
    @Column(name = "sms_opt_in", nullable = false)
    private Boolean smsOptIn;
    @Column(name = "create_time")
    private LocalDateTime createTime;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private UserRole role;

    public User() {
    }

    public User(User user) {
        this.id = user.id;
        this.username = user.username;
        this.password = user.password;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.middleName = user.middleName;
        this.ssn = user.ssn;
        this.email = user.email;
        this.cellPhone = user.cellPhone;
        this.createTime = user.createTime;
        this.role = new UserRole(user.role.name);
    }

    public User(SignUpDto signUpDto) {
        this.username = signUpDto.getUsername();
        this.password = signUpDto.getPassword();
        this.firstName = signUpDto.getFirstName();
        this.lastName = signUpDto.getLastName();
        this.middleName = signUpDto.getMiddleName();
        this.ssn = signUpDto.getSsn();
        this.email = signUpDto.getEmail();
        this.cellPhone = signUpDto.getCellPhone();
        this.homePhone = signUpDto.getHomePhone();
        setDob(signUpDto.getDob());
        this.streetAddress = signUpDto.getStreetAddress();
        this.city = signUpDto.getCity();
        this.state = signUpDto.getState();
        this.zipCode = signUpDto.getZipCode();
        this.smsOptIn = signUpDto.getSmsOptIn();
        this.language = "EN";
        this.role = new UserRole(Role.CUSTOMER.name());
    }


    public User generalAccess() {
        User generalUser = new User(this);
        generalUser.createTime = null;
        generalUser.ssn = null;
        return generalUser;
    }

    public User privilegedAccess() {
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = LocalDate.parse(dob);
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

    public String getZip_code() {
        return zipCode;
    }

    public void setZip_code(String zip_code) {
        this.zipCode = zip_code;
    }

    public Boolean getSmsOptIn() {
        return smsOptIn;
    }

    public void setSmsOptIn(Boolean smsOptIn) {
        this.smsOptIn = smsOptIn;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = LocalDateTime.parse(createTime);
    }

    public Role getRole() {
        return Role.valueOf(this.role.name);
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
