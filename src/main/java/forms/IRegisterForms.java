package forms;

public interface IRegisterForms {
    /**
     * Saves Person object created by the user to the database in the table "persons".
     */
    void registerInDatabase();

    void registerInDatabaseWithNewGroup();
}
