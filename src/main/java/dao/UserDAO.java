package dao;

import connection.CustomConnection;
import connection.MySqlConnector;
import entity.User;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Log4j

public class UserDAO {

    private static final CustomConnection mySqlConnection = new MySqlConnector();

    /**
     * Requires input of email and password in console using Scanner and trying to match data in "Persons" table
     *
     * @param email    unique String value of person
     * @param password String value of person
     * @return user - after successful login returns User type instance (inherited after Person class) or null value
     * after incorrect data input
     */
    public User signIn(String email, String password) {
        String query = "SELECT * FROM persons " +
                "WHERE email = '" + email + "' COLLATE utf8mb4_0900_ai_ci " +
                "AND password = '" + password + "' COLLATE utf8mb4_0900_as_cs " +
                "AND email = '" + email + "' " +
                "AND password = '" + password + "'";

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                log.info("Znaleziono użytkownika - Logowanie");

                User user = new User();
                user.setPersonId(resultSet.getInt("person_id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                user.setRoleId(resultSet.getInt("role_id"));
                user.setParentId(resultSet.getInt("parent_id"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setPrivilegeId(resultSet.getInt("privilege_id"));
                return user;
            } else {
                System.out.println("\nWprowadzono błędne dane... Spróbuj ponownie.");
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }
}
