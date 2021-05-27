package menu;

import dao.*;
import entity.*;
import forms.IRegisterForms;
import forms.RegisterOfferForm;
import lombok.extern.log4j.Log4j;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Scanner;

@Log4j
public class OfferMenu {
    private final User user;
    List<Person> parentsChildren;
    private final Scanner scanner = new Scanner(System.in);

    public OfferMenu(User user) {
        this.user = user;
    }

    /**
     * Views possible choices of action with camp offers, then reads from console (scanner) your choice
     * and then runs proper methods or functions
     */
    public void offersMenuScreen() {
        OffersDAO offersDAO = new OffersDAO();
        offersDAO.deactivateObsolete(); // deaktywacja wszystkich ofert, które teraz są mają status "aktywny",
        // a które dotyczą grup, których data początku jest w przeszłości (przed dzisiaj)
        System.out.println("\nWitaj w CampMaster - Oferty");
        String menu = "\nDokonaj wyboru: \n1. Kupione obozy \n2. Zapisz na obóz \n3. Historia obozów" +
                "\n4. Dodaj ofertę nowego obozu \n5. Zmień ofertę istniejącego obozu" +
                "\n6. Wypisz z obozu\n7. Wróć \n8. Wyloguj  ";
        do {
            System.out.println(menu);
            int selectedValue;
            try {
                selectedValue = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Podaj właściwą wartość z przedziału 1-7.");
                continue;
            }
            switch (selectedValue) {
                case 1:
                    checkUserThenShowCamps(true);

                    break;
                case 2:
                    // pobiera listę aktualnych ofert
                    List<Offer> activeOffersList = offersDAO.findActiveOffersJoinGroups();
                    // jeśli użytkownik jest rodzicem
                    if (user.getPrivilegeId() == 3) {
                        System.out.println("\nLista dostępnych wyjazdów:");
                        offersDAO.printOffersList(activeOffersList); //wypisuje na konsolę aktualne oferty
                        buyOffer(activeOffersList); //rozpoczyna proces kupna przekazuje listę ofert

                    } else if (user.getPrivilegeId() == 4) {
                        System.out.println("\nNie masz uprawnień do kupowania obozów.\nLista dostępnych wyjazdów:");
                        offersDAO.printOffersList(activeOffersList);        // dziecku tylko wypisuje oferty, które można kupić

                    } else if (user.getPrivilegeId() == 2) {
                        System.out.println("\nLista dostępnych wyjazdów:");
                        offersDAO.printOffersList(activeOffersList);
                        buyOffer(activeOffersList); //rozpoczyna proces zapisu przekazuje listę ofert
                    } else {
                        System.out.println("Tylko rodzice i opiekunowie grup mogą korzystać z tej funkcji!");
                    }
                    break;

                case 3:
                    checkUserThenShowCamps(false);
                    break;

                case 4: // stwórz ofertę i obóz

                    if (user.getPrivilegeId() == 2 || user.getPrivilegeId() == 1) {
                        IRegisterForms registerOfferForm = new RegisterOfferForm();
                        registerOfferForm.registerInDatabaseWithNewGroup();
                    } else {
                        System.out.println("Nie masz odpowiednich uprawnień!");
                    }
                    break;

                case 5: // edytuj ofertę
                    if (user.getPrivilegeId() == 2 || user.getPrivilegeId() == 1) {
                        IRegisterForms registerOfferForm = new RegisterOfferForm();
                        registerOfferForm.registerInDatabase();
                    } else {
                        System.out.println("Nie masz odpowiednich uprawnień!");
                    }
                    break;

                case 6: //wycofaj z obozu
                    checkUserThenShowCamps(true);
                    unsubscribeFromCamp();
                    break;
                case 7:
                    return;
                case 8:
                    System.exit(1);
                default:
                    System.out.println("Nie dokonano właściwego wyboru. Wybierz wartość z przedziału 1-7.");
            }
        } while (true);

    }

