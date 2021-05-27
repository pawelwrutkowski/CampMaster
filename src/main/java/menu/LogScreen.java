package menu;

import dao.UserDAO;
import entity.User;
import forms.IRegisterForms;
import forms.RegisterUserForm;
import lombok.extern.log4j.Log4j;

import java.util.Scanner;

@Log4j
public class LogScreen {

    Scanner scan = new Scanner(System.in);

    public void chooseOption() {

        System.out.println("\nWitaj w CampMaster - Logowanie");
        String menu = "\nDokonaj wyboru:\n 1. Logowanie\n 2. Rejestracja\n 3. Zamknij";
        do {
            System.out.println(menu);
            int selectedValue;
            try {
                selectedValue = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                log.error(e);
                System.out.println("Podaj właściwą wartość z przedziału 1-3.");
                continue;
            }
            switch (selectedValue) {
                case 1:
                    String email;
                    String password;

                    System.out.println("Logowanie\n Podaj email: ");
                    email = scan.nextLine();
                    scan.skip("((?<!\\R)\\s)*");

                    System.out.println("Podaj hasło: ");
                    password = scan.nextLine();

                    UserDAO userDAO = new UserDAO();
                    User user = userDAO.signIn(email, password);

                    if (user != null) {
                        MainMenu maMenu = new MainMenu(user);
                        maMenu.mainMenuScreen();
                    }
                    break;

                case 2:
                    System.out.println("Rejestracja");
                    IRegisterForms form = new RegisterUserForm();
                    form.registerInDatabase();
                    break;
                case 3:
                    System.exit(-1);
                    break;
                default:
                    System.out.println("Nie dokonano właściwego wyboru. Wybierz wartość z przedziału 1-3.");
            }
        } while (true);
    }
}
