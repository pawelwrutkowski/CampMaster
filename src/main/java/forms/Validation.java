package forms;

import dao.PersonsDAO;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Validation {
    private final PersonsDAO personsDAO = new PersonsDAO();

    /**
     * Takes a string from user as a parameter and checks two conditions to return true
     *
     * @param email String value
     * @return boolean, true if both methods are true
     */
    protected boolean isEmailCorrect(String email) {
        return checkEmailsInputFormat(email) && isEmailUnique(email);
    }

    /**
     * Regex checks if there are "@" "." signs and if there are 2 to 4 signs after the dot
     * Examples of good values: "test@gmail.com" "w@w.ww" "1@1.1111"
     *
     * @param email String value
     * @return boolean, true if the given value matches a regular expression
     */
    private boolean checkEmailsInputFormat(String email) {
        final String regex = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches())
            return true;
        else {
            System.out.println("Podano zły email");
            return false;
        }
    }

    /**
     * Checks if the given value is not already used in database in "Persons" table
     *
     * @param email String value
     * @return boolean
     */
    private boolean isEmailUnique(String email) {
        if (personsDAO.findByEmail(email) == null)
            return true;
        else {
            System.out.println("Taki email już istnieje");
            return false;
        }
    }

    /**
     * Regex checks that there are at least 8 characters containing at least one uppercase letter, lowercase letter
     * and a number. Examples of good values: "examPle1" "TeST1234"
     * @param password String value
     * @return boolean, true if value matches regex
     */
    protected boolean isPasswordCorrect(String password) {
        final String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        if (matcher.matches())
            return true;
        else {
            System.out.println("Podano złe hasło");
            return false;
        }
    }

    /**
     * Takes a string from user as a parameter and checks two conditions to return true. Two ints are needed to test
     * the condition
     * @param date String value
     * @param minAge int value
     * @param maxAge int value
     * @return boolean, true if both methods are true
     */
    protected boolean isDateCorrect(String date, int minAge, int maxAge) {
        return checkDateInputFormat(date) && isAgeInLimit(date, minAge, maxAge);
    }

    /**
     * Regex checks whether the parameter is in the YYYY/MM/DD or YYYY-MM-DD format and whether it is a correct date,
     * also whether February 29 is possible. Example of good values: "2000-12-31" "1996/02/29
     * @param date String value
     * @return boolean, true if value matches regex
     */
    private boolean checkDateInputFormat(String date) {
        final String regex = "(?:(?:(?:(?:1[8-9]|20)(?:04|08|[2468][048]|[13579][26]))|2000)([-/])02(?:\\1)29|" +
                "(?:(?:1[8-9]|20)\\d\\d)([-/])(?:(?:0[1-9]|1[0-2])(?:\\2)(?:0[1-9]|1[0-9]|2[0-8])|(?:0(?:1|[3-9])|" +
                "(?:1[0-2]))(?:\\2)(?:29|30)|(?:0[13578]|1[02])(?:\\2)31))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);

        if (matcher.matches())
            return true;
        else {
            System.out.println("Podano nieistniejącą datę, lub błędny format danych.");
            return false;
        }
    }

    /**
     * Checks if the given String is between minAge and maxAge or equals minAge/maxAge
     * @param date String value
     * @param minAge int value
     * @param maxAge int value
     * @return boolean
     */
    private boolean isAgeInLimit(String date, int minAge, int maxAge) {
        LocalDate now = LocalDate.now();
        LocalDate birth = LocalDate.parse(date);
        Period period = Period.between(birth, now);

        if (period.getYears() >= minAge && period.getYears() <= maxAge)
            return true;
        else {
            System.out.println("Twój wiek nie spełnia wymogów.");
            return false;
        }
    }

    /**
     * Takes a string from user as a parameter and checks two conditions to return true.
     * @param date String value
     * @return boolean, true if both methods are true
     */
    protected boolean isGroupDateCorrect(String date) {
        return checkDateInputFormat(date) && isDateFuture(date);
    }

    /**
     * Checks that the given value is at least 7 days from today
     * @param dateString String value
     * @return boolean
     */
    private boolean isDateFuture(String dateString) {
        LocalDate now = LocalDate.now();
        LocalDate date = LocalDate.parse(dateString);
        Period period = Period.between(now, date);

        if (date.isAfter(now) && period.getDays() > 7)
            return true;
        else {
            System.out.println("Data nie jest minimum tydzień od dzisiaj!");
            return false;
        }
    }

    //Group Name Validation
    static boolean isGroupNameCorrect(String name) {
        final String regex = "([A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*[\\s]?)*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        if (matcher.matches())
            return true;
        else {
            System.out.println("Niepoprawne dane. Każde słowo wprowadź z wielkiej litery. Brak cyfr.");
            return false;
        }
    }


    /**
     * Regex checks if the value contains only letters considering Polish characters. The first letter must be uppercase
     * and subsequent letters (must be at least one) lowercase.
     * @param name String value
     * @return boolean, true if value matches regex
     */
    protected boolean isNameCorrect(String name) {
        final String regex = "^[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś].*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        if (matcher.matches())
            return true;
        else {
            System.out.println("Niepoprawne dane. Zacznij od wielkiej litery.");
            return false;
        }
    }

    /**
     * Regex checks that the value contains exactly 9 digits, the first of which is not zero.
     * @param number String value
     * @return boolean, true if value matches regex
     */
    protected boolean isPhoneNumberCorrect(String number) {
        final String regex = "[1-9][0-9]{8}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);

        if (matcher.matches())
            return true;
        else {
            System.out.println("Zły numer.");
            return false;
        }
    }
}
