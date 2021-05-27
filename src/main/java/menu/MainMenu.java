package menu;

import entity.User;

import java.util.Scanner;

public class MainMenu {
    private User user;

    public MainMenu(User user) {
        this.user = user;
    }

    public void mainMenuScreen() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWitaj w CampMaster - Main Menu");
        String menu = "\nDokonaj wyboru:\n1. Twoje dzieci.\n2. Oferty obozów.\n3. Odznaki.\n4. Twój profil." +
                "\n5. Zarządzaj użytkownikami.\n6. Wyloguj.";

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
                    if (user.getPrivilegeId() == 4) {
                        System.out.println("Nie masz dostępu do Child Menu!");
                    } else {
                        ChildMenu childMenu = new ChildMenu(user);
                        childMenu.childMenuScreen();
                    }
                    break;
                case 2:
                    OfferMenu offerMenu = new OfferMenu(user);
                    offerMenu.offersMenuScreen();
                    break;
                case 3:
                    BadgesMenu badMenu = new BadgesMenu(user);
                    badMenu.badgesMenuScreen();
                    break;
                case 4:
                    UsersProfileMenu usersProfileMenu = new UsersProfileMenu(user);
                    usersProfileMenu.usersProfileMenuScreen();
                    break;
                case 5:
                    if (user.getPrivilegeId() == 1) {
                        AdminMenu adminMenu = new AdminMenu(user);
                        adminMenu.adminMenuScreen();
                    } else {
                        System.out.println("Nie masz dostępu do zarządzania użytkownikami.");
                    }
                    break;
                case 6:
                    System.exit(-1);
                    break;
                default:
                    System.out.println("Nie dokonano właściwego wyboru. Wybierz wartość z przedziału 1-6.");
            }
        } while (true);
    }
}


