package menu;

import dao.PersBadgesDAO;
import dao.PersonsDAO;
import entity.PersBadge;
import entity.Person;
import entity.User;
import lombok.extern.log4j.Log4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Log4j
public class BadgesMenu {
    private final User user;
    private final Scanner scanner = new Scanner(System.in);

    public BadgesMenu(User user) {
        this.user = user;
    }

    private void showBadges(int showBadgesByPersonID) {
        PersBadgesDAO persBadgesDAO = new PersBadgesDAO();
        List<PersBadge> badgesList = persBadgesDAO.findByPersonIdJoinBadgesJoinPerson(showBadgesByPersonID);     //lista powiązań osoba - odznaka dla danego id osoby
        if (badgesList.isEmpty()) {
            PersonsDAO personsDAO = new PersonsDAO();
            Person person = personsDAO.findById(showBadgesByPersonID);
            System.out.println("Przykro nam, dla: " + person.getFirstName() + " " + person.getLastName() +
                    " nie ma odznak wprowadzonych do bazy danych.");
        } else {
            //To, żeby wypisał personalia zanim wypisze odznaki
            String personalia = badgesList.get(0).getPerson().getFirstName() + " " + badgesList.get(0).getPerson().getLastName();
            System.out.println(personalia + " - Odznaki:");

            //Tu wypisanie odznak
            for (PersBadge each : badgesList) {
                System.out.println("Nazwa odznaki: " + each.getBadge().getName() + "\t\tOpis odznaki: " + each.getBadge().getDescription());
            }
        }
    }

    private LocalDate getDateFromScanner() {
        final String FORMAT = "dd.MM.yyyy";
        System.out.println("Podaj datę w formacie " + FORMAT + ": ");

        String stringDatyScan = scanner.nextLine();

        DateTimeFormatter format = DateTimeFormatter.ofPattern(FORMAT);
        LocalDate wczytanaData = LocalDate.parse(stringDatyScan, format);

        System.out.println(wczytanaData);

        return wczytanaData;
    }

    public void badgesMenuScreen() {
        System.out.println("\nWitaj w CampMaster - Odznaki");
        String menu = "\nDokonaj wyboru:\n1. Podejrzyj odznaki.\n2. Nadaj odznakę.\n3. Usuń odznakę.\n4. Wróć.\n5. Wyloguj.";

        do {
            System.out.println(menu);
            int selectedValue;
            try {
                selectedValue = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Podaj właściwą wartość z przedziału 1-5.");
                continue;
            }
            switch (selectedValue) {
                case 1:
                    if (user.getPrivilegeId() == 3) {
                        PersonsDAO personsDAO = new PersonsDAO();
                        List<Person> parentsChildren = personsDAO.createListOfParentsChildren(user.getPersonId());

                        for (Person each : parentsChildren) {
                            showBadges(each.getPersonId());
                        }
                    } else if (user.getPrivilegeId() == 4) {
                        showBadges(user.getPersonId());
                    } else if (user.getPrivilegeId() == 2 || user.getPrivilegeId() == 1){
                        try {
                            System.out.println("Podaj person_ID dziecka:");
                            int personId = Integer.parseInt(scanner.nextLine());
                            showBadges(personId);
                        } catch (NullPointerException | NumberFormatException e) {
                            System.out.println("Podano niewłaściwe dane dziecka, odznaki lub format daty. Podaj prawidłowe.");
                            log.error(e);
                            continue;
                        }
                    }
                    break;

                case 2:
                    if (user.getPrivilegeId() == 2 || user.getPrivilegeId() == 1) {
                        try {
                            System.out.println("Podaj person ID dziecka:");
                            int personId = Integer.parseInt(scanner.nextLine());

                            System.out.println("Podaj badge ID odznaki:");
                            int badgeId = Integer.parseInt(scanner.nextLine());

                            System.out.println("Data zdobycia odznaki.");
                            LocalDate acquired = getDateFromScanner();

                            PersBadge persBadges = new PersBadge();
                            persBadges.setBadgeId(badgeId);
                            persBadges.setPersonId(personId);
                            persBadges.setAcquired(acquired);

                            PersBadgesDAO persBadgesDAO = new PersBadgesDAO();
                            persBadgesDAO.save(persBadges);

                            showBadges(personId);

                        } catch (NullPointerException | DateTimeParseException | NumberFormatException e) {
                            System.out.println("Podano niewłaściwe dane dziecka, odznaki lub format daty. Podaj prawidłowe dane.");
                            log.error(e);
                            continue;
                        }
                    } else {
                        System.out.println("Nie masz odpowiednich uprawnień!");
                    }
                    break;

                case 3:
                    if (user.getPrivilegeId() == 2 || user.getPrivilegeId() == 1) {
                        try {
                            System.out.println("Podaj person ID dziecka:");
                            int personId = Integer.parseInt(scanner.nextLine());

                            System.out.println("Podaj badge ID odznaki:");
                            int badgeId = Integer.parseInt(scanner.nextLine());

                            PersBadge persBadges = new PersBadge();
                            persBadges.setBadgeId(badgeId);
                            persBadges.setPersonId(personId);

                            PersBadgesDAO persBadgesDAO = new PersBadgesDAO();
                            persBadgesDAO.delete(persBadges);

                            showBadges(personId);

                        } catch (NullPointerException | NumberFormatException e) {
                            System.out.println("Podano niewłaściwe dane dziecka lub odznaki. Podaj prawidłowe.");
                            log.error(e);
                            continue;
                            //Wypisuje błąd przy złym person_id (tak chcę) ale nie wypisuje błędu przy złym badge_id
                        }
                    } else {
                        System.out.println("Nie masz odpowiednich uprawnień!");
                    }
                    break;
                case 4:
                    return;
                case 5:
                    System.exit(-1);
                    break;
                default:
                    System.out.println("Nie dokonano właściwego wyboru. Wybierz wartość z przedziału 1-5.");
            }
        } while (true);
    }
}
