package menu;

import entity.Person;
import entity.User;
import forms.EditPersonForm;
import forms.IEditForms;

import java.util.Scanner;

public class PersonEditMenu {
    private final User user;
    private final Person person;

    public PersonEditMenu(User user, Person person) {
        this.user = user;
        this.person = person;
    }

    public void personEditMenuScreen() {
        IEditForms editForm = new EditPersonForm(person);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Dane osoby: " + person.getFirstName() + " " + person.getLastName() + " " + person.getBirthDate() +
                "\nDane kontaktowe do osoby: " + person.getEmail() + " " + person.getPhoneNumber());
        String menu = "Edytuj pole:\n1. Imię.\n2. Nazwisko\n3. Data Urodzenia.\n4. Email.\n5. Nr Telefonu.\n6. Hasło." +
                "\n7. Usuń.\n8. Wróć.\n9. Wyloguj.";

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
                    editForm.editFirstName();
                    break;
                case 2:
                    editForm.editLastName();
                    break;
                case 3:
                    if (person.getPrivilegeId() == 4) {
                        editForm.editChildBirthDate();
                    } else {
                        editForm.editBirthDate();
                    }
                    break;
                case 4:
                    editForm.editEmail();
                    break;
                case 5:
                    editForm.editPhoneNumber();
                    break;
                case 6:
                    editForm.editPassword();
                    break;
                case 7:
                    editForm.deleteProfile();
                    return;
                case 8:
                    return;
                case 9:
                    System.exit(-1);
                    break;
                default:
                    System.out.println("Nie dokonano właściwego wyboru. Wybierz wartość z przedziału 1-7!");
            }
        } while (true);
    }
}
