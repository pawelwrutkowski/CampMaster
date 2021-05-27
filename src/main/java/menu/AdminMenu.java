package menu;

import dao.PersonsDAO;
import entity.Person;
import entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdminMenu {
    private final User user;
    private final Scanner scanner = new Scanner(System.in);
    private final PersonsDAO personsDAO = new PersonsDAO();
    private List<Person> allUsers = personsDAO.createListOfAllUsers();

    public AdminMenu(User user) {
        this.user = user;
    }

    private void printUsers(List<Person> listOfUsers) {
        int counter = 1;
        System.out.println("Imię nazwisko i email:");
        for (Person each : listOfUsers) {
            System.out.println(counter + ") " + each.getFirstName() + " " + each.getLastName()
                    + " " + each.getEmail() + " " + privilegeIdToText(each.getPrivilegeId()));
            if (counter % 5 == 0) {
                System.out.println();
            }
            counter++;
        }
    }

    private List<Person> filterByPriviledgeId(int priviledgeId) {
        List<Person> filteredList = new ArrayList<>();
        for (Person each : allUsers) {
            if (each.getPrivilegeId() == priviledgeId)
                filteredList.add(each);
        }
        return filteredList;
    }

    private int askHowToFilter(String text) {
        int chosenValue = 0;
        System.out.println(text);
        try {
            chosenValue = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Podano wartość spoza przedziału.");
        }
        return chosenValue;
    }

    private List<Person> findAllByFirstName() {
        System.out.println("Jakie imię wyszukać ? (Filtracja po zgadzającej się części znaków)");
        String name = scanner.nextLine();
        return allUsers.stream()
                .filter(personsFirstName -> personsFirstName.getFirstName().contains(name))
                .collect(Collectors.toList());
    }

    private List<Person> findAllByLastName() {
        System.out.println("Jakie nazwisko wyszukać ? (Filtracja po zgadzającej się części znaków)");
        String lastName = scanner.nextLine();
        return allUsers.stream()
                .filter(personsLastName -> personsLastName.getLastName().contains(lastName))
                .collect(Collectors.toList());
    }

    private List<Person> findAllByEmail() {
        System.out.println("Jaki email wyszukać ? (Filtracja po zgadzającej się części znaków)");
        String email = scanner.nextLine();
        return allUsers.stream()
                .filter(personsEmail -> personsEmail.getEmail().contains(email))
                .collect(Collectors.toList());
    }

    private List<Person> adminsAndTrainers() {
        return Stream.concat(filterByPriviledgeId(1).stream(),
                filterByPriviledgeId(2).stream())
                .collect(Collectors.toList());
    }

    private List<Person> trainersAndParents() {
        return Stream.concat(filterByPriviledgeId(2).stream(),
                filterByPriviledgeId(3).stream())
                .collect(Collectors.toList());
    }

    private int askWhatFieldInClass() {
        int chosenValue = 0;
        System.out.println("Filtruj po:\n1. Imieniu\n2. Nazwisku\n3. Emailu");
        try {
            chosenValue = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Podano wartość spoza przedziału.");
        }
        return chosenValue;
    }

    private List<Person> showFilters(int number) {
        List<Person> filteredList = new ArrayList<>();
        if (number == 1) {
            filteredList = findAllByFirstName();
        } else if (number == 2) {
            filteredList = findAllByLastName();
        } else if (number == 3) {
            filteredList = findAllByEmail();
        } else {
            System.out.println("Wybrano złą opcję.");
        }
        return filteredList;
    }

    private int getUserFromInput(List<Person> listOfUsers, int usersChoose) {
        usersChoose -= 1; // Gdyż lista zaczyna się od 0, a użytkownik widzi od 1
        return listOfUsers.get(usersChoose).getPersonId();
    }

    private int requestUserFromList(List<Person> listOfUsers) {
        int userFromlist = 0;
        do {
            System.out.println("Wybierz użytkownika do operacji(wprowadź 1-" + (listOfUsers.size()) + "): ");
            try {
                userFromlist = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Proszę podać numer.");
            }
        } while (userFromlist < 1 || userFromlist > listOfUsers.size());

        return getUserFromInput(listOfUsers, userFromlist);
    }

    private String privilegeIdToText(int privilegeId) {
        if (privilegeId == 1)
            return "Admin";
        else if (privilegeId == 2) {
            return "Trener";
        } else if (privilegeId == 3)
            return "Rodzic";
        else
            return "Dziecko";
    }

    public void adminMenuScreen() {
        String textForFilterOption = "Pokaż:\n1. Administratorów.\n2. Trenerów.\n3. Rodziców.\n4. Dzieci.\n5. Wyszukaj" +
                " po danej.\n6. Wróc.\n 7. Wyloguj.";

        String textBeforeOperation = "Aby rozpocząć operację, musisz pofiltrowac listę. Pokaż:\n1. Administratorów." +
                "\n2. Trenerów.\n3. Rodziców.\n4. Dzieci.\n5. Wyszukaj po polu.\n6. Wróć.\n7. Wyloguj.";

        System.out.println("\nWitaj w CampMaster - Admin Panel");
        String menu = "\nDokonaj wyboru:\n1. Lista użytkowników.\n2. Filtruj użytkowników.\n3. Edytuj/Usuń użytkownika."
                + "\n4. Awansuj/Degraduj.\n5. Wróć.\n6. Wyloguj.";

        do {
            System.out.println(menu);
            int selectedValue;
            try {
                selectedValue = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Podaj właściwą wartość z przedziału 1-6.");
                continue;
            }
            switch (selectedValue) {
                case 1:
                    printUsers(allUsers);
                    break;
                case 2:
                    int givenValue = askHowToFilter(textForFilterOption);
                    if (givenValue >= 1 && givenValue <= 4) {
                        printUsers(filterByPriviledgeId(givenValue));
                    } else if (givenValue == 5) {
                        printUsers(showFilters(askWhatFieldInClass()));
                    }
                    break;
                case 3:
                    List<Person> filteredList = new ArrayList<>();
                    givenValue = askHowToFilter(textBeforeOperation);
                    if (givenValue >= 1 && givenValue <= 4) {
                        filteredList = filterByPriviledgeId(givenValue);
                    } else if (givenValue == 5) {
                        filteredList = showFilters(askWhatFieldInClass());
                    }
                    if (filteredList.isEmpty()) {
                        System.out.println("Przykro nam, coś poszło nie tak.");
                    } else {
                        System.out.println("Wybierz użytkownika do edycji: ");
                        printUsers(filteredList);
                        int idToEdit = requestUserFromList(filteredList);
                        Person personToEdit = personsDAO.findById(idToEdit);
                        PersonEditMenu personEdit = new PersonEditMenu(user, personToEdit);
                        personEdit.personEditMenuScreen();
                        //Update list with new data.
                        allUsers = personsDAO.createListOfAllUsers();
                    }
                    break;
                case 4:
                    System.out.println("Dokonaj wyboru:\n1. Awans.\n2. Degradacja.");
                    int chosenValue = 0;
                    do {
                        try {
                            chosenValue = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Podaj właściwą wartość z przedziału 1-2.");
                        }
                    } while (chosenValue != 1 && chosenValue != 2);

                    System.out.println(chosenValue);
                    if (chosenValue == 1) {
                        System.out.println("Wybierz osobę której dać awans o 1 pozycję:");
                        List<Person> listOfTrainersAndParents = trainersAndParents();
                        printUsers(listOfTrainersAndParents);
                        int personIdToPromote = requestUserFromList(listOfTrainersAndParents);
                        Person personToUpdate = personsDAO.findById(personIdToPromote);
                        int personsPriviledegeId = personToUpdate.getPrivilegeId();
                        personToUpdate.setPrivilegeId(--personsPriviledegeId);
                        personsDAO.update(personToUpdate);

                    } else {
                        System.out.println("Wybierz osobę ktrórą degradować o 1 pozycję:");
                        List<Person> listOfAdminsAndTrainers = adminsAndTrainers();
                        printUsers(listOfAdminsAndTrainers);

                        int personIdToPromote = requestUserFromList(listOfAdminsAndTrainers);
                        Person personToUpdate = personsDAO.findById(personIdToPromote);
                        int personsPriviledegeId = personToUpdate.getPrivilegeId();
                        personToUpdate.setPrivilegeId(++personsPriviledegeId);
                        personsDAO.update(personToUpdate);
                    }
                    //Update list with new data.
                    allUsers = personsDAO.createListOfAllUsers();
                    break;
                case 5:
                    return;
                case 6:
                    System.exit(-1);
                    break;
                default:
                    System.out.println("Nie dokonano właściwego wyboru. Wybierz wartość z przedziału 1-6.");
            }
        } while (true);
    }
}
