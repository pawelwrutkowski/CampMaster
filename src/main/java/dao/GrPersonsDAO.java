package dao;

import connection.CustomConnection;
import connection.MySqlConnector;
import entity.GrPersons;
import entity.Group;
import entity.Offer;
import entity.Person;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j

public class GrPersonsDAO {
    private final CustomConnection mySqlConnection;

    public GrPersonsDAO() {
        mySqlConnection = new MySqlConnector();
    }

    /**
     * Seeks group-person entry in "groups_persons" table using person_id parameter
     *
     * @param personId unique int person_id value of groups-person pairs to find
     * @return List(GrPersons) returns List of GrPersons type object
     */
    public List<GrPersons> findAllGrPersonByPersonId(int personId) {
        List<GrPersons> result = new ArrayList<>();

        String query = "SELECT * FROM groups_persons WHERE person_id = " + personId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                GrPersons grPersons = new GrPersons();
                grPersons.setPersonId(resultSet.getInt("person_id"));
                grPersons.setGroupId(resultSet.getInt("group_id"));
                grPersons.setOfferId(resultSet.getInt("offer_id"));
                grPersons.setPairDate(resultSet.getDate("pair_date").toLocalDate());


                result.add(grPersons);
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return result;
    }

    /**
     * Checks if given person is already subscribed to given group
     *
     * @param groupId  unique int group_id
     * @param personId unique int person_id
     * @return boolean true if person is already in group
     */
    public boolean checkIfInGroup(int personId, int groupId) {

        String query = "SELECT count(*) FROM groups_persons gp " +
                "WHERE gp.person_id = " + personId + " AND gp.group_id = " + groupId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next() && resultSet.getInt("count(*)") > 0) {
                return true;
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return false;
    }

    /**
     * Checks number of group - persons connections in "groups_categories" by group_id
     *
     * @param groupId unique group_id in table "groups_persons"
     * @return int number places taken by normal people (not couch)
     */
    public int countPlaceInGroup(int groupId) {
        String query = "SELECT Count(*) FROM groups_persons " +
                "WHERE " +
                "offer_id IS NOT NULL " +
                "AND group_id = " + groupId;
        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("Count(*)");
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return 0;
    }

    /**
     * Checks if there is place in group for normal people (not couch)
     *
     * @param groupId unique group_id in table "groups_persons"
     * @return boolean true if there are places in group, false if not
     */
    public boolean isPlaceInGroup(int groupId) {
        GroupsDAO groupsDAO = new GroupsDAO();
        if (groupsDAO.groupSize(groupId) <= countPlaceInGroup(groupId)) {
            return false;
        }
        return true;
    }

    /**
     * Seeks all persons in "groups_categories" by group_id
     *
     * @param groupId unique group_id in table "groups_persons"
     * @return List of GrPersons type objects
     */
    public List<GrPersons> findAllGrPersonByGroupId(int groupId) {
        List<GrPersons> result = new ArrayList<>();

        String query = "SELECT * FROM groups_persons WHERE group_id = " + groupId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                GrPersons grPersons = new GrPersons();
                grPersons.setPersonId(resultSet.getInt("person_id"));
                grPersons.setGroupId(resultSet.getInt("group_id"));
                grPersons.setOfferId(resultSet.getInt("offer_id"));
                grPersons.setPairDate(resultSet.getDate("pair_date").toLocalDate());

                result.add(grPersons);
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return result;
    }

