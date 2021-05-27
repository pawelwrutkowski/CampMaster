package dao;

import connection.CustomConnection;
import connection.MySqlConnector;
import entity.*;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class GroupsDAO {
    private static final CustomConnection mySqlConnection = new MySqlConnector();

    /**
     * Changes the groups 'Age Range' parameter from int (0 or 1) to string (name of age category)
     *
     * @param ageRange int
     * @return String
     */
    public String nameAgeRange(int ageRange) {
        if (ageRange == 1) {
            return "Starszaki";
        } else if (ageRange == 0) {
            return "Młodziaki";
        } else {
            return ("Błąd danych");
        }
    }

    /**
     * Seeks group entry in "groups" table using group_id parameter
     *
     * @param groupId unique int group_id value of group to find
     * @return Group type object
     */
    public Group findByGroupId(int groupId) {
        String query = "SELECT * FROM camp.groups WHERE group_id =" + groupId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {  //result set głównie do odbioru ale da się go przerobić do zapisu

                Group group = new Group();

                group.setGroupId(resultSet.getInt("group_id"));
                group.setName(resultSet.getString("name"));
                group.setLocationId(resultSet.getInt("location_id"));
                group.setAgeRange(resultSet.getInt("age_range"));
                group.setCategoriesId(resultSet.getInt("categories_id"));
                group.setStartDate(resultSet.getDate("start_date").toLocalDate());
                group.setEndDate(resultSet.getDate("end_date").toLocalDate());
                group.setSize(resultSet.getInt("size"));

                return group;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Seeks size value of group using group ID value
     *
     * @param groupId int group ID value
     * @return int size value of group
     */
    public int groupSize(int groupId) {
        String query = "SELECT size FROM camp.groups WHERE group_id = " + groupId;
        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("size");
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return 0;
    }

    /**
     * Seeks age_range value of group using group ID value
     *
     * @param groupId int group ID value
     * @return int age_range value of group
     */
    public int getGroupAgeRange(int groupId) {
        String query = "SELECT g.age_range FROM camp.groups g WHERE g.group_id = " + groupId;
        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("g.age_range");
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return -1;
    }

    /**
     * Seeks actual max group_id value (autoincremented) in table groups
     *
     * @return int max value of group_id in group table
     */
    public int findMaxGroupId() {
        String query = "SELECT MAX(group_id) FROM camp.groups";

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getInt("MAX(group_id)");
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return 0;
    }

    /**
     * Seeks actual max group_id value (autoincremented) in table groups
     *
     * @return int max value of group_id in group table
     */
    public boolean checkIfFuture(int groupId) {
        String query = "SELECT count(*) FROM camp.groups g" +
                " WHERE g.group_id = " + groupId +
                " AND start_date >= '" + Date.valueOf(LocalDate.now()) + "'";

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next() && resultSet.getInt("count(*)") != 0) {
                return true;
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return false;
    }

    /**
     * Finds all groups in "groups" table
     *
     * @return List of Group type objects
     */
    public List<Group> findAll() {
        List<Group> allGroups = new ArrayList<>();

        String query = "Select * from camp.groups ";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {

                Group group = new Group();
                group.setGroupId(resultSet.getInt("group_id"));
                group.setName(resultSet.getString("name"));
                group.setLocationId(resultSet.getInt("location_id"));
                group.setAgeRange(resultSet.getInt("age_range"));
                group.setCategoriesId(resultSet.getInt("categories_id"));
                group.setStartDate(resultSet.getDate("start_date").toLocalDate());
                group.setEndDate(resultSet.getDate("end_date").toLocalDate());
                group.setSize(resultSet.getInt("size"));

                allGroups.add(group);
            }
            resultSet.close();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return allGroups;
    }

    /**
     * Seeks all groups from "groups" table, which start_date is after today
     *
     * @return List of Group type objects with full data from Location, GrCategory.
     */
    public List<Group> findAllFutureJoinGrCatLocation() {
        List<Group> allFutureGroups = new ArrayList<>();

        String query = "Select * from camp.groups g " +
                "JOIN groups_categories gc ON g.categories_id = gc.category_id " +
                "JOIN locations l ON g.location_id = l.location_id " +
                "WHERE start_date >= '" + Date.valueOf(LocalDate.now()) + "'";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {

                Group group = new Group();
                group.setGroupId(resultSet.getInt("group_id"));
                group.setName(resultSet.getString("name"));
                group.setLocationId(resultSet.getInt("location_id"));
                group.setAgeRange(resultSet.getInt("age_range"));
                group.setCategoriesId(resultSet.getInt("categories_id"));
                group.setStartDate(resultSet.getDate("start_date").toLocalDate());
                group.setEndDate(resultSet.getDate("end_date").toLocalDate());
                group.setSize(resultSet.getInt("size"));

                Location location = new Location();
                location.setLocationId(resultSet.getInt("l.location_id"));
                location.setName(resultSet.getString("l.name"));
                location.setCity(resultSet.getString("l.city"));

                GrCategory grCategory = new GrCategory();
                grCategory.setCategoryId(resultSet.getInt("gc.category_id"));
                grCategory.setName(resultSet.getString("gc.name"));
                grCategory.setDescription(resultSet.getString("gc.description"));

                group.setLocation(location);
                group.setGrCategory(grCategory);
                allFutureGroups.add(group);
            }
            resultSet.close();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return allFutureGroups;
    }

    /**
     * Prints all content of given List<Group> object to console in an ordered and formatted manner
     *
     * @param groupList List<Group> object with joined location and group category data
     */
    public void printGroupsList(List<Group> groupList) {
        System.out.println("Nadchodzące obozy:");
        for (Group group : groupList) {
            System.out.println("ID obozu: " + group.getGroupId() + "\t\tNazwa grupy: " + group.getName() +
                    "\t\tKategoria: " + group.getGrCategory().getName() + "\t\tKategoria wiekowa: " + nameAgeRange(group.getAgeRange()) +
                    "\n\tOpis: " + group.getGrCategory().getDescription() +
                    "\n\tPoczątek obozu: " + group.getStartDate() + "\tKoniec obozu: " + group.getEndDate() +
                    "\tLokalizacja: " + group.getLocation().getName() + "\t\tMiasto: " + group.getLocation().getCity() + "\n");
        }
    }

    /**
     * Saves new group entry in "groups" table using Group type object
     *
     * @param group Group type object
     */
    public void save(Group group) {
        String query = "INSERT INTO camp.groups " +
                "(name," +
                "location_id," +
                "age_range," +
                "categories_id," +
                "start_date," +
                "end_date," +
                "size)" +
                "VALUES" +
                "(?,?,?,?,?,?,?)";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, group.getName());
            statement.setInt(2, group.getLocationId());
            statement.setInt(3, group.getAgeRange());
            statement.setInt(4, group.getCategoriesId());
            statement.setDate(5, Date.valueOf(group.getStartDate()));
            statement.setDate(6, Date.valueOf(group.getEndDate()));
            statement.setInt(7, group.getSize());

            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Updates existing group entry in "groups" table using Group type object
     *
     * @param group Group type object
     */
    public void update(Group group) {
        String query = "UPDATE camp.groups " +
                "SET name = ?, " +
                "location_id= ?, " +
                "age_range= ?, " +
                "categories_id =?," +
                "start_date =?," +
                "end_date =?," +
                "size= ? " +
                "WHERE group_id = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, group.getName());
            statement.setInt(2, group.getLocationId());
            statement.setInt(3, group.getAgeRange());
            statement.setInt(4, group.getCategoriesId());
            statement.setDate(5, Date.valueOf(group.getStartDate()));
            statement.setDate(6, Date.valueOf(group.getEndDate()));
            statement.setInt(7, group.getSize());

            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Deletes group entry in "groups" table using group_id parameter
     *
     * @param groupId unique int group_id value
     */
    public void delete(int groupId) {
        String query = "DELETE FROM camp.groups WHERE group_id= ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, groupId);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }
}
