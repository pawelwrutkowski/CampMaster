package dao;

import connection.CustomConnection;
import connection.MySqlConnector;
import entity.Person;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Log4j
public class PersonsDAO {
    private final CustomConnection mySqlConnection; //tworzymy tu sobie żeby w ogóle łączyło z bazą danych

    public PersonsDAO() {
        mySqlConnection = new MySqlConnector(); //inicjujemy w konstruktorze
    }

    /**
     * Seeks person entry in "persons" table using person_id parameter
     *
     * @param personId unique int person_id value of person to find
     * @return Person type object
     */
    public Person findById(int personId) {
        String query = "SELECT * FROM persons WHERE person_id = " + personId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {  //result set głównie do odbioru ale da się go przerobić do zapisu
                //log.info("Wynik Find: ID: " + resultSet.getInt("person_id") + " Last Name: " + resultSet.getString("last_name"));

                Person person = new Person();
                person.setPersonId(resultSet.getInt("person_id"));
                person.setFirstName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                person.setEmail(resultSet.getString("email"));
                person.setPassword(resultSet.getString("password"));
                person.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                person.setRoleId(resultSet.getInt("role_id"));
                person.setParentId(resultSet.getInt("parent_id"));
                person.setPhoneNumber(resultSet.getString("phone_number"));
                person.setPrivilegeId(resultSet.getInt("privilege_id"));

                return person;
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Seeks person entry in "persons" table using person_id parameter
     *
     * @param personEmail unique String email value of person to find
     * @return Person returns Persons type object
     */
    public Person findByEmail(String personEmail) {
        String query = "SELECT * FROM persons WHERE email = '" + personEmail + "'";

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                // log.info("Wynik: ID: " + resultSet.getInt("person_id") + " Last Name: " + resultSet.getString("last_name"));

                Person person = new Person();
                person.setPersonId(resultSet.getInt("person_id"));          //1
                person.setFirstName(resultSet.getString("first_name"));     //2
                person.setLastName(resultSet.getString("last_name"));       //3
                person.setEmail(resultSet.getString("email"));              //4
                person.setPassword(resultSet.getString("password"));        //5
                person.setBirthDate(resultSet.getDate("birth_date").toLocalDate());  //6
                person.setRoleId(resultSet.getInt("role_id"));              //7
                person.setParentId(resultSet.getInt("parent_id"));          //8
                person.setPhoneNumber(resultSet.getString("phone_number"));    //9
                person.setPrivilegeId(resultSet.getInt("privilege_id"));    //10
                return person;
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Seeks entries of children in "persons" table using parents person_id as parameter
     * Creates list of users children
     *
     * @param parentPersonId unique parent int person_id value
     * @return List(Persons) returns List of Person type objects, where given param equals parent_id in "Persons" table
     */
    public List<Person> createListOfParentsChildren(int parentPersonId) {
        List<Person> result = new ArrayList<>();
        String query = "SELECT * FROM persons WHERE parent_id = " + parentPersonId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {

                Person person = new Person();
                person.setPersonId(resultSet.getInt("person_id"));
                person.setFirstName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                person.setEmail(resultSet.getString("email"));
                person.setPassword(resultSet.getString("password"));
                person.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                person.setRoleId(resultSet.getInt("role_id"));
                person.setParentId(resultSet.getInt("parent_id"));
                person.setPhoneNumber(resultSet.getString("phone_number"));
                person.setPrivilegeId(resultSet.getInt("privilege_id"));
                result.add(person);
            }
            return result;

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Finds and saves to list every Person from "persons" table.
     *
     * @return List(Persons) returns List of Person type objects
     */
    public List<Person> createListOfAllUsers() {
        List<Person> result = new ArrayList<>();
        String query = "SELECT * FROM persons";

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {

                Person person = new Person();
                person.setPersonId(resultSet.getInt("person_id"));
                person.setFirstName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                person.setEmail(resultSet.getString("email"));
                person.setPassword(resultSet.getString("password"));
                person.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                person.setRoleId(resultSet.getInt("role_id"));
                person.setParentId(resultSet.getInt("parent_id"));
                person.setPhoneNumber(resultSet.getString("phone_number"));
                person.setPrivilegeId(resultSet.getInt("privilege_id"));
                result.add(person);
            }
            return result;

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Prints full name and id of each child of the user (List<Person> object) to console in an ordered and formatted manner
     *
     * @param parentsChildren List<Person> object
     */
    public void printChildren(List<Person> parentsChildren) {
        System.out.println("Twoje dzieci:");
        int countChildren = 1;
        for (Person child : parentsChildren) {
            System.out.println(countChildren + ") \tImię i nazwisko: " + child.getFirstName() + " " + child.getLastName() + "\tID: " + child.getPersonId());
            countChildren++;
        }
    }

    /**
     * Seeks actual max person_id value (autoincremented) in table persons
     *
     * @return returns max value of person_id in persons table
     */
    public int findMaxPersonId() {
        String query = "SELECT MAX(person_id) FROM persons";

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getInt("MAX(person_id)");
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return 0;
    }

    /**
     * Deletes person entry  in "persons" table using person_id parameter
     *
     * @param personId unique int person_id value
     */
    public void delete(int personId) {
        String query = "DELETE FROM persons WHERE person_id = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, personId);
            preparedStatement.executeUpdate();

            log.info("Delete query: " + preparedStatement);
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Saves new person entry in "persons" table using Person type object
     *
     * @param person Person type object
     */
    public void save(Person person) {
        String query = "INSERT INTO persons " +
                "(first_name, " +       //1
                "last_name, " +         //2
                "email, " +             //3
                "password, " +          //4
                "birth_date, " +        //5
                "role_id, " +           //6
                "parent_id, " +         //7
                "phone_number, " +      //8
                "privilege_id) " +      //9
                "VALUES " +
                "(?,?,?,?,?,?,?,?,?)";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // statement.setInt(1, person.getPersonId());                //person id
            statement.setString(1, person.getFirstName());             //1 first name
            statement.setString(2, person.getLastName());            //2 last name
            statement.setString(3, person.getEmail());             //3 email
            statement.setString(4, person.getPassword());           //4 password
            statement.setDate(5, Date.valueOf(person.getBirthDate()));  //5 birthdate
            statement.setInt(6, person.getRoleId());                    //6 role id
            if (person.getParentId() == null) {
                statement.setNull(7, Types.INTEGER);
            } else {
                statement.setInt(7, person.getParentId());
            }         // 7 parent
            statement.setString(8, person.getPhoneNumber());            //8 phone
            statement.setInt(9, person.getPrivilegeId());                    //9 privilege id

            statement.executeUpdate();

            log.info("Save query: " + statement);

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Updates existing person entry in "persons" table using Person type object
     *
     * @param person Person type object
     */
    public void update(Person person) {
        String query = "UPDATE persons " +
                "SET first_name = ?" +          //1
                ", last_name = ?" +             //2
                ", email = ?" +                 //3
                ", password = ?" +              //4
                ", birth_date = ?" +            //5
                ", role_id = ?" +               //6
                //", parent_id = ?" +           //Nieuzywane
                ", phone_number = ?" +          //7
                ", privilege_id = ?" +          //8
                " WHERE person_id = ?";         //9

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.setString(4, person.getPassword());
            preparedStatement.setDate(5, Date.valueOf(person.getBirthDate()));
            preparedStatement.setInt(6, person.getRoleId());

            /* Nieuzywane
            if (person.getParentId() == null)
                preparedStatement.setNull(7, Types.INTEGER);
            else
                preparedStatement.setInt(7, person.getParentId());
             */

            preparedStatement.setString(7, person.getPhoneNumber());
            preparedStatement.setInt(8, person.getPrivilegeId());
            preparedStatement.setInt(9, person.getPersonId());
            preparedStatement.executeUpdate();

            log.info("Update query: " + preparedStatement);

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Deletes person entry in "persons" table using email parameter
     *
     * @param email unique String email value of person to delete
     */
    public void deleteByEmail(String email) {
        String query = "DELETE FROM persons WHERE email = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();

            log.info("Delete query: " + preparedStatement);
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Asks to enter matching text before removing the person from the "Persons" table
     *
     * @param textToAccept String value that will allow to delete record
     * @param personId     unique int from "Persons" table
     */
    public void askToDelete(String textToAccept, int personId) {
        System.out.println("Czy na pewno chcesz usunąć? Nie można cofnąć tej operacji.");
        System.out.println("Aby kontynuować operację wprowadź: " + textToAccept);

        Scanner scanner = new Scanner(System.in);
        String scanText = scanner.nextLine();

        if (scanText.equals(textToAccept)) {
            delete(personId);
        }
    }
}
