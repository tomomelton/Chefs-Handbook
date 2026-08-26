import org.mindrot.jbcrypt.BCrypt;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
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


//    public void login(String username, String password)
//    {
//        // Attempts to log in a user
//
//        String sql = ""
//    }


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



    public static void main(String[] args) throws SQLException {
        Program p = new Program();

        if (p.register("Tom", "password"))
        {
            System.out.println("Successfully registered!");
        }
        else
        {
            System.out.println("Username already exists!");
        }
    }
}
