package database;

import models.User;

import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static database.DatabaseConnection.getConnection;
import static utils.Hash.hash;

/******************************************************************************

 File        : database.UserDAO.java

 Date        : Tuesday 1st September 2026

 Author      : Tom Melton

 Description : Data Access Object class which contains methods which insert or
               select from the User table

 History     : 01/09/2026 - v1.00

 ******************************************************************************/

public class UserDAO
{
    private static final Connection conn = getConnection();

    public static boolean createUser(String username, String plainTextPassword)
    {
        // Attempt to add a new user to the database
        // username must be unique
        // Will return true if successful, false if username already exists

        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql))
        {
            statement.setString(1, username);
            statement.setString(2, hash(plainTextPassword));

            statement.executeUpdate();

            return true;
        }
        catch (PSQLException e)
        {
            // Check for unique constraint violation
            if ("23505".equals(e.getSQLState()))
            {
                // Username already exists
                return false;
            }
            throw new RuntimeException(e);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static User getUser(String username)
    {
        // Method to select user details from the database and return a ResultSet object

        String sql = "SELECT userID, username, password FROM users WHERE username = ?";

        // Attempt to execute query
        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                return new User(
                        resultSet.getString("username"),
                        resultSet.getString("userID"),
                        resultSet.getString("password")
                );
            }
            return null;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        System.out.println(getUser("Tom"));
    }
}