    /**
     * Seeks group-person entry in "groups_persons" table using person_id parameter
     * joining badges table using badge_id and joining persons table using person_id
     *
     * @param personId   unique int person_id value of person-badge pairs with joined objects to find
     * @param onlyFuture true or false, depends if program should look for future or past start dates of camps
     * @return List(PersBadge) returns List of PersBadge type object with joined Person type objects and joined Badge objects
     */
    public List<GrPersons> findJoinedGroupsPersonsByPersonId(int personId, boolean onlyFuture) {
        List<GrPersons> result = new ArrayList<>();

        String query = "SELECT * FROM groups_persons gp" +
                " JOIN camp.groups g ON gp.group_id = g.group_id" +
                " JOIN persons p ON gp.person_id = p.person_id" +
                " JOIN offers o ON gp.offer_id = o.offer_id" +
                " WHERE gp.person_id = " + personId;

        if (onlyFuture) {
            query = query + " AND start_date >= '" + Date.valueOf(LocalDate.now()) + "'";
        }

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                GrPersons grPerson = new GrPersons();
                grPerson.setPersonId(resultSet.getInt("gp.person_id"));
                grPerson.setGroupId(resultSet.getInt("gp.group_id"));
                grPerson.setOfferId(resultSet.getInt("gp.offer_id"));
                grPerson.setPairDate(resultSet.getDate("gp.pair_date").toLocalDate());

                Group group = new Group();
                group.setGroupId(resultSet.getInt("g.group_id"));
                group.setName(resultSet.getString("g.name"));
                group.setStartDate(resultSet.getDate("g.start_date").toLocalDate());
                group.setEndDate(resultSet.getDate("g.end_date").toLocalDate());

                Person person = new Person();
                person.setPersonId(resultSet.getInt("p.person_id"));
                person.setFirstName(resultSet.getString("p.first_name"));
                person.setLastName(resultSet.getString("p.last_name"));

                Offer offer = new Offer();
                offer.setOfferId(resultSet.getInt("o.offer_id"));
                offer.setPrice(resultSet.getInt("o.price"));

                grPerson.setGroup(group);
                grPerson.setPerson(person);
                grPerson.setOffer(offer);

                result.add(grPerson);
            }
            return result;
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Seeks group-person entry in "groups_persons" table using couch person_id parameter
     * joining badges table using badge_id and joining persons table using person_id
     *
     * @param personId unique int person_id value of person-badge pairs with joined objects to find
     * @return List(PersBadge) returns List of PersBadge type object with joined Person type objects and joined Badge objects
     */
    public List<GrPersons> findJoinedGroupsPersonsByPersonIdCouch(int personId, boolean onlyFuture) {
        List<GrPersons> result = new ArrayList<>();

        String query = "Select * FROM groups_persons gp" +
                " JOIN camp.groups g ON gp.group_id = g.group_id" +
                " JOIN persons p ON gp.person_id = p.person_id" +
                " WHERE gp.person_id = " + personId;

        if (onlyFuture) {
            query = query + " AND start_date >= '" + Date.valueOf(LocalDate.now()) + "'";
        }

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {

                GrPersons grPerson = new GrPersons();
                grPerson.setPersonId(resultSet.getInt("gp.person_id"));
                grPerson.setGroupId(resultSet.getInt("gp.group_id"));
                grPerson.setOfferId(resultSet.getInt("gp.offer_id"));
                grPerson.setPairDate(resultSet.getDate("gp.pair_date").toLocalDate());

                Group group = new Group();
                group.setGroupId(resultSet.getInt("g.group_id"));
                group.setName(resultSet.getString("g.name"));
                group.setStartDate(resultSet.getDate("g.start_date").toLocalDate());
                group.setEndDate(resultSet.getDate("g.end_date").toLocalDate());

                Person person = new Person();
                person.setPersonId(resultSet.getInt("p.person_id"));
                person.setFirstName(resultSet.getString("p.first_name"));
                person.setLastName(resultSet.getString("p.last_name"));

                grPerson.setGroup(group);
                grPerson.setPerson(person);

                result.add(grPerson);
            }
            return result;
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Saves new group-person entry in "groups_persons" table using GrPersons type object
     *
     * @param grPersons GrPersons type object
     */
    public void save(GrPersons grPersons) {
        String query = "INSERT INTO groups_persons " +
                "(person_id, " +
                "group_id, " +
                "offer_id, " +
                "pair_date) " +
                "VALUES " +
                "(?,?,?,?)";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, grPersons.getPersonId());
            statement.setInt(2, grPersons.getGroupId());
            if (grPersons.getOfferId() == null) {
                statement.setNull(3, Types.INTEGER);
            } else {
                statement.setInt(3, grPersons.getOfferId());
            }
            statement.setDate(4, Date.valueOf(LocalDate.now()));

            statement.executeUpdate();

            System.out.println("Zapisano powiązanie osoby z grupą.");
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Saves to table "groups_persons" group_id and person_id, which are unique values from tables "groups" and "persons"
     *
     * @param personId int, unique id of person
     * @param groupId  int, unique id of group
     */
    public void saveById(int personId, int groupId) {
        String query = "INSERT INTO groups_persons " +
                "(person_id, " +
                "group_id) " +
                "VALUES " +
                "(?,?)";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, personId);
            statement.setInt(2, groupId);

            statement.executeUpdate();

            System.out.println("Zapisano powiązanie osoby z grupą.");
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Updates existing group-person entry in "groups_persons" table using GrPersons type object
     *
     * @param newGrPersons GrPersons type object with new parameters
     * @param oldGrPersons GrPersons type object with previous parameters
     */
    public void update(GrPersons oldGrPersons, GrPersons newGrPersons) {
        String query = "UPDATE groups_persons SET " +
                "person_id = ?, " +
                "group_id = ? " +
                "WHERE " +
                "person_id = ? " +
                "AND group_id = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, newGrPersons.getPersonId());
            statement.setInt(2, newGrPersons.getGroupId());
            statement.setInt(3, oldGrPersons.getPersonId());
            statement.setInt(4, oldGrPersons.getGroupId());
            statement.executeUpdate();

            System.out.println("Dane zostały zaktualizowane.");
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Deletes existing group-person entry in "groups_persons" table using GrPersons type object
     *
     * @param grPersons unique PersBadge object with parameters like entry to delete
     */
    public void delete(GrPersons grPersons) {
        String query = "DELETE FROM groups_persons " +
                "WHERE " +
                "person_id = ? " +
                "AND group_id  = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, grPersons.getPersonId());
            statement.setInt(2, grPersons.getGroupId());
            statement.executeUpdate();
            System.out.println("Usunięto powiązanie osoby z grupą.");
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }
}
