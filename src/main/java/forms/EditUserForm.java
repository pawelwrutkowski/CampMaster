package forms;

import dao.PersonsDAO;
import entity.User;

public class EditUserForm extends Requests implements IEditForms {
    private User user;
    private PersonsDAO personsDAO = new PersonsDAO();

    public EditUserForm(User user) {
        this.user = user;
    }

    @Override
    public void editEmail() {
        user.setEmail(requestEmail());
        personsDAO.update(user);
    }

    @Override
    public void editPassword() {
        user.setPassword(requestPassword());
        personsDAO.update(user);
    }

    @Override
    public void editFirstName() {
        user.setFirstName(requestName("imię"));
        personsDAO.update(user);
    }

    @Override
    public void editLastName() {
        user.setLastName(requestName("nazwisko"));
        personsDAO.update(user);
    }

    @Override
    public void editBirthDate() {
        user.setBirthDate(requestBirthDate(minAdultAge, maxAdultAge));
        personsDAO.update(user);
    }

    @Override
    public void editChildBirthDate() {

    }

    @Override
    public void editPhoneNumber() {
        user.setPhoneNumber(requestPhoneNumber());
        personsDAO.update(user);
    }

    @Override
    public void deleteProfile() {
        personsDAO.askToDelete("T", user.getPersonId());
    }
}
