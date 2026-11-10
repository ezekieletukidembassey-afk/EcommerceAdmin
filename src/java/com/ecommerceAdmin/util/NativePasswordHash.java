/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerceAdmin.util;
import java.security.MessageDigest;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class NativePasswordHash {
    private static final int ITERATIONS = 600000; // Modern security threshold
    private static final int KEY_LENGTH = 256;

    // 1. Generate a unique, random salt for every user
    public static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // 2. Hash the password
    public static String hashPassword(String password, byte[] salt) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] passwordChars = password.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(passwordChars, salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }
    
    /**
     * Authenticates the user by verifying the entered password against the stored hash.
     * 
     * @param enteredPassword The plain-text password from the login form
     * @param storedHash      The Base64 encoded hash fetched from the database
     * @param storedSalt      The original byte[] salt fetched from the database
     * @return true if the password is correct, false otherwise
     */
    public static boolean authenticate(String enteredPassword, String storedHash, byte[] storedSalt) {
        try {
            // 1. Hash the incoming password using the exact same salt and iterations
            String newHash = hashPassword(enteredPassword, storedSalt);
            
            // 2. Decode both hashes to raw byte arrays for safe comparison
            byte[] newHashBytes = Base64.getDecoder().decode(newHash);
            byte[] storedHashBytes = Base64.getDecoder().decode(storedHash);
            
            // 3. Prevent timing attacks by using MessageDigest.isEqual()
            return MessageDigest.isEqual(newHashBytes, storedHashBytes);
            
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {
            // Log the exception in production
            return false; 
        }
    }
}
