import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/******************************************************************************

 File        : Connector.java

 Date        : Wednesday 26th August 2026

 Author      : Tom Melton

 Description : Class which creates a connection to my postgresql database

 History     : 26/08/2026 - v1.00

 ******************************************************************************/


public class Connector
{
    // load .env data
    private static final Dotenv dotenv = Dotenv.load();

    // retrieve keys from .env
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    // test database connection
    public static void main(String[] args)
    {

        try (Connection connection = Connector.getConnection())
        {
            System.out.println("Successfully connected to PostgreSQL!");
        }
        catch (SQLException e)
        {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}
