package org.dal.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {

    public static String encryptPassword(String password) {
        try {
            // Create an instance of the MD5 message digest algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Convert the password string to bytes
            byte[] passwordBytes = password.getBytes();

            // Perform the MD5 hashing
            byte[] hashedBytes = md.digest(passwordBytes);

            // Convert the hashed bytes to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                sb.append(Integer.toString((hashedByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception if MD5 algorithm is not available
            e.printStackTrace();
            return null;
        }
    }

    public static boolean comparePasswords(String inputPassword, String storedPassword) {
        String encryptedInputPassword = encryptPassword(inputPassword);
        return encryptedInputPassword.equals(storedPassword);
    }
}
