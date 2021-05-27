package dao;

import connection.CustomConnection;
import connection.MySqlConnector;

import entity.Badge;
import entity.PersBadge;
import entity.Person;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class PersBadgesDAO {
    private final CustomConnection mySqlConnection;

    public PersBadgesDAO() {
        mySqlConnection = new MySqlConnector();
    }

    /**
     * Seeks person-badge entry in "person_badges" table using person_id parameter
     *
     * @param personId unique int person_id value of person-badge pairs to find
     * @return List(PersBadge) returns List of PersBadge type object
     */
    public List<PersBadge> findByPersonId(int personId) {
        List<PersBadge> result = new ArrayList<>();

        String query = "SELECT * FROM person_badges WHERE person_id = " + personId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                PersBadge persBadges = new PersBadge();
                persBadges.setPersonId(resultSet.getInt("person_id"));
                persBadges.setBadgeId(resultSet.getInt("badge_id"));
                persBadges.setAcquired(resultSet.getDate("acquired").toLocalDate());

                result.add(persBadges);
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return result;
    }

    /**
     * Seeks person-badge entry in "person_badges" table using person_id parameter
     * joining badges table using badge_id and joining persons table using person_id
     *
     * @param personId unique int person_id value of person-badge pairs with joined objects to find
     * @return List(PersBadge) returns List of PersBadge type object with joined Person type objects and joined Badge objects
     */
    public List<PersBadge> findByPersonIdJoinBadgesJoinPerson(int personId) {
        List<PersBadge> result = new ArrayList<>();

        String query = "SELECT * FROM person_badges pb" +
                " JOIN badges b ON pb.badge_id = b.badge_id" +
                " JOIN persons p ON pb.person_id = p.person_id" +
                " WHERE pb.person_id = " + personId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                PersBadge persBadges = new PersBadge();
                persBadges.setPersonId(resultSet.getInt("person_id"));
                persBadges.setBadgeId(resultSet.getInt("badge_id"));
                persBadges.setAcquired(resultSet.getDate("acquired").toLocalDate());

                Badge badge = new Badge();
                badge.setBadgeId(resultSet.getInt("badge_id"));
                badge.setDescription(resultSet.getString("description"));
                badge.setName(resultSet.getString("name"));

                Person person = new Person();
                person.setPersonId(resultSet.getInt("person_id"));
                person.setFirstName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));

                persBadges.setBadge(badge);
                persBadges.setPerson(person);

                result.add(persBadges);
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return result;
    }

    /**
     * Seeks person-badge entry in "person_badges" table using badge_id parameter
     *
     * @param badgeId unique int badge_id value of person-badge pairs to find
     * @return List(PersBadge) returns List of PersBadge type objects
     */
    public List<PersBadge> findByBadgeId(int badgeId) {
        List<PersBadge> result = new ArrayList<>();

        String query = "SELECT * FROM person_badges WHERE badge_id = " + badgeId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                PersBadge persBadges = new PersBadge();
                persBadges.setPersonId(resultSet.getInt("person_id"));
                persBadges.setBadgeId(resultSet.getInt("badge_id"));
                persBadges.setAcquired(resultSet.getDate("acquired").toLocalDate());

                result.add(persBadges);
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return result;
    }

    /**
     * Saves new person-badge entry in "persons_badges" table using PersBadge type object
     *
     * @param persBadge Person type object
     */
    public void save(PersBadge persBadge) {
        String query = "INSERT INTO person_badges " +
                "(person_id, " +
                "badge_id, " +
                "acquired) " +
                "VALUES " +
                "(?,?,?)";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, persBadge.getPersonId());
            statement.setInt(2, persBadge.getBadgeId());
            statement.setDate(3, Date.valueOf(persBadge.getAcquired()));

            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Updates person-badge entry in "persons_badges" table using two PersBadge type object
     *
     * @param oldPersBadge Person type object with old parameters
     * @param newPersBadge Person type object with new parameters
     */
    public void update(PersBadge oldPersBadge, PersBadge newPersBadge) {
        String query = "UPDATE person_badges SET " +
                "person_id = ?, " +
                "badge_id = ?, " +
                "acquired = ? " +
                "WHERE " +
                "person_id = ? " +
                "AND badge_id = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, newPersBadge.getPersonId());
            statement.setInt(2, newPersBadge.getBadgeId());
            statement.setDate(3, Date.valueOf(newPersBadge.getAcquired()));
            statement.setInt(4, oldPersBadge.getPersonId());
            statement.setInt(5, oldPersBadge.getBadgeId());

            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Deletes person entry in "persons" table using PersBadge object
     *
     * @param persBadge unique PersBadge object with parameters like entry to delete
     */
    public void delete(PersBadge persBadge) {
        String query = "DELETE FROM person_badges " +
                "WHERE " +
                "person_id = ? " +
                "AND badge_id = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, persBadge.getPersonId());
            statement.setInt(2, persBadge.getBadgeId());

            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }
}