    /**
     * Method for unsubscribing from camp.
     * First reads group ID from scanner, checks if this is an future offer
     * If is future and you have parent privilege then reads kid from console and deletes kid connection to chosen group
     * If is future and you have couch privilege then binds user.personId to group and but offer will be null! (couch dont buy, but hires himself)
     * Admin gets information he can't do anything
     * <p>
     * void - deletes chosen group-person connection from database
     */
    private void unsubscribeFromCamp() {
        GrPersonsDAO grPersonsDAO = new GrPersonsDAO();
        GrPersons grPersons = new GrPersons();
        GroupsDAO groupsDAO = new GroupsDAO();

        try { // wartości startowe, bo trzeba je zainicjalizować
            int groupId = 0;
            int childId;

            // Wczytujemy z konsoli (skaner) ID grupy
            System.out.println("Podaj ID obozu: \n(Jeśli chcesz zrezygnować wybierz 0 lub liczbę spoza listy)");
            groupId = Integer.parseInt(scanner.nextLine());

            // Jeśli wybrano przeszłą grupę przerywamy operację
            if (!groupsDAO.checkIfFuture(groupId)) {
                System.out.println("Przerwanie operacji. Nieprawidłowe dane");
                return;
            }

            // Jeśli jest rodzicem to zaczynamy proces wycofania
            if (user.getPrivilegeId() == 3) {

                PersonsDAO personsDAO = new PersonsDAO();
                this.parentsChildren = personsDAO.createListOfParentsChildren(user.getPersonId());
                personsDAO.printChildren(parentsChildren);

                // wprowadzenie id dziecka, dla którego wycofujemy ofertę
                childId = requestChildFromList();

                grPersons.setPersonId(childId);
                grPersons.setGroupId(groupId);

                grPersonsDAO.delete(grPersons);

                // jeśli jesteś opiekunem
            } else if (user.getPrivilegeId() == 2) {

                grPersons.setPersonId(user.getPersonId());
                grPersons.setGroupId(groupId);

                grPersonsDAO.delete(grPersons);

                System.out.println("Wypisałeś się z wybranego obozu.");

            } else {
                System.out.println("Niewłaściwy ID grupy. Wybierz inną.");
            }

        } catch (IndexOutOfBoundsException | NullPointerException | NumberFormatException e) {
            System.out.println("Podano niewłaściwe dane. Wyjście.");
        }
    }

    /**
     * Method for offer buying process.
     * First reads offer ID from scanner, checks if this is an active offer
     * If is active and you have parent privilege then reads person_id of kid from console and binds kid to group and offer
     * If is active and you have couch privilege then binds user.personId to group and but offer will be null! (couch dont buy, but hires himself)
     * Admin gets information he can't do anything
     *
     * @param offersList List<Offer>
     *                   void - saves chosen offer to database (Groups-Persons table with connection to offerId and date of transaction)
     */
    private void buyOffer(List<Offer> offersList) {
        GrPersonsDAO grPersonsDAO = new GrPersonsDAO();
        GrPersons grPersons = new GrPersons();

        try { // wartości startowe, bo trzeba je zainicjalizować
            int active = 0;
            int groupId = 0;
            int childId;

            // Wczytujemy z konsoli (skaner) ID oferty
            System.out.println("Podaj ID oferty: \n(Jeśli chcesz zrezygnować wybierz 0 lub liczbę spoza listy)");
            int offerId = Integer.parseInt(scanner.nextLine());

            // Sprawdzamy w List<Offer> przekazanej w parametrach metody, czy oferta wpisana przez użytkownika w konsoli jest aktywna i do jakiego obozu się odnosi
            for (Offer each : offersList) {
                if (each.getOfferId() == offerId) {
                    active = each.getActive();
                    groupId = each.getGroupId();
                }
            }

            // Jeśli oferta jest aktywna (active = 1) czyli aktualna, a użytkownik jest rodzicem to zaczynamy proces kupna
            if (active == 1 && user.getPrivilegeId() == 3) {
                // zanim cokolwiek uruchomimy sprawdzamy czy jest miejsce w grupie
                if (!grPersonsDAO.isPlaceInGroup(groupId)) {
                    System.out.println("Brak miejsca w grupie");
                    return;
                }

                PersonsDAO personsDAO = new PersonsDAO();
                this.parentsChildren = personsDAO.createListOfParentsChildren(user.getPersonId());
                personsDAO.printChildren(parentsChildren);

                // wprowadzenie id dziecka, dla którego kupujemy ofertę
                childId = requestChildFromList();

                if (grPersonsDAO.checkIfInGroup(childId, groupId)) {
                    System.out.println("Osoba już jest zapisana do tej grupy");
                    return;
                }

                if (!isAgeInLimit(childId, groupId)) {
                    return;
                }

                grPersons.setOfferId(offerId);
                grPersons.setPersonId(childId);
                grPersons.setGroupId(groupId);

                grPersonsDAO.save(grPersons);

                // ten komunikat wyświetli się nawet gdy nie kupimy oferty przez błędne id dziecka
                // ponadto nie sprawdzamy czy kupujemy obóz dla swojego dziecka
                System.out.println("Kupiono wybraną ofertę.");

                // jeśli jesteś opiekunem/trenerem to zapiszesz się na obóz jako trener (bez parametru OfferID w Groups-Persons
            } else if (active == 1 && user.getPrivilegeId() == 2) {

                if (grPersonsDAO.checkIfInGroup(user.getPersonId(), groupId)) {
                    System.out.println("Jesteś już opiekunem tej grupy");
                    return;
                }

                grPersons.setPersonId(user.getPersonId());
                grPersons.setGroupId(groupId);

                grPersonsDAO.save(grPersons);

                System.out.println("Zostałeś opiekunem wybranego obozu.");

            } else {
                System.out.println("Niewłaściwy ID oferty. Wybierz inną.");
            }

        } catch (IndexOutOfBoundsException | NullPointerException | NumberFormatException e) {
            System.out.println("Podano niewłaściwe dane. Wyjście.");
        }
    }

