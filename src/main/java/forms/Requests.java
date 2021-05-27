package forms;

import java.time.LocalDate;
import java.util.Scanner;

import dao.GroupsDAO;
import dao.GrCategoriesDAO;
import dao.LocationsDAO;

public abstract class Requests extends Validation {
    private final Scanner scan = new Scanner(System.in);

    protected final int minAdultAge = 18;
    protected final int maxAdultAge = 100;
    protected final int minChildAge = 11;
    protected final int maxChildAge = 18;

    /**
     * Asks to enter a value until the given value matches the validation.
     *
     * @return String
     */
    protected String requestEmail() {
        String email;
        do {
            System.out.println("Wprowadź email: ");
            email = scan.nextLine().trim().toLowerCase();
            scan.skip("((?<!\\R)\\s)*"); // refresh scan
        } while (!isEmailCorrect(email));
        return email;
    }

    /**
     * Asks to enter a value until the given value matches the validation.
     *
     * @return String
     */
    protected String requestPassword() {
        String password;
        do {
            System.out.println("Wprowadź hasło(minimum 8 znaków w tym co najmniej 1 wielka litera oraz 1 cyfra): ");
            password = scan.nextLine().trim();
            scan.skip("((?<!\\R)\\s)*");
        } while (!isPasswordCorrect(password));
        return password;
    }

    /**
     * Asks to enter a value until the given value matches the validation.
     *
     * @return String
     */
    protected String requestName(String text) {
        String name;
        do {
            System.out.println("Wprowadź " + text + ": ");
            name = scan.nextLine().trim();
            scan.skip("((?<!\\R)\\s)*");
        } while (!isNameCorrect(name));
        return name;
    }

    /**
     * Asks to enter a value until the given value matches the validation.
     *
     * @return LocalDate
     */
    protected LocalDate requestBirthDate(int minAge, int maxAge) {
        String birthDateString;
        do {
            System.out.println("Wprowadź datę urodzenia(" + minAge + "-" + maxAge + "lat) RRRR-MM-DD: ");
            // replaceAll(), gdyż regex pozwala przyjąć datę w formacie YYYY/MM/DD
            birthDateString = scan.nextLine().replaceAll("/", "-").trim();
            scan.skip("((?<!\\R)\\s)*");
        } while (!isDateCorrect(birthDateString, minAge, maxAge));
        return LocalDate.parse(birthDateString);
    }

    /**
     * Asks to enter a value until the given value matches the validation.
     *
     * @return String
     */
    protected String requestPhoneNumber() {
        String phoneNumber;
        do {
            System.out.print("Wprowadź numer telefonu:\n+48 ");
            phoneNumber = scan.nextLine().replaceAll("-", "").trim();
            phoneNumber = phoneNumber.replaceAll(" ", "");
            scan.skip("((?<!\\R)\\s)*");
        } while (!isPhoneNumberCorrect(phoneNumber));
        String phone = "+48";
        return phone.concat(phoneNumber);
    }

    /**
     * Asks to enter a value until the given value matches the validation.
     *
     * @return String
     */
    protected String requestGroupName() {
        String groupName;
        do {
            System.out.println("Wprowadź nazwę grupy (maks 45 znaków): ");
            groupName = scan.nextLine().trim();
            scan.skip("((?<!\\R)\\s)*");
        } while (!isGroupNameCorrect(groupName) || groupName.length() > 45);
        return groupName;
    }

    protected LocalDate requestStartDate() {
        String startDateString;
        do {
            System.out.println("Wprowadź datę początku obozu w formacie RRRR-MM-DD: ");
            // replaceAll(), gdyż regex pozwala przyjąć datę w formacie YYYY/MM/DD
            startDateString = scan.nextLine().replaceAll("/", "-").trim();
            scan.skip("((?<!\\R)\\s)*");
        } while (!isGroupDateCorrect(startDateString));
        return LocalDate.parse(startDateString);
    }

    protected LocalDate requestEndDate() {
        String endDateString;
        do {
            System.out.println("Wprowadź datę końca obozu w formacie RRRR-MM-DD: ");
            // replaceAll(), gdyż regex pozwala przyjąć datę w formacie YYYY/MM/DD
            endDateString = scan.nextLine().replaceAll("/", "-").trim();
            scan.skip("((?<!\\R)\\s)*");
        } while (!isGroupDateCorrect(endDateString));
        return LocalDate.parse(endDateString);
    }

    protected int requestGroupId() {
        GroupsDAO groupsDAO = new GroupsDAO();
        int groupId = 0;
        int maxGroupId = groupsDAO.findMaxGroupId();
        do {
            try {
                System.out.print("Wprowadź ID Obozu: ");
                groupId = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Podaj właściwą wartość.");
            }

        } while (groupId < 1 || groupId > maxGroupId);
        return groupId;
    }

    protected int showAndRequestGroupCategory() {
        GrCategoriesDAO grCategoriesDAO = new GrCategoriesDAO();
        int groupCatId = 0;
        int maxGrCatId = grCategoriesDAO.findMaxGrCatId();
        grCategoriesDAO.printGrCategoriesList(grCategoriesDAO.findAll());
        do {
            try {
                System.out.print("Wprowadź ID kategorii grupy: ");
                groupCatId = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Podaj właściwą wartość.");
            }

        } while (groupCatId < 1 || groupCatId > maxGrCatId);
        return groupCatId;
    }

    protected int showAndRequestLocation() {
        LocationsDAO locationsDAO = new LocationsDAO();
        int locationId = 0;
        int maxLocationId = locationsDAO.findMaxLocationId();
        locationsDAO.printLocationsList(locationsDAO.findAll());
        do {
            try {
                System.out.print("Wprowadź ID lokalizacji: ");
                locationId = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Podaj właściwą wartość.");
            }

        } while (locationId < 1 || locationId > maxLocationId);
        return locationId;
    }

    protected int requestAgeRange() {
        int ageRange = -1;
        do {
            try {
                System.out.print("Wprowadź grupę wiekową (1 - Starszaki, 0 - Młodziaki): ");
                ageRange = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Podaj właściwą wartość.");
            }

        } while (ageRange != 0 && ageRange != 1);
        return ageRange;
    }

    protected int requestPrice() {
        int price = 0;
        do {
            try {
                System.out.print("Wprowadź cenę w pełnych złotych: ");
                price = Integer.parseInt(scan.nextLine());
                scan.skip("((?<!\\R)\\s)*");
            } catch (NumberFormatException e) {
                System.out.println("Podaj właściwą wartość.");
            }

        } while (price == 0);
        return price;
    }

    protected int requestSize() {
        int size = 0;
        do {
            try {
                System.out.print("Wprowadź maksymalną liczbę uczestników obozu (max 60): ");
                size = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Podaj właściwą wartość.");
            }

        } while (size == 0 || size > 60);
        return size;
    }
}
