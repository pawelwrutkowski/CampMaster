package menu;

import dao.PersonsDAO;
import entity.Person;
import entity.User;
import forms.IRegisterForms;
import forms.RegisterChildForm;

import java.util.List;
import java.util.Scanner;

public class ChildMenu {
    private final User user;
    private List<Person> parentsChildren;
    private final Scanner scanner = new Scanner(System.in);

    public ChildMenu(User user) {
        this.user = user;
    }

    private int getChildIdFromInput(int usersChoose) {
        usersChoose -= 1; // Gdyż lista zaczyna się od 0, a użytkownik widzi od 1
        return parentsChildren.get(usersChoose).getPersonId();
    }

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

    public void childMenuScreen() {
        PersonsDAO personsDAO = new PersonsDAO();
        this.parentsChildren = personsDAO.createListOfParentsChildren(user.getPersonId());

        System.out.println("Witaj w CampMaster - Child Menu");
        String menu = "Dokonaj wyboru:\n1. Lista Twoich dzieci.\n2. Dodaj dziecko.\n3. Edytuj/Usuń profil dziecka." +
                "\n4. Wróć.\n5. Wyloguj.";

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
                case 1:     //Pokaż dzieci
                    personsDAO.printChildren(parentsChildren);
                    break;
                case 2:     //Dodaj dziecko
                    IRegisterForms register = new RegisterChildForm(user);
                    register.registerInDatabase();

                    this.parentsChildren = personsDAO.createListOfParentsChildren(user.getPersonId());
                    personsDAO.printChildren(parentsChildren);

                    break;
                case 3:     //Edytuj dziecko
                    personsDAO.printChildren(parentsChildren);
                    if (parentsChildren.isEmpty()) {
                        System.out.println("Przykro nam, żadne dziecko nie jest wpisane w naszym serwisie.");
                    } else {
                        int childIdToEdit = requestChildFromList();
                        Person child = personsDAO.findById(childIdToEdit);

                        PersonEditMenu childEditMenu = new PersonEditMenu(user, child);
                        childEditMenu.personEditMenuScreen();

                        this.parentsChildren = personsDAO.createListOfParentsChildren(user.getPersonId());
                    }
                    break;
                case 4:
                    return;
                case 5:
                    System.exit(-1);
                    break;
                default:
                    System.out.println("Nie dokonano właściwego wyboru. Wybierz wartość z przedziału 1-5");
            }
        } while (true);
    }
}
