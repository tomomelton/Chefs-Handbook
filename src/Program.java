import org.mindrot.jbcrypt.BCrypt;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

/******************************************************************************

 File        : Program.java

 Date        : Wednesday 26th August 2026

 Author      : Tom Melton

 Description : Program which manages users, recipes, and user interactions
               by interacting with the database

 History     : 26/08/2026 - v1.00

 ******************************************************************************/


public class Program
{
    User user;
    ArrayList<Recipe> recipes;

    // Database connection
    Connection conn = attemptConn();


    private Connection attemptConn()
    {
        // Attempts to connect to the database using Connector
        try
        {
            return Connector.getConnection();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    public boolean login(String username, String password) throws SQLException
    {
        // Attempts to log in a user
        // Returns true if successful, false if failed (either username or password is incorrect)

        // fetch rows from users table with this username
        try (ResultSet userResult = userDetails(username))
        {
            // If username exists
            if (userResult.next())
            {
                String queryID, queryPassword;

                queryID = userResult.getString("userID");
                queryPassword = userResult.getString("password");

                if (checkPassword(password, queryPassword))
                {
                    this.user = new User(username, queryID);

                    return true;
                }
            }
            // username or password is incorrect
            return false;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    private ResultSet userDetails(String username) throws SQLException
    {
        String sql = "SELECT userID, username, password FROM users WHERE username = ?";

        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setString(1, username);

        return statement.executeQuery();
    }

    public boolean register(String username, String password) throws SQLException
    {
        // Attempt to add a new user to the database
        // username must be unique
        // Will return true if successful, false if username already exists

        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql))
        {
            statement.setString(1, username);
            statement.setString(2, hash(password));

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


    private String hash(String password)
    {
        // Function to hash a password, to be used before entry to database
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean checkPassword(String plainTextPassword, String hashedPassword)
    {
        // compares the plain text password to its hashed counterpart
        // Returns true if the arguments match via the method
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }



    public static void main(String[] args) throws SQLException {
        // testing details:
        // username = "Tom"
        // password = "password"

        Program p = new Program();



        String password = "password";

        System.out.println(p.hash(password));

        if (p.login("Tom", password))
        {
            System.out.println("Logged in!");

            System.out.println(p.user);
        }
        else
        {
            System.out.println("Username or password incorrect");
        }




    }
}
