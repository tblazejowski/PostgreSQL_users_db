package com.PostgreSQL_users_db;

import java.sql.*;

public class User {

    private String login;
    private String password;
    Connection connection = Database.getConnection();

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User() {
    }

    public void addUserToDB() throws SQLException {

        PreparedStatement statement = null;
        String sql = "INSERT INTO users (login, password) VALUES(?, crypt(?, gen_salt('bf')));";
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            statement.setString(2, password);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException exception) {
            throw exception;
        }

    }

    public boolean checkUserCredentialsInDB() {

        PreparedStatement statement;
        String sql = "SELECT FROM users WHERE (login = ? AND password = crypt(?, password));";
        Connection connection = Database.getConnection();
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet result;
            result = statement.executeQuery();
            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("You are NOT yet in DB my dear ;)");
        return false;
    }

    public String getLogin() {
        return login;
    }
}