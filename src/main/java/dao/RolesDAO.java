package dao;

import connection.CustomConnection;
import connection.MySqlConnector;
import entity.Role;
import lombok.extern.log4j.Log4j;

import java.sql.*;

@Log4j
public class RolesDAO {
    private final CustomConnection mySqlConnection;

    public RolesDAO() {
        mySqlConnection = new MySqlConnector();
    }

    /**
     * Seeks role entry in "roles" table using role_id parameter
     * @param roleId unique int role_id value of role to find
     * @return Role type object
     */
    public Role findById(int roleId) {
        String query = "SELECT * FROM roles WHERE role_id = " + roleId;

        log.info(query);

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {

                Role role = new Role();
                role.setRoleId(resultSet.getInt("role_id"));
                role.setName(resultSet.getString("name"));
                return role;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Seeks actual max role_id value (autoincremented) in table roles
     * @return returns max value of role_id in roles table
     */
    public int findMaxRoleId() {
        String query = "SELECT MAX(role_id) FROM roles";

        log.info(query);

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                log.info("Max role ID: " + resultSet.getString("MAX(role_id)"));

                return resultSet.getInt("MAX(role_id)");
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return 0;
    }

    /**
     * Saves new role entry in "roles" table using Role type object
     * @param role Role type object
     */
    public void save(Role role) {
        String query = "INSERT INTO roles (name) VALUES(?)";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, role.getName());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Updates existing role entry in "roles" table using Role type object
     * @param role Role type object
     */
    public void update(Role role) {
        String query = "UPDATE roles SET name = ? WHERE role_id = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, role.getName());
            statement.setInt(2, role.getRoleId());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Deletes role entry in "roles" table using role_id parameter
     *
     * @param roleId unique int role_id value
     */
    public void delete(int roleId) {
        String query = "DELETE FROM roles WHERE role_id = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, roleId);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }
}
