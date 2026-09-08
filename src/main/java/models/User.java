package models;

/******************************************************************************

 File        : models.User.java

 Date        : Tuesday 25th August 2026

 Author      : Tom Melton

 Description : Class describing a user

 History     : 25/08/2026 - v1.00

 ******************************************************************************/

public class User
{
    private String username;
    private int id;
    private String passwordHash = " ";

    public User(String username, int id)
    {
        this.username = username;
        this.id = id;
    }

    public User(String username, int id, String passwordHash)
    {
        this.username = username;
        this.id = id;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String toString()
    {
        return
            "id: " + this.id + "\n" +
            "username: " + this.username;
    }
}
