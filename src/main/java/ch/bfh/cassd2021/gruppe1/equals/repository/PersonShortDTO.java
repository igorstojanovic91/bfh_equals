package ch.bfh.cassd2021.gruppe1.equals.repository;

/**
 * Beispiel für ein typisches Data Transfer Object. In der Regel sollen sie "immutable" sein, d.h.
 * nach der Erzeugung sollen sie nicht mehr verändert werden können. Wenn es mehr Attribute gibt,
 * Builder Pattern anwenden, wurde hier als Beispiel implementiert, obschon nur 2 Attribute
 * vorhanden.
 */
public class PersonShortDTO {

    private long id;
    private String name;

    private PersonShortDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class Builder {

        private long id;
        private String name;

        public Builder(long id) {
            this.id = id;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public PersonShortDTO build() {
            final PersonShortDTO personShortDTO = new PersonShortDTO(this);
            return personShortDTO;
        }

    }
}
