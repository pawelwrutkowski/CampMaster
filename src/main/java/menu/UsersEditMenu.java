package menu;

import entity.User;
import forms.EditUserForm;
import forms.IEditForms;

import java.util.Scanner;

public class UsersEditMenu {
    private User user;

    public UsersEditMenu(User user) {
        this.user = user;
    }

    public void usersEditMenu() {
        IEditForms editUser = new EditUserForm(user);
        Scanner scanner = new Scanner(System.in);
        String menu = "Edytuj pole:\n1. Imię.\n2. Nazwisko.\n3. Datę urodzenia\n4. Email.\n5. Nr telefonu.\n6. Hasło." +
                "\n7. Wróć.\n8. Wyloguj.";

        do {
            System.out.println(menu);
            int selectedValue;
            try {
                selectedValue = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Podaj właściwą wartość z przedziału 1-8");
                continue;
            }
            switch (selectedValue) {
                case 1:
                    editUser.editFirstName();
                    break;
                case 2:
                    editUser.editLastName();
                    break;
                case 3:
                    editUser.editBirthDate();
                    break;
                case 4:
                    editUser.editEmail();
                    break;
                case 5:
                    editUser.editPhoneNumber();
                    break;
                case 6:
                    editUser.editPassword();
                    break;
                case 7:
                    return;
                case 8:
                    System.exit(-1);
                    break;
                default:
                    System.out.println("Nie dokonano właściwego wyboru. Wybierz wartość z przedziału 1-8");
            }
        } while (true);
    }
}