    /**
     * Method for printing camp list (with prices) on console for regular users
     * It has boolean switch, witch allows to choose only future offers or every offer ever bought
     *
     * @param personId   - unique int person_id value
     * @param onlyFuture boolean - allows to choose only future offers or every offer ever bought
     *                   void
     */
    private void showCampsByPersonId(int personId, boolean onlyFuture) {
        GrPersonsDAO grPersonsDAO = new GrPersonsDAO();
        List<GrPersons> grPersonsList = grPersonsDAO.findJoinedGroupsPersonsByPersonId(personId, onlyFuture);     //lista powiązań osoba - grupa dla danego id osoby (join Groups, join Persons, join Offers)

        if (grPersonsList.size() == 0) {
            System.out.println("Brak wyników dla ID: " + personId);
        } else {
            //To, żeby wypisał personalia zanim wypisze przypisane obozy
            String personalia = grPersonsList.get(0).getPerson().getFirstName() + " " + grPersonsList.get(0).getPerson().getLastName();
            System.out.println("\n" + personalia + " - Obozy:");

            //Tu wypisanie przypisanych przyszłych obozów
            for (GrPersons each : grPersonsList) {
                System.out.println("ID obozu: " + each.getGroupId() + " \tNazwa obozu: " + each.getGroup().getName() + "\t\tData Obozu od: " + each.getGroup().getStartDate() + " do: " + each.getGroup().getEndDate() + "\t Cena: " + each.getOffer().getPrice());
            }
        }
    }

    /**
     * Method for printing camp list (without prices) on console for couch users
     * It has boolean switch, witch allows to choose only future offers or every offer ever bought
     *
     * @param personId   - unique int person_id value
     * @param onlyFuture boolean - allows to choose only future offers or every offer ever connected to
     *                   void
     */
    private void showCouchCampsByPersonId(int personId, boolean onlyFuture) {
        GrPersonsDAO grPersonsDAO = new GrPersonsDAO();
        List<GrPersons> grPersonsList = grPersonsDAO.findJoinedGroupsPersonsByPersonIdCouch(personId, onlyFuture);     //lista powiązań osoba - grupa dla danego id osoby (join Groups i join Persons)
        if (grPersonsList.size() == 0) {
            System.out.println("Brak wyników dla ID: " + personId);
        } else {
            //To, żeby wypisał personalia zanim wypisze przypisane obozy
            String personalia = grPersonsList.get(0).getPerson().getFirstName() + " " + grPersonsList.get(0).getPerson().getLastName();
            System.out.println("\n" + personalia + " - Obozy:");

            //Tu wypisanie przypisanych przyszłych obozów
            for (GrPersons each : grPersonsList) {
                System.out.println("ID obozu: " + each.getGroupId() + " \tNazwa obozu: " + each.getGroup().getName() + "\t\tData Obozu od: " + each.getGroup().getStartDate() + " do: " + each.getGroup().getEndDate());
            }
        }
    }

