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
public class OffersDAO {
    private final CustomConnection mySqlConnection;

    public OffersDAO() {
        mySqlConnection = new MySqlConnector();
    }

    /**
     * Seeks offer entry in "offers" table using offer_id parameter
     *
     * @param offerId unique int offer_id value of offer to find
     * @return Offer type object
     */
    public Offer findByOfferId(int offerId) {
        String query = "SELECT * FROM offers WHERE offer_id = " + offerId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {

                Offer offer = new Offer();
                offer.setOfferId(resultSet.getInt("offer_id"));
                offer.setGroupId(resultSet.getInt("group_id"));
                offer.setPrice(resultSet.getInt("price"));
                offer.setActive(resultSet.getInt("active"));

                return offer;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Seeks an active offer from "Offers" table, which status active equals 1,
     * and is connected to specified group.
     *
     * @param groupId - unique group_id parameter
     * @return Offer type object
     */
    public Offer findActiveByGroupId(int groupId) {
        String query = "SELECT * FROM offers o WHERE o.active = 1 AND o.group_id = " + groupId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {

                Offer offer = new Offer();
                offer.setOfferId(resultSet.getInt("o.offer_id"));
                offer.setGroupId(resultSet.getInt("o.group_id"));
                offer.setPrice(resultSet.getInt("o.price"));
                offer.setActive(resultSet.getInt("o.active"));

                return offer;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Seeks all offers from "Offers" table, which status 'active' equals 1
     *
     * @return List of Offer type objects with full data from Location, GrCategory and Group objects.
     */
    public List<Offer> findActiveOffersJoinGroups() {
        List<Offer> result = new ArrayList<>();

        String query = "Select * FROM offers o" +
                " JOIN camp.groups g ON o.group_id = g.group_id" +
                " JOIN groups_categories gc ON g.categories_id = gc.category_id" +
                " JOIN locations l ON g.location_id = l.location_id" +
                " WHERE o.active = 1";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Offer offer = new Offer();
                offer.setOfferId(resultSet.getInt("o.offer_id"));
                offer.setGroupId(resultSet.getInt("o.group_id"));
                offer.setPrice(resultSet.getInt("o.price"));
                offer.setActive(resultSet.getInt("o.active"));

                Group group = new Group();
                group.setGroupId(resultSet.getInt("g.group_id"));
                group.setName(resultSet.getString("g.name"));
                group.setCategoriesId(resultSet.getInt("g.categories_id"));
                group.setLocationId(resultSet.getInt("g.location_id"));
                group.setStartDate(resultSet.getDate("g.start_date").toLocalDate());
                group.setEndDate(resultSet.getDate("g.end_date").toLocalDate());
                group.setAgeRange(resultSet.getInt("g.age_range"));

                GrCategory grCategory = new GrCategory();
                grCategory.setCategoryId(resultSet.getInt("gc.category_id"));
                grCategory.setName(resultSet.getString("gc.name"));
                grCategory.setDescription(resultSet.getString("gc.description"));

                Location location = new Location();
                location.setLocationId(resultSet.getInt("l.location_id"));
                location.setName(resultSet.getString("l.name"));
                location.setCity(resultSet.getString("l.city"));
                location.setPostalCode(resultSet.getString("l.postal_code"));

                group.setGrCategory(grCategory);
                group.setLocation(location);

                offer.setGroup(group);

                result.add(offer);
            }
            return result;
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Seeks actual max offer_id value (autoincremented) in table offers
     *
     * @return int max value of offer_id in offers table
     */
    public int findMaxOfferId() {
        String query = "SELECT MAX(offer_id) FROM offers";

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getInt("MAX(offer_id)");
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return 0;
    }

    /**
     * Prints every position from list of offers
     *
     * @param offersList List<Offer>
     *                   void - prints list on console
     */
    public void printOffersList(List<Offer> offersList) {
        for (Offer each : offersList) {
            System.out.println("ID oferty: " + each.getOfferId() +
                    "\tNazwa: " + each.getGroup().getName() +
                    "\t\tOpis: " + each.getGroup().getGrCategory().getDescription() +
                    "\t\tTermin: od " + each.getGroup().getStartDate() + " do " + each.getGroup().getEndDate() +
                    "\t\tCena: " + each.getPrice());
        }
    }

    /**
     * Checks 'active' parameter of offer in table "Offers".
     *
     * @param offerId int, unique offer_id
     * @return boolean
     */
    public boolean checkIfActive(int offerId) {
        String query = "SELECT active FROM offers WHERE offer_id = " + offerId;

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return false;
    }

    /**
     * Saves new offer entry in "offers" table using Offer type object
     *
     * @param offer Offer type object
     */
    public void save(Offer offer) {
        String query = "INSERT INTO offers " +
                "(group_id, " +
                "price) " +
                "VALUES " +
                "(?,?)";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, offer.getGroupId());
            statement.setInt(2, offer.getPrice());
            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Updates existing offer entry in "offers" table using Offer type object
     *
     * @param offer Offer type object
     */
    public void update(Offer offer) {
        String query = "UPDATE offers SET " +
                "price = ? " +
                "WHERE offer_id = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, offer.getPrice());
            statement.setInt(2, offer.getOfferId());

            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Changes 'active' parameter of offer in table "Offers" to 0, which means that the offer has ended.
     *
     * @param offerId int, unique offer_id
     */
    public void deactivate(int offerId) {
        String query = "UPDATE offers SET " +
                "active = 0 " +
                "WHERE offer_id = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, offerId);

            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Changes 'active' parameter of offers in table "Offers" to 0.
     * It affects these offers, which has active field value = 1,
     * and which have group_id of groups, that have start_date field in the past.
     * Which means that all the offers of obsolete groups are now also obsolete.
     * Used when opening OffersMenu
     */
    public void deactivateObsolete() {
        String query = "UPDATE offers o SET " +
                "o.active = 0 " +
                "WHERE o.active = 1 " +
                "AND o.group_id = (SELECT g.group_id FROM camp.groups g " +
                "WHERE g.start_date < '" + Date.valueOf(LocalDate.now()) + "')";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.execute();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Changes 'active' parameter of obsolete offer with offer_id =9 in table "Offers" to 1.
     * It is used only to test method "deactivateObsolete()" in Tests
     */
    public void activateOfferNineToTest() {
        String query = "UPDATE camp.offers o SET o.active = 1 WHERE o.offer_id = 9";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.execute();

            log.info(statement);
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Deletes offer entry in "offers" table using offer_id parameter
     *
     * @param offerId unique int offer_id value
     */
    public void delete(int offerId) {
        String query = "DELETE FROM offers WHERE offer_id = ?";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, offerId);

            statement.executeUpdate();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }
}

