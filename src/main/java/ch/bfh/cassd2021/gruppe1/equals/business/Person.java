package ch.bfh.cassd2021.gruppe1.equals.business;

import java.time.LocalDate;
import java.util.Objects;

public class Person {

  private long id;
  private String lastName;
  private String firstName;
  private LocalDate dateOfBirth;
  private String placeOfOrigin;
  private String sex;
  private String userName;
  private int password;

  /**
   * Berechnet eine einfache Quersumme aus Tag, Monat und Jahr des Geburtsdatums (dateOfBirth).
   *
   * Beispiel: dateOfBirth = 23.04.2001, checksum = 23 + 04 + 2001 = 2028
   * @return Die vereinfachte, aus Tag, Monat und Jahr gebildete Quersumme des Geburtsdatums.
   */
  public Integer calculateChecksumOfDateOfBirth() {
    Objects.requireNonNull(dateOfBirth, "dateOfBirth must not be null");
    Integer checksumOfDateOfBirth = 0;

    checksumOfDateOfBirth += dateOfBirth.getYear();
    checksumOfDateOfBirth += dateOfBirth.getMonthValue();
    checksumOfDateOfBirth += dateOfBirth.getDayOfMonth();

    return checksumOfDateOfBirth;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public int getPassword() {
    return password;
  }

  public void setPassword(int password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "Person{" +
        "id=" + id +
        ", lastName='" + lastName + '\'' +
        ", firstName='" + firstName + '\'' +
        ", dateOfBirth=" + dateOfBirth +
        ", placeOfOrigin='" + placeOfOrigin + '\'' +
        ", sex='" + sex + '\'' +
        ", userName='" + userName + '\'' +
        ", password=" + password +
        '}';
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (o == null || getClass() != o.getClass()) {
          return false;
      }
    Person person = (Person) o;
    return id == person.id &&
        password == person.password &&
        Objects.equals(lastName, person.lastName) &&
        Objects.equals(firstName, person.firstName) &&
        Objects.equals(dateOfBirth, person.dateOfBirth) &&
        Objects.equals(placeOfOrigin, person.placeOfOrigin) &&
        Objects.equals(sex, person.sex) &&
        Objects.equals(userName, person.userName);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, lastName, firstName, dateOfBirth, placeOfOrigin, sex, userName, password);
  }
}