    /**
     * Checks user privilege_id and user person_id and then run proper
     * showCampsByPersonId or showCouchCampsByPersonId methods with proper parameters
     * It has boolean switch, witch allows to choose only future offers or every offer ever bought
     *
     * @param onlyFuture boolean - allows to choose only future offers or every offer ever connected to
     */
    private void checkUserThenShowCamps(boolean onlyFuture) {

        //Jeśli jesteś rodzicem wypisze obozy wszystkich Twoich dzieci
        if (user.getPrivilegeId() == 3) {
            PersonsDAO personsDAO = new PersonsDAO();
            List<Person> childrenList = personsDAO.createListOfParentsChildren(user.getPersonId());

            for (Person each : childrenList) {
                showCampsByPersonId(each.getPersonId(), onlyFuture);
            }

            //Jeśli jesteś trenerem / opiekunem
        } else if (user.getPrivilegeId() == 2) {
            showCouchCampsByPersonId(user.getPersonId(), onlyFuture);

            //Jeśli jesteś administratorem wypisze odmowę operacji
        } else if (user.getPrivilegeId() == 1) {
            System.out.println("Jako administrator nie masz przypisanych obozów!");

            //Jeśli jesteś dzieckiem wypisze Twoje przyszłe obozy albo zrobić opcję wczytania z konsoli personId
        } else {
            showCampsByPersonId(user.getPersonId(), onlyFuture);
        }
    }

    /**
     * Changes chosen number of child on the printed list to child person_id
     *
     * @param usersChoose int - number of child on the list
     * @return child's int  person_id number
     */
    private int getChildIdFromInput(int usersChoose) {
        usersChoose -= 1; // Gdyż lista zaczyna się od 0, a użytkownik widzi od 1
        return parentsChildren.get(usersChoose).getPersonId();
    }

    /**
     * Asks for child number taken from numbered list printed on console.
     * Skans number from console.
     * number of child on the list
     *
     * @return child's int person_id number using getChildIdFromInput(int usersChoose) method
     */
    private int requestChildFromList() {
        int childFromList = 0;
        do {
            System.out.println("Wybierz dziecko do operacji(wprowadź 1-" + (parentsChildren.size()) + "): ");
            try {
                childFromList = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Proszę podać numer.");
            }
        } while (childFromList < 1 || childFromList > parentsChildren.size());

        return getChildIdFromInput(childFromList);
    }

    /**
     * Checks if child's age matches group's age range
     *
     * @param childId int person ID of child
     * @param groupId int group ID of group
     * @return boolean true for a match
     */
    private boolean isAgeInLimit(int childId, int groupId) {
        GroupsDAO groupsDAO = new GroupsDAO();
        int groupAgeRange = groupsDAO.getGroupAgeRange(groupId);
        LocalDate birthdate = null;

        for (Person child : parentsChildren) {
            if (child.getPersonId() == childId) {
                birthdate = child.getBirthDate();
            }
        }

        LocalDate now = LocalDate.now();
        Period period = Period.between(birthdate, now);
        int minAge = 11;
        int divisionAge = 15;
        int maxAge = 18;
        if (groupAgeRange == 1 && period.getYears() >= divisionAge && period.getYears() <= maxAge) {
            return true;
        } else if (groupAgeRange == 0 && period.getYears() >= minAge && period.getYears() < divisionAge) {
            return true;
        } else if (groupAgeRange == -1) {
            System.out.println("Błąd danych.");
            return false;
        } else {
            System.out.println("Wiek nie dopasowany do grupy.");
            return false;
        }
    }
}

