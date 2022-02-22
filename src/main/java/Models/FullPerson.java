package Models;

public class FullPerson {
    private Person person;
    private int birthYear;
    private int deathYear;

    public FullPerson(Person person, int birthYear, int deathYear) {
        this.person = person;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(int deathYear) {
        this.deathYear = deathYear;
    }
}
