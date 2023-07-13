package org.dal.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

public class CustomFileFormatHandler {

    private static final String SECRET_KEY = "MySecretKey12345";

    public static byte[] encryptData(byte[] fileContent) throws Exception {

        // Generate the secret key from the provided passphrase
        byte[] keyBytes = SECRET_KEY.getBytes();
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        keyBytes = sha.digest(keyBytes);
        keyBytes = Arrays.copyOf(keyBytes, 16); // Use only first 128 bits for AES

        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        // Create the cipher and encrypt the file content
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedContent = cipher.doFinal(fileContent);

        return encryptedContent;
    }

    public static byte[] decryptFile(byte[] encryptedContent) throws Exception {

        // Generate the secret key from the provided passphrase
        byte[] keyBytes = SECRET_KEY.getBytes();
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        keyBytes = sha.digest(keyBytes);
        keyBytes = Arrays.copyOf(keyBytes, 16); // Use only first 128 bits for AES

        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        // Create the cipher and decrypt the encrypted content
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedContent = cipher.doFinal(encryptedContent);
        return decryptedContent;
    }
}
