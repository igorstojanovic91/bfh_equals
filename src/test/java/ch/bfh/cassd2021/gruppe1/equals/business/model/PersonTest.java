package ch.bfh.cassd2021.gruppe1.equals.business.model;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PersonTest {

  private final Logger logger = LoggerFactory.getLogger(PersonTest.class);

  @Test
  /*public void calculateChecksumOfDateOfBirthWithCorrectDate_calculateCorrectChecksum() {
    Person testPerson = createBaseTestPerson();
    testPerson.setDateOfBirth(LocalDate.of(2001, 4, 23));
    logger.debug("Testperson erzeugt: {}", testPerson);

    Integer expectedChecksum = 23 + 4 + 2001;
    Integer calculatedChecksum = testPerson.calculateChecksumOfDateOfBirth();

    assertEquals(expectedChecksum, calculatedChecksum);

  }*/

  private Person createBaseTestPerson() {
    Person testPerson = new Person();

    testPerson.setPersonId(1);
    testPerson.setLastName("Muster");
    testPerson.setFirstName("Max");

    return testPerson;
  }


}
