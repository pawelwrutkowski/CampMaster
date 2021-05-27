package menu;

import dao.PersonsDAO;
import entity.User;

import java.util.Scanner;

public class UsersProfileMenu {
    private User user;

    public UsersProfileMenu(User user) {
        this.user = user;
    }

    private void showUsersData() {
        System.out.println("Email: " + user.getEmail() + "\nImię i nazwisko: " + user.getFirstName() + " " +
                user.getLastName() + "\nNumer telefonu: " + user.getPhoneNumber() + "\nData urodzenia: " +
                user.getBirthDate());
    }

    public void usersProfileMenuScreen() {
        Scanner scanner = new Scanner(System.in);
        PersonsDAO personsDAO = new PersonsDAO();
        System.out.println("\nWitaj w Camp Master " + user.getFirstName() + " - Twój profil");
        String menu = "\nDokonaj wyboru:\n1. Pokaż dane.\n2. Edytuj.\n3. Usuń.\n4. Wróć.\n5. Wyloguj.";
        do {
            System.out.println(menu);
            int selectedValue;
            try {
                selectedValue = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Podaj właściwą wartość z przedziału 1-5");
                continue;
            }
            switch (selectedValue) {
                case 1:
                    showUsersData();
                    break;
                case 2:
                    UsersEditMenu userEditMenu = new UsersEditMenu(user);
                    userEditMenu.usersEditMenu();
                    break;
                case 3:
                    personsDAO.askToDelete("T", user.getPersonId());
                    System.exit(-1);
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
