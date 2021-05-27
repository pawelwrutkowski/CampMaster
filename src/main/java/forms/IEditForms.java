package forms;

public interface IEditForms {
    /**
     * From the value entered by the user sets the email in the Person object and then saves it to the database
     * in the table "persons"
     */
    void editEmail();

    /**
     * From the value entered by the user sets the password in the Person object and then saves it to the database
     * in the table "persons"
     */
    void editPassword();

    /**
     * From the value entered by the user sets the firstName in the Person object and then saves it to the database
     * in the table "persons"
     */
    void editFirstName();

    /**
     * From the value entered by the user sets the lastName in the Person object and then saves it to the database
     * in the table "persons"
     */
    void editLastName();

    /**
     * From the value entered by the user sets the birthDate in the Person object and then saves it to the database
     * in the table "persons"
     */
    void editBirthDate();

    /**
     * From the value entered by the user sets the birthDate in the Person object and then saves it to the database
     * in the table "persons"
     */
    void editChildBirthDate();

    /**
     * From the value entered by the user sets the phoneNumber in the Person object and then saves it to the database
     * in the table "persons"
     */
    void editPhoneNumber();

    void deleteProfile();
}
