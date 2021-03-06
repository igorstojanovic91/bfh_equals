package ch.bfh.cassd2021.gruppe1.equals.business.model;

import java.time.LocalDate;

/**
 * The Person class implements a person.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */

public class Person {

    private int personId;
    private String lastName;
    private String firstName;
    private LocalDate dateOfBirth;
    private String placeOfOrigin;
    private String sex;
    private String userName;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public void setPlaceOfOrigin(String placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Person{" +
            "id=" + personId +
            ", lastName='" + lastName + '\'' +
            ", firstName='" + firstName + '\'' +
            ", dateOfBirth=" + dateOfBirth +
            ", placeOfOrigin='" + placeOfOrigin + '\'' +
            ", sex='" + sex + '\'' +
            ", userName='" + userName + '\'' +
            '}';
    }
}
