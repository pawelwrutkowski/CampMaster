package dao;

import connection.CustomConnection;
import connection.MySqlConnector;
import entity.Location;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j

public class LocationsDAO {
    private final CustomConnection mySqlConnection;

    public LocationsDAO() { mySqlConnection = new MySqlConnector();}

    /**
     * Seeks location entry in "locations" table using location_id parameter
     * @param locationId unique int location_id value of location to find
     * @return Location type object
     */
    public Location findById(int locationId) {
        String query = "SELECT * FROM locations WHERE location_id = " + locationId;

        log.info(query);

        try(Connection connection = mySqlConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {

            if(resultSet.next()) {
                log.info(resultSet.getString("name"));

                Location location = new Location();
                location.setLocationId(resultSet.getInt("location_id"));
                location.setName(resultSet.getString("name"));
                location.setCity(resultSet.getString("city"));
                location.setPostalCode(resultSet.getString("postal_code"));
                return location;

            }

        } catch(SQLException sqlException) {
                log.error(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Finds all locations data in "locations" table
     * @return List of Location type objects
     */
    public List<Location> findAll() {
        List<Location> allLocations = new ArrayList<>();

        String query = "Select * from camp.locations ";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {

                Location location = new Location();
                location.setLocationId(resultSet.getInt("location_id"));
                location.setName(resultSet.getString("name"));
                location.setCity(resultSet.getString("city"));
                location.setPostalCode(resultSet.getString("postal_code"));


                allLocations.add(location);
            }
            resultSet.close();

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return allLocations;
    }

    /**
     * Prints all content of given List<Location> object to console in an ordered and formatted manner
     * @param locationsList List<Location> object
     */
    public void printLocationsList(List<Location> locationsList) {
        System.out.println("Nadchodzące obozy:");
        for (Location location : locationsList) {
            System.out.println("ID lokalizacji: " + location.getLocationId() + "\t\tNazwa: " + location.getName() +
                    "\t\tMiasto: " + location.getCity() + "\t\tKod pocztowy: " + location.getPostalCode() +"\n");
        }
    }

    /**
     * Seeks actual max location_id value (autoincremented) in table locations
     * @return int max value of location_id in locations table
     */
    public int findMaxLocationId() {
        String query = "SELECT MAX(location_id) FROM camp.locations";

        try (Connection connection = mySqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getInt("MAX(location_id)");
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return 0;
    }

    /**
     * Saves new location entry in "locations" table using Location type object
     * @param location Location type object
     */
    public void save(Location location) {
        String query = "INSERT INTO locations " +
                "(name, " +
                "city, " +
                "postal_code) " +
                "VALUES " +
                "(?,?,?)";

        try (Connection connection = mySqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, location.getName());
            statement.setString(2, location.getCity());
            statement.setString(3, location.getPostalCode());
            statement.executeUpdate();

        } catch(SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Updates existing location entry in "locations" table using Location type object
     * @param location Location type object
     */
    public void update(Location location) {
        String query = "UPDATE locations SET " +
                "name = ?, " +
                "city = ?, " +
                "postal_code = ? " +
                "WHERE location_id = ?";

        try(Connection connection = mySqlConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, location.getName());
            statement.setString(2, location.getCity());
            statement.setString(3, location.getPostalCode());
            statement.setInt(4, location.getLocationId());

            statement.executeUpdate();

        } catch(SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Deletes location entry in "locations" table using location_id parameter
     * @param locationId unique int location_id value
     */
    public void delete(int locationId) {
        String query = "DELETE FROM locations WHERE location_id = ?";

        try(Connection connection = mySqlConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, locationId);
            statement.executeUpdate();

        } catch(SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
    }
}
