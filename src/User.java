/******************************************************************************

 File        : User.java

 Date        : Tuesday 25th August 2026

 Author      : Tom Melton

 Description : Class describing a user

 History     : 25/08/2026 - v1.00

 ******************************************************************************/

public class User
{
    private String username;
    private String id;

    public User(String username, String id)
    {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return
            "id: " + this.id + "\n" +
            "username: " + this.username;
    }
}
