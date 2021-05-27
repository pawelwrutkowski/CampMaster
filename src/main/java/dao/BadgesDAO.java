package dao;

import connection.CustomConnection;
import connection.MySqlConnector;
import entity.Badge;
import lombok.extern.log4j.Log4j;

import java.sql.*;

@Log4j
public class BadgesDAO {
    private final CustomConnection mySqlConnection;

    public BadgesDAO() {
        mySqlConnection = new MySqlConnector();
    }

    /**
     * Seeks badge entry in "badges" table using badge_id parameter
     *
     * @param badgeId unique int badge_id value of badge to find
     * @return Badge - returns Badge type object
     */
    public Badge findById(int badgeId) {
        String query = "SELECT * FROM badges WHERE badge_id = " + badgeId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                Badge badge = new Badge();
                badge.setBadgeId(resultSet.getInt("badge_id"));
                badge.setName(resultSet.getString("name"));
                badge.setDescription(resultSet.getString("description"));
                return badge;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Saves new badge entry in "badges" table using Badge type object
     *
     * @param badge Badge type object
     */

    public void save(Badge badge) {
        String query = "INSERT INTO badges " +
                "(name, " +
                "description) " +
                "VALUES " +
                "(?,?)";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, badge.getName());
            statement.setString(2, badge.getDescription());
            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Deletes badge entry in "badges" table using int badgeId value
     *
     * @param badgeId unique int badgeId value of entry to delete
     */
    public void delete(int badgeId) {
        String query = "DELETE FROM badges WHERE badge_id = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, badgeId);
            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }
/*
    public void update(Badge badge) {
        String query = "UPDATE badges SET " +
                "name = ?, " +
                "description = ?";

        try (Connection connection = mySqlConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, badge.getName());
            statement.setString(2, badge.getDescription());
            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }
*/
}
