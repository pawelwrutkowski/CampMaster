package forms;

import dao.PersonsDAO;
import entity.Person;
import entity.User;

public class RegisterChildForm extends Requests implements IRegisterForms {
    private User user;
    private final PersonsDAO personsDAO = new PersonsDAO();

    private final int defaultRoleId = 16;
    private final int defaultPrivilegeId = 4;

    public RegisterChildForm(User user) {
        this.user = user;
    }

    @Override
    public void registerInDatabase() {
        personsDAO.save(makeInstanceOfPerson());
    }

    @Override
    public void registerInDatabaseWithNewGroup() {
    }

    /**
     * Creates a Person object from the provided data by the user.
     *
     * @return Person with role_id = 4 and privilege_id = 16
     */
    private Person makeInstanceOfPerson() {
        Person person = new Person();
        person.setEmail(requestEmail());
        person.setPassword(requestPassword());
        person.setFirstName(requestName("imię"));
        person.setLastName(user.getLastName());
        person.setBirthDate(requestBirthDate(minChildAge, maxChildAge));
        person.setPhoneNumber(requestPhoneNumber());
        person.setParentId(user.getPersonId());
        person.setRoleId(defaultRoleId);
        person.setPrivilegeId(defaultPrivilegeId);
        return person;
    }
}
