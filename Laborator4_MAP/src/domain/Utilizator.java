package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import validators.ValidationException;
public class Utilizator extends Entity<Long> {
    private String firstName;
    private String lastName;
    private final ArrayList<Utilizator> friends;

    //constructor
    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new ArrayList<>();
    }

    //gettere si settere
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

    public List<Utilizator> getFriends() {
        return friends;
    }

    /**
     * adauga un prieten in lista utilizatorului de prieteni
     * @param friend: prietenul de adaugat
     * @throws ValidationException
     *          daca prietenul e null, sau sunt deja prieteni utiliatorii sau au acelasi id
     */
    public void addFriend(Utilizator friend) throws ValidationException{
        if (friend == null){
            throw new ValidationException("Prietenul nu poate sa fie null.");
        }
        if (this.friends.contains(friend)){
            throw new ValidationException("Sunt deja prieteni.");
        }
        if (Objects.equals(this.getId(), friend.getId())){
            throw new ValidationException("Cei doi prieteni au acelasi id.");
        }
        this.friends.add(friend);
    }

    public void removeFriend(Long id) throws ValidationException {
        // Use an iterator to safely remove the friend while iterating over the list
        for (java.util.Iterator<Utilizator> iterator = friends.iterator(); iterator.hasNext();) {
            Utilizator friend = iterator.next();
            if (Objects.equals(friend.getId(), id)) {
                iterator.remove();
                friend.getFriends().remove(this);
                return;
            }
        }
        throw new ValidationException("Nu sunt prieteni.");
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Utilizator{" +
                "prenume='" + firstName + '\'' +
                ", nume='" + lastName + '\'' +
                ", friends: ");
        for(Utilizator utilizator: friends) {
            result.append(utilizator.getFirstName())
                    .append(" ")
                    .append(utilizator.getLastName())
                    .append(",");
        }
        result = new StringBuilder(result.substring(0, result.length() - 1));
        result.append("}");
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getId());
    }
}
