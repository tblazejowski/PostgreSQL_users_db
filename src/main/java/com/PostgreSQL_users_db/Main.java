package com.PostgreSQL_users_db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public enum State {
        START,
        LOGGING,
        IN_REGISTRATION,
        LOGGED_IN,
        EXIT
    }

    public static User currentUser;

    public static void main(String[] args) {

        Connection connection = Database.getConnection();
        State state = State.START;

        Scanner input = new Scanner(System.in).useDelimiter("\\n");

        while (state != State.EXIT) {
            switch (state) {
                case START:
                    state = printStartScreen(input);
                    break;
                case LOGGING:
                    state = printLoginScreen(input);
                    break;
                case IN_REGISTRATION:
                    state = printRegistrationScreen(input);
                    break;
                case LOGGED_IN:
                    state = printLoggedInScreen(input);
                    break;
            }
        }
        System.out.println("Bye");
        input.close();
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static State printStartScreen(Scanner input) {
        currentUser = null;
        System.out.println("Welcome!");
        System.out.println("type [1] if you want to log in\n" + "type [2] if you want to create new account\n"
                + "type [0] anytime you want to exit");
        String numberEntered = input.next();
        switch (numberEntered) {
            case "0":
                return State.EXIT;
            case "1":
                return State.LOGGING;
            case "2":
                return State.IN_REGISTRATION;
            default:
                System.out.println("Sorry mate, wrong number");
                return State.START;
        }
    }

    private static State printLoginScreen(Scanner input) {

        System.out.println("enter you login: ");
        String login = input.next();
        System.out.println("enter your password: ");
        String password = input.next();
        currentUser = new User(login, password);
        if (currentUser.checkUserCredentialsInDB()) {
            return State.LOGGED_IN;
        } else {
            return State.START;
        }

    }

    private static State printLoggedInScreen(Scanner input) {
        System.out.println("Welcome " + currentUser.getLogin() + "!");
        System.out.println("[0] Exit ");
        System.out.println("[9] Return to main menu by logging out");
        String numberEntered = input.next();
        switch (numberEntered) {
            case "0":
                return State.EXIT;
            case "9":
                return State.START;
            default:
                return State.LOGGED_IN;
        }
    }

    private static State printRegistrationScreen(Scanner input) {

        System.out.println("create you login: ");
        String login = input.next();
        System.out.println("create your password: ");
        String password = input.next();
        currentUser = new User(login, password);
        System.out.print("User has been created...");
        try {
            currentUser.addUserToDB();
            System.out.println("and added to DB!");
        } catch (SQLException e) {
            String exceptionCode = e.getSQLState();
            if (exceptionCode.equals("23505")) {
                System.out.println("and NOT added to DB!");
                System.out.println("Unfortunately entered login: " + login + " already exists in DB. Try another one!");
                return State.IN_REGISTRATION;
            } else {
                System.out.println("Unexpected error occurred!");
            }
        }
        return State.LOGGED_IN;
    }
}