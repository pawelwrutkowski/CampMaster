package dao;

import connection.CustomConnection;
import connection.MySqlConnector;
import entity.GrCategory;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class GrCategoriesDAO {
    private final CustomConnection mySqlConnection;

    public GrCategoriesDAO() {
        mySqlConnection = new MySqlConnector();
    }

    /**
     * Seeks group category entry in "groups_categories" table using group category Id parameter
     *
     * @param grCatId unique int category_Id value group category to find
     * @return GrCategory returns GrCategory type object
     */
    public GrCategory findById(int grCatId) {
        String query = "SELECT * FROM groups_categories WHERE category_id = " + grCatId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                GrCategory grCategory = new GrCategory();
                grCategory.setCategoryId(resultSet.getInt("category_id"));
                grCategory.setName(resultSet.getString("name"));
                grCategory.setDescription(resultSet.getString("description"));
                return grCategory;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Seeks actual max category_id value (autoincremented) in table groups_categories
     *
     * @return int max value of category_id in groups_categories table
     */
    public int findMaxGrCatId() {
        String query = "SELECT MAX(category_id) FROM camp.groups_categories";

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getInt("MAX(category_id)");
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return 0;
    }

    /**
     * Finds all group categories in "groups_categories" table
     *
     * @return List of GrCategory type objects
     */
    public List<GrCategory> findAll() {
        List<GrCategory> allGrCategories = new ArrayList<>();

        String query = "Select * from camp.groups_categories ";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {

                GrCategory grCategory = new GrCategory();
                grCategory.setCategoryId(resultSet.getInt("category_id"));
                grCategory.setName(resultSet.getString("name"));
                grCategory.setDescription(resultSet.getString("description"));

                allGrCategories.add(grCategory);
            }
            resultSet.close();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return allGrCategories;
    }

    /**
     * Prints all content of given List<GrCategory> object to console in an ordered and formatted manner
     *
     * @param groupsCatList List<GrCategory> object
     */
    public void printGrCategoriesList(List<GrCategory> groupsCatList) {
        System.out.println("Nadchodzące obozy:");
        for (GrCategory groupsCat : groupsCatList) {
            System.out.println("ID kategorii: " + groupsCat.getCategoryId() + "\t\tNazwa kategorii: " + groupsCat.getName() +
                    "\t\tOpis kategorii: " + groupsCat.getDescription() + "\n");
        }
    }

    /**
     * Saves new group category entry in "groups_categories" table using GrCategory type object
     *
     * @param grCategory GrCategory type object
     */
    public void save(GrCategory grCategory) {
        String query = "INSERT INTO groups_categories " +
                "(name, " +
                "description) " +
                "VALUES " +
                "(?,?)";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, grCategory.getName());
            statement.setString(2, grCategory.getDescription());
            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Updates existing group category entry in "groups_categories" table using GrCategory type object
     * Name and description entries can be updated using category_id parameter.
     *
     * @param grCategory GrCategory type object
     */
    public void update(GrCategory grCategory) {
        String query = "UPDATE groups_categories SET " +
                " name = ?," +
                " description = ?" +
                " WHERE category_id = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, grCategory.getName());
            statement.setString(2, grCategory.getDescription());
            statement.setInt(3, grCategory.getCategoryId());
            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Deletes existing group category entry in "groups_categories" table using int category ID value
     *
     * @param grCatId int category ID value
     */
    public void delete(int grCatId) {
        String query = "DELETE FROM groups_categories WHERE category_id = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, grCatId);
            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }
}
