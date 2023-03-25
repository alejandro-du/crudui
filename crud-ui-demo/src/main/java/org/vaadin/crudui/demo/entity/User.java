package org.vaadin.crudui.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

/**
 * @author Alejandro Duarte
 */
@Entity
@Table(name = "user_")
public class User {

    @NotNull
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    @Past
    private LocalDate birthDate;

    @NotNull
    private int phoneNumber; // as an int for testing purposes

    @NotNull
    @Email
    private String email;

    @NotNull
    private BigDecimal salary;

    @NotNull
    @Size(min = 6, max = 100)
    private String password;

    private Boolean active = true;

    @ManyToOne
    private Group mainGroup;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @NotNull
    private Set<Group> groups = new HashSet<>();

    private MaritalStatus maritalStatus;

    public User() {
    }

    public User(@NotNull String name, @Past LocalDate birthDate, @NotNull int phoneNumber, @NotNull @Email String email,@NotNull BigDecimal salary,
                @NotNull @Size(min = 6, max = 100) String password, Boolean active, Group mainGroup, Set<Group> groups, MaritalStatus maritalStatus) {
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.salary = salary;
        this.password = password;
        this.active = active;
        this.mainGroup = mainGroup;
        this.groups = groups;
        this.maritalStatus = maritalStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Group getMainGroup() {
        return mainGroup;
    }

    public void setMainGroup(Group mainGroup) {
        this.mainGroup = mainGroup;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
}
