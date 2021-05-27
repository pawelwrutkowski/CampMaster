package forms;

import dao.PersonsDAO;
import entity.Person;

public class EditPersonForm extends Requests implements IEditForms {
    private Person person;
    private PersonsDAO personsDAO = new PersonsDAO();

    public EditPersonForm(Person person) {
        this.person = person;
    }

    @Override
    public void editEmail() {
        person.setEmail(requestEmail());
        personsDAO.update(person);
    }

    @Override
    public void editPassword() {
        person.setPassword(requestPassword());
        personsDAO.update(person);
    }

    @Override
    public void editFirstName() {
        person.setFirstName(requestName("imię"));
        personsDAO.update(person);
    }

    @Override
    public void editLastName() {
        person.setLastName(requestName("nazwisko"));
        personsDAO.update(person);
    }

    @Override
    public void editChildBirthDate() {
        person.setBirthDate(requestBirthDate(minChildAge, maxChildAge));
        personsDAO.update(person);
    }

    @Override
    public void editBirthDate() {
        person.setBirthDate(requestBirthDate(minAdultAge, maxAdultAge));
        personsDAO.update(person);
    }

    @Override
    public void editPhoneNumber() {
        person.setPhoneNumber(requestPhoneNumber());
        personsDAO.update(person);
    }

    @Override
    public void deleteProfile() {
        personsDAO.askToDelete("T", person.getPersonId());
    }
}
