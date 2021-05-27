
import connection.CustomConnection;
import connection.MySqlConnector;
import dao.*;
import entity.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DAOTests {

    private final PersonsDAO personsDAO;
    private final LocationsDAO locationsDAO;
    private final UserDAO userDAO;
    private final GroupsDAO groupDao;
    private final BadgesDAO badgesDAO;
    private final GrCategoriesDAO grCategoriesDAO;
    private final OffersDAO offersDao;
    private final GrPersonsDAO grPersonsDAO;
    private final PersBadgesDAO persBadgesDAO;
    private final RolesDAO rolesDAO;

    public DAOTests() {
        this.personsDAO = new PersonsDAO();
        this.locationsDAO = new LocationsDAO();
        this.userDAO = new UserDAO();
        this.groupDao = new GroupsDAO();
        this.badgesDAO = new BadgesDAO();
        this.grCategoriesDAO = new GrCategoriesDAO();
        this.offersDao= new OffersDAO();
        this.grPersonsDAO = new GrPersonsDAO();
        this.persBadgesDAO = new PersBadgesDAO();
        this.rolesDAO = new RolesDAO();
    }

    @Test(testName = "Test 1 - Połącz się ze swoim serwerem bazy danych MySQL")
    public void shouldConnectToDatabase() throws SQLException {
        CustomConnection mySqlConnection = new MySqlConnector();
        Connection connection = mySqlConnection.getConnection();

        Assert.assertNotNull(connection);
        Assert.assertNotNull(connection.getMetaData().getURL());
    }

    @Test(testName = "Test 2 - Wyszukaj osobę po identyfikatorze")
    public void shouldFindPersonById() {
        Person result = personsDAO.findById(1);
        //System.out.println(result.toString());  //drukujemy dla sprawdzenia
        Assert.assertNotNull(result);
    }

    @Test(testName = "Test 2.5 - Wyszukaj osobę po email")
    public void shouldFindPersonByEmail() {
        Person result = personsDAO.findByEmail("tiktak@campmaster.pl");
        //System.out.println(result.toString());  //drukujemy dla sprawdzenia
        Assert.assertNotNull(result);
    }

    @Test(testName = "Lista dzieci rodzica po personID rodzica")
    public void shouldGetChildListListByParentPersonId() {

        List <Person> findPerParPerID = personsDAO.createListOfParentsChildren(15);
        System.out.println(findPerParPerID.toString());
        Assert.assertNotNull(findPerParPerID);
    }

    @Test(testName = "Test 3 - Wyszukaj największy person_id")
    public void shouldFindMaxPersonId() {
        Integer result = personsDAO.findMaxPersonId();
        Assert.assertNotNull(result);
    }

    @Test(testName = "zad. 4 - Dodaj nową osobę")
    public void shouldSavePerson() {
        int oldMaxPersonID = personsDAO.findMaxPersonId();
        Person personToSave = new Person();


        //personToSave.setPersonId(2);
        personToSave.setFirstName("Rocky");
        personToSave.setLastName("Balboa");
        personToSave.setEmail("rocky@italianstallion.com");
        personToSave.setPassword("eyeofthetiger");
        personToSave.setBirthDate(LocalDate.parse("1960-11-23"));
        personToSave.setRoleId(1);
        personToSave.setPhoneNumber("+48111222333");
        //personToSave.setParentId();
        personToSave.setPrivilegeId(2);

        personsDAO.save(personToSave);

        int newMaxPersonID = personsDAO.findMaxPersonId();

        Person savedPerson = personsDAO.findById(newMaxPersonID);

        Assert.assertNotNull(savedPerson);
        Assert.assertEquals(newMaxPersonID, (oldMaxPersonID + 1));
    }

    @Test(testName = "zad. 5 - Zmień dane person")
    public void shouldUpdatePersonData() {
        String oldEntryToChange;
        String newEntryAfterChange;

        Person personBeforeUpdate = personsDAO.findByEmail("rocky@italianstallion.com");
        oldEntryToChange = personBeforeUpdate.getPhoneNumber();

        personBeforeUpdate.setPhoneNumber("+48666123666");
        personBeforeUpdate.setPrivilegeId(3);
        personsDAO.update(personBeforeUpdate);

        Person personAfterUpdate = personsDAO.findByEmail("rocky@italianstallion.com");
        newEntryAfterChange = personAfterUpdate.getPhoneNumber();

        Assert.assertNotNull(personBeforeUpdate);
        Assert.assertNotNull(personAfterUpdate);
        Assert.assertNotNull(newEntryAfterChange);
        Assert.assertNotNull(oldEntryToChange);
        Assert.assertNotEquals(newEntryAfterChange, oldEntryToChange);
    }

    @Test(testName = "zad. 6 - Usuń osobę na podstawie person_id")
    public void shouldDeletePersonByPersonId() {
        int personId = 18;

        personsDAO.delete(personId);

        Person deletedPerson = personsDAO.findById(personId);
        Assert.assertNull(deletedPerson);
    }

    @Test(testName = "zad. 6.5 - Usuń osobę na podstawie email")
    public void shouldDeletePersonByEmail() {
        String email = "rocky@italianstallion.com";

        personsDAO.deleteByEmail(email);

        Person deletedPerson = personsDAO.findByEmail(email);
        Assert.assertNull(deletedPerson);
    }

    @Test(testName = "Test 7 Stwórz obiekt user z poprawnymi danymi logowania")
    public void shouldCreateUserByEmailAndPassword() {
        String email = "tiktak@campmaster.pl";
        String password = "jajestempantiktak";
        User user = userDAO.signIn(email, password);
        String userName = user.getFirstName();

        String wrongEmail = "wrong@example.com";
        String wrongPassword = "JAjeStemPantikTAK";

        User user2 = userDAO.signIn(wrongEmail, wrongPassword);
        User user3 = userDAO.signIn(wrongEmail, password);
        User user4 = userDAO.signIn(email, wrongPassword);

        Assert.assertNotNull(user);
        Assert.assertEquals(userName, "Pan");
        Assert.assertNull(user2);
        Assert.assertNull(user3);
        Assert.assertNull(user4);

        /* TO DO hasło powinno uwzględniać wielkość liter, póki co takie samo hasło z różną wielkością liter utworzy użytkownika.
            wrongPassword = "JAjeStemPantikTAK";
            dao.User user5 = dao.User.signIn(email, wrongPassword;
            Assert.assertNull(user5);
         */
    }


    //TESTY DO LOKALIZACJI
    @Test(testName = "Test 7.5 Znajdź lokalizację po id")
    public void shouldFindLocationById() {
        Location findLocation = locationsDAO.findById(6);
        Assert.assertNotNull(findLocation);
    }

    @Test(testName = "Test 8 Stwórz lokalizację i ja znajdź")
    public void shouldCreateLocation() {
        Location location = new Location();
        location.setName("Nazwa lokalizacji");
        location.setCity("Miasto lok");
        location.setPostalCode("69-666");

        locationsDAO.save(location);
        //Trzeba sprawdzić jakie id wolne, bo autoinkrementacja
        //Tu można zrobić po dodaniu lokacji analog findMaxPersonId,
        // bo szukamy maksymalnego (nowo przydzielonego przez inkrementację) location_id
        Location findLocation = locationsDAO.findById(6);

        Assert.assertNotNull(findLocation);
        Assert.assertEquals(location.getName(), findLocation.getName());
    }

    @Test(testName = "Test 9 Zaktualizuj pole w lokalizacji")
    public void shouldUpdateLocation() {
        Location location = locationsDAO.findById(2);

        String nameBeforeUpdate = location.getName();
        location.setName("Nowa nazwa");
        locationsDAO.update(location);
        location = locationsDAO.findById(2);
        Assert.assertNotEquals(nameBeforeUpdate, location.getName());
    }

    @Test(testName = " Test 10 Usuń lokalizacje")
    public void shouldDeleteLocation() {
        locationsDAO.delete(2);
        Location deletedLocation = locationsDAO.findById(2);

        Assert.assertNull(deletedLocation);
    }

    // TESTY DO GROUPDAO


    @Test(testName = "Test - Wyszukaj grupę po id ")
    public void shouldFindGroupById() {
        Group result = groupDao.findByGroupId(1);
        System.out.println(result.toString());  //drukujemy dla sprawdzenia
        Assert.assertNotNull(result);
    }

    @Test(testName = "Wszystkie przyszłe grupy")
    public void shouldFindFutureGroups() {
        List <Group> findFutureGroups = groupDao.findAllFutureJoinGrCatLocation();
        groupDao.printGroupsList(findFutureGroups);
        Assert.assertNotNull(findFutureGroups);
    }


    @Test(testName = "Test - Wyszukaj największy group_id")
    public void shouldFindMaxGroupId() {
        Integer result = groupDao.findMaxGroupId();
        Assert.assertNotNull(result);
        Assert.assertNotEquals(result,0);
        System.out.println(result);
    }

    @Test(testName = "Utwórz nową grupę")
    public void shouldSaveGroup() {
        Group groupToSave= new Group();

        groupToSave.setGroupId(2);
        groupToSave.setName("Basketball");
        groupToSave.setLocationId(2);
        groupToSave.setAgeRange(3);
        groupToSave.setCategoriesId(2);
        groupToSave.setStartDate(LocalDate.parse("2021-05-01"));
        groupToSave.setEndDate(LocalDate.parse("2021-05-20"));
        groupToSave.setSize(34);


        groupDao.save(groupToSave);

        Group savedGroup= groupDao.findByGroupId(2);

        Assert.assertNotNull(savedGroup);

    }

    @Test(testName = " Usuń Grupę")
    public void shouldDeleteGroup() {
        groupDao.delete(2);
        Group deletedGroup = groupDao.findByGroupId(2);
        Assert.assertNull(deletedGroup);
    }

    @Test(testName = "Test 9 Zaktualizuj pole w grupie")
    public void shouldUpdateGroup() {

    Group group = groupDao.findByGroupId(2);
        String nameBeforeUpdate = group.getName();
        group.setName("Nowa nazwa grupy");

      //groupDao.update(group);
        group = groupDao.findByGroupId(2);
        Assert.assertNotEquals(nameBeforeUpdate, group.getName());
    }

    @Test(testName = " Usuń Grupę")
    public void shouldCheckIfFuture() {
        Assert.assertTrue(groupDao.checkIfFuture(12));
        Assert.assertFalse(groupDao.checkIfFuture(7));
    }

    //TESTY DO ODZNAK

    @Test(testName = "Test 7.5 Znajdź odznakę po id")
    public void shouldFindBadgeById() {
        Badge findBadge = badgesDAO.findById(1);
        Assert.assertNotNull(findBadge);
    }

    @Test(testName = "Stwórz odznakę i ja znajdź")
    public void shouldCreateBadge() {
        Badge badge = new Badge();
        badge.setName("Super odznaka");
        badge.setDescription("Tylko dla super ludzi");

        badgesDAO.save(badge);
        Badge findBadge = badgesDAO.findById(1);

        Assert.assertNotNull(findBadge);
        Assert.assertEquals(badge.getName(), findBadge.getName());
    }


/*
    @Test(testName = "Zaktualizuj pole w odznace")
    public void shouldUpdateBadge() {
        Badge badge = badgesDAO.findById(1);
        String nameBeforeUpdate = badge.getName();
        badge.setName("Super ekstra odznaka");
        badgesDAO.update(badge);
        badge = badgesDAO.findById(1);

        Assert.assertNotEquals(nameBeforeUpdate, badge.getName());
    }
*/
    @Test(testName = "Usuń odznakę")
    public void shouldDeleteBadge() {
        badgesDAO.delete(1);
        Badge deletedBadge = badgesDAO.findById(1);

        Assert.assertNull(deletedBadge);
    }

    //TESTY DO KATEGORII GRUP

    @Test(testName = "Test 7.5 Znajdź odznakę po id")
    public void shouldFindGroupCategById() {
        GrCategory findCat = grCategoriesDAO.findById(1);
        Assert.assertNotNull(findCat);
    }

    @Test(testName = "Test - Wyszukaj największy category_id")
    public void shouldFindMaxGroupCatId() {
        Integer result = grCategoriesDAO.findMaxGrCatId();
        Assert.assertNotNull(result);
        Assert.assertNotEquals(result,0);
        System.out.println(result);
    }

    @Test(testName = "Stwórz kategorię i ja znajdź")
    public void shouldCreateCategory() {
        GrCategory cat = new GrCategory();
        cat.setName("Sprawnościowy");
        cat.setDescription("Ostry wysiłek");

        grCategoriesDAO.save(cat);
        GrCategory findCat = grCategoriesDAO.findById(1);

        Assert.assertNotNull(findCat);
        Assert.assertEquals(cat.getName(), findCat.getName());
    }

    @Test(testName = "Zaktualizuj pole w kategorii")
    public void shouldUpdateCategory() {
        GrCategory cat = grCategoriesDAO.findById(1);
        String nameBeforeUpdate = cat.getName();
        cat.setName("Sprawnościowy + drużynowy");
        grCategoriesDAO.update(cat);
        cat = grCategoriesDAO.findById(1);

        Assert.assertNotEquals(nameBeforeUpdate, cat.getName());
    }

    @Test(testName = "Usuń kategorię")
    public void shouldDeleteCategory() {
        grCategoriesDAO.delete(1);
        GrCategory deletedCat = grCategoriesDAO.findById(1);

        Assert.assertNull(deletedCat);
    }

    // Testy do offers

    @Test(testName = "Test Znajdź ofertę po id")
    public void shouldFindOfferById() {
        Offer result = offersDao.findByOfferId(15);
        System.out.println(result.toString());
        Assert.assertNotNull(result);
    }

    @Test(testName = "Test Znajdź aktywne oferty")
    public void shouldFindActiveOffers() {
        List <Offer> result = offersDao.findActiveOffersJoinGroups();
        for (Offer each : result) {
            System.out.println(each.toString());
        }
        Assert.assertNotNull(result);
    }

    @Test(testName = "Stwórz ofertę")
    public void shouldCreateOffer() {
        int oldId = offersDao.findMaxOfferId();
        Offer offer= new Offer();
        offer.setGroupId(7);
        offer.setPrice(1500);
        offersDao.save(offer);
       int newId = offersDao.findMaxOfferId();
        Offer findOffer= offersDao.findByOfferId(newId);
        Assert.assertNotNull(findOffer);
        Assert.assertEquals(offer.getPrice(), findOffer.getPrice());
        Assert.assertEquals(oldId, (newId-1));
    }
    //Wyłączamy opcję update - ofertę można tylko dodać lub inaktywować
    @Test(testName = "Zaktualizuj pole w Offers")
    public void shouldUpdateOffer() {
        Offer offer= offersDao.findByOfferId(2);
        Integer priceBeforUpdate= offer.getPrice();
        offer.setPrice(8000);
        offersDao.update( offer);
        offer= offersDao.findByOfferId(2);
        Assert.assertNotEquals(priceBeforUpdate, offer.getPrice());
    }

    @Test(testName = "Usuń ofertę")
    public void shouldDeleteOffer() {
        offersDao.delete(17);
        Offer deleteOffer = offersDao.findByOfferId(17);

        Assert.assertNull(deleteOffer);
    }

    @Test(testName = "Dezaktywuj ofertę")
    public void shouldInactivateOffer() {
        int offerId = 1;
        offersDao.deactivate(offerId);
        Offer checkOffer = offersDao.findByOfferId(offerId);

        Assert.assertEquals(checkOffer.getActive(), 0);
    }

    @Test(testName = "Sprawdź dezaktywację ofert dla wszystkich przestarzałych grup")
    public void shouldDeactivateObsolete() {
        offersDao.activateOfferNineToTest();
        int sizeBefore = offersDao.findActiveOffersJoinGroups().size();
        offersDao.deactivateObsolete();
        int sizeAfter = offersDao.findActiveOffersJoinGroups().size();

        Assert.assertNotEquals(sizeBefore,sizeAfter);
    }


    //Testy do GroupPersons

    @Test(testName = "Test XX Lista grup dla danej osoby")
    public void shouldGetGrPersonsListByPersonId() {

        List <GrPersons> findGrPer = grPersonsDAO.findAllGrPersonByPersonId(5);
        System.out.println(findGrPer.toString());
        Assert.assertNotNull(findGrPer);
    }

    @Test(testName = "Test XX Lista osób dla danej grupy")
    public void shouldGetGrPersonsListByGroupId() {

        List <GrPersons> findGrPer = grPersonsDAO.findAllGrPersonByGroupId(2);
        System.out.println(findGrPer.toString());
        Assert.assertNotNull(findGrPer);
    }

    @Test(testName = "Test XX wypisz GrPersons połączone z Person, Groups i Offers, z przełącznikiem na oferty wszystkie lub przyszłe")
    public void shouldGetGrPersonsByPersonIdJoinGroupsPersonsOffers() {

        List <GrPersons> findGrPer = grPersonsDAO.findJoinedGroupsPersonsByPersonId(3, true);
        System.out.println(findGrPer.toString());
        Assert.assertNotNull(findGrPer);
    }

    @Test(testName = "Stwórz powiązanie person - group")
    public void shouldCreateGroupPersonConnection() {
        GrPersons grPersons = new GrPersons();
        int personId = 17;
        int groupId = 1;
     //   int offerId = 1;
        grPersons.setPersonId(personId);
        grPersons.setGroupId(groupId);
        //grPersons.setOfferId(offerId);

        grPersonsDAO.save(grPersons);
       // List<GrPersons> findGPbyPerson = grPersonsDAO.findByPersonId(personId);
      //  List<GrPersons> findGPbyGroup = grPersonsDAO.findByGroupId(groupId);

        //Assert.assertTrue(findGPbyPerson.stream().findAny(groupId).stream().collect() && findGPbyGroup.stream().findAny(personId));
        //Assert.assertTrue(findGPbyPerson.contains(Integer.valueOf(groupId)) && findGPbyGroup.contains(Integer.valueOf(personId)));
    }

    @Test(testName = "Zaktualizuj pole w groups_persons")
    public void shouldUpdateGroupPersonEntry() {
        int oldPersonId = 3;
        int oldGroupId = 8;
        int newPersonId = 3;
        int newGroupId = 9;

        GrPersons oldGrPersons = new GrPersons();
        GrPersons newGrPersons = new GrPersons();

        oldGrPersons.setPersonId(oldPersonId);
        oldGrPersons.setGroupId(oldGroupId);

        newGrPersons.setPersonId(newPersonId);
        newGrPersons.setGroupId(newGroupId);

        grPersonsDAO.update(oldGrPersons, newGrPersons);

        List<GrPersons> findGPbyPerson = grPersonsDAO.findAllGrPersonByPersonId(newPersonId);
        List<GrPersons> findGPbyGroup = grPersonsDAO.findAllGrPersonByGroupId(newGroupId);

        Assert.assertFalse(findGPbyPerson.contains(oldGrPersons) && findGPbyGroup.contains(oldGrPersons));
        Assert.assertTrue(findGPbyPerson.contains(newGrPersons) && findGPbyGroup.contains(newGrPersons));
    }

    @Test(testName = "Usuń powiązanie person - group")
    public void shouldDeleteGroupPersonConnection() {
        GrPersons grPersons = new GrPersons();
        int personId = 3;
        int groupId = 9;
        grPersons.setPersonId(personId);
        grPersons.setGroupId(groupId);

        grPersonsDAO.delete(grPersons);
        List<GrPersons> findGPbyPerson = grPersonsDAO.findAllGrPersonByPersonId(personId);
        List<GrPersons> findGPbyGroup = grPersonsDAO.findAllGrPersonByGroupId(groupId);

        Assert.assertFalse(findGPbyPerson.contains(grPersons) && findGPbyGroup.contains(grPersons));
    }

    @Test(testName = "Test sprawdzania, czy dana osoba jest już wpisana do grupy")
    public void shouldCheckIfPersonIsInGroup() {
        int isForSurePersonId1 = 16;
        int isForSurePersonId2 = 5;

        int forSureNotPersonId = 1;

        int groupId = 2;
        Assert.assertTrue(grPersonsDAO.checkIfInGroup(isForSurePersonId1,groupId));
        Assert.assertTrue(grPersonsDAO.checkIfInGroup(isForSurePersonId2,groupId));
        Assert.assertFalse(grPersonsDAO.checkIfInGroup(forSureNotPersonId,groupId));

    }

    //Testy do PersonsBadges

    @Test(testName = "Test XX Lista grup dla danego id osoby")
    public void shouldGetPersBadgesListByPersonId() {

        List <PersBadge> findPerBad = persBadgesDAO.findByPersonId(4);
        System.out.println(findPerBad.toString());
        Assert.assertNotNull(findPerBad);
    }
    @Test(testName = "Test XX Lista osób dla danej badge_id")
    public void shouldGetPersBadgesListByGroupId() {
        List <PersBadge> findPerBad = persBadgesDAO.findByBadgeId(1);
        System.out.println(findPerBad.toString());
        Assert.assertNotNull(findPerBad);
    }

    @Test(testName = "Test XX join")
    public void shouldGetPersBadgesWithJoinListByGroupId() {
        List <PersBadge> findPerBad = persBadgesDAO.findByPersonIdJoinBadgesJoinPerson(17);
        System.out.println(findPerBad.toString());
        Assert.assertNotNull(findPerBad);
    }

    @Test(testName = "Stwórz powiązanie persons_badges")
    public void shouldCreatePersonBadgesConnection() {
        PersBadge persBadges = new PersBadge();
        int personId = 5;
        int badgeId = 5;
        persBadges.setPersonId(personId);
        persBadges.setBadgeId(badgeId);
        persBadges.setAcquired(LocalDate.parse("2010-11-25"));

        persBadgesDAO.save(persBadges);
        List<PersBadge> findPBbyPerson = persBadgesDAO.findByPersonId(personId);
        List<PersBadge> findPBbyBadge = persBadgesDAO.findByBadgeId(badgeId);

        Assert.assertTrue(findPBbyPerson.contains(persBadges) && findPBbyBadge.contains(persBadges));
    }


    @Test(testName = "Zaktualizuj pole w persons_badges")
    public void shouldUpdatePersonBadgesEntry() {
        int oldPersonId = 3;
        int oldBadgeId = 8;
        LocalDate oldAcquired = LocalDate.parse("2010-11-23");
        int newPersonId = 3;
        int newBadgeId = 9;
        LocalDate newAcquired = LocalDate.parse("2010-11-21");

        PersBadge oldPersBadges = new PersBadge();
        PersBadge newPersBadges = new PersBadge();

        oldPersBadges.setPersonId(oldPersonId);
        oldPersBadges.setBadgeId(oldBadgeId);
        oldPersBadges.setAcquired(oldAcquired);


        newPersBadges.setPersonId(newPersonId);
        newPersBadges.setBadgeId(newBadgeId);
        newPersBadges.setAcquired(newAcquired);

        persBadgesDAO.update(oldPersBadges, newPersBadges);

        List<PersBadge> findGPbyPerson = persBadgesDAO.findByPersonId(newPersonId);
        List<PersBadge> findGPbyGroup = persBadgesDAO.findByBadgeId(newBadgeId);

        Assert.assertFalse(findGPbyPerson.contains(oldPersBadges) && findGPbyGroup.contains(oldPersBadges));
        Assert.assertTrue(findGPbyPerson.contains(newPersBadges) && findGPbyGroup.contains(newPersBadges));
    }

    @Test(testName = "Usuń powiązanie person - badge")
    public void shouldDeletePersonBadgesConnection() {
        PersBadge persBadges = new PersBadge();
        int personId = 5;
        int badgeId = 5;
        persBadges.setPersonId(personId);
        persBadges.setBadgeId(badgeId);

        persBadgesDAO.delete(persBadges);
        List<PersBadge> findGPbyPerson = persBadgesDAO.findByPersonId(personId);
        List<PersBadge> findGPbyGroup = persBadgesDAO.findByBadgeId(badgeId);

        Assert.assertFalse(findGPbyPerson.contains(persBadges) && findGPbyGroup.contains(persBadges));
    }

    //TESTY DO ROLI

    @Test(testName = "Znajdź rolę po ID")
    public void shouldFindRoleId() {
        Role findRole = rolesDAO.findById(1);
        Assert.assertNotNull(findRole);
    }

    @Test(testName = "Stwórz rolę i ją znajdź")
    public void shouldCreateRole() {
        Role role = new Role();
        role.setName("Rola");

        rolesDAO.save(role);
        Role findRole = rolesDAO.findById(rolesDAO.findMaxRoleId());

        Assert.assertNotNull(findRole);
        Assert.assertEquals(role.getName(), findRole.getName());
    }

    @Test(testName = "Zaktualizuj pole w roli")
    public void shouldUpdateRole() {
        Role role = rolesDAO.findById(rolesDAO.findMaxRoleId());
        String nameBeforeUpdate = role.getName();
        role.setName("Super ekstra rola");
        rolesDAO.update(role);
        role = rolesDAO.findById(rolesDAO.findMaxRoleId());

        Assert.assertNotEquals(nameBeforeUpdate, role.getName());
    }

    @Test(testName = "Usuń role")
    public void shouldDeleteRole() {
        int id = rolesDAO.findMaxRoleId();
        rolesDAO.delete(rolesDAO.findMaxRoleId());
        Role deletedRole = rolesDAO.findById(id);

        Assert.assertNull(deletedRole);
    }

}


