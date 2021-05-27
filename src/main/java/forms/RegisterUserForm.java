package forms;

import dao.PersonsDAO;
import entity.Person;

public class RegisterUserForm extends Requests implements IRegisterForms {
    private final PersonsDAO personsDAO = new PersonsDAO();

    private final int defaultRoleId = 15;
    private final int defaultPrivilegeId = 3;


    @Override
    public void registerInDatabase() {
        personsDAO.save(makeInstanceOfPerson());
    }

    @Override
    public void registerInDatabaseWithNewGroup() {}

    /**
     * Creates a Person object from the provided data by the user.
     *
     * @return Person with role_id = 3 and privilege_id = 15
     */
    private Person makeInstanceOfPerson() {
        Person person = new Person();
        person.setEmail(requestEmail());
        person.setPassword(requestPassword());
        person.setFirstName(requestName("imię"));
        person.setLastName(requestName("nazwisko"));
        person.setBirthDate(requestBirthDate(minAdultAge, maxAdultAge));
        person.setPhoneNumber(requestPhoneNumber());
        person.setRoleId(defaultRoleId);
        person.setPrivilegeId(defaultPrivilegeId);
        return person;
    }
}
