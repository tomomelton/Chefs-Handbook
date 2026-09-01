package utils;

import org.mindrot.jbcrypt.BCrypt;

/******************************************************************************

 File        : utils.Hash.java

 Date        : Tuesday 1st September 2026

 Author      : Tom Melton

 Description : Utility class to contain methods regarding hashing

 History     : 01/09/2026 - v1.00

 ******************************************************************************/

public class Hash
{
    public static String hash(String password)
    {
        // Function to hash a password, to be used before entry to database
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public static boolean checkPassword(String plainTextPassword, String hashedPassword)
    {
        // compares the plain text password to its hashed counterpart
        // Returns true if the arguments match via the method
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
