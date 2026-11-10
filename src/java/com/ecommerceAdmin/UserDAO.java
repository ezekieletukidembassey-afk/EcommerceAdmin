 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerceAdmin;

import com.ecommerceAdmin.util.NativePasswordHash;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ezekiel
 */
public class UserDAO {
   public static int insertUser(User user) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {        
        try(Connection connection = DBConfiguration.getConnection();) {
            byte[] salt = NativePasswordHash.getSalt();
            String hashedPassword = NativePasswordHash.hashPassword(user.getPassword(), salt);
            String sql = "INSERT INTO user (firstname, lastname, username, email, password, salt) VALUES (?,?,?,?,?,?)";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, user.getFirstName());
            pst.setString(2, user.getLastName());
            pst.setString(3, user.getUsername());
            pst.setString(4, user.getEmail());
            pst.setString(5, hashedPassword);
            pst.setBytes(6, salt);
            int update = pst.executeUpdate();
            //ResultSet rs = pst.executeQuery();
            return update;
        }
    }
   
    public static User loginUser(String username, String password) throws SQLException, ClassNotFoundException{
       try(Connection connection = DBConfiguration.getConnection();){
           String sql = "SELECT * FROM user WHERE username = ?";
           PreparedStatement pst = connection.prepareStatement(sql);
           pst.setString(1, username);
           ResultSet rs = pst.executeQuery();
           if(rs.next())
           {
               String hashedPassword = rs.getString("password");
               byte[] salt = rs.getBytes("salt");
               if(NativePasswordHash.authenticate(password, hashedPassword, salt)) 
               {
                    User user = new User();
                    user.setEmail(rs.getString("email"));
                    user.setFirstName(rs.getString("firstname"));
                    user.setLastName(rs.getString("lastname"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));

                    return user;
               }
               else
                   return null;
           }
           else
               return null;
       }
    } 
    
    public static void resetPassword() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DBConfiguration.getConnection()) {
                
                // Step 1: Check if token exists and hasn't expired yet
                String checkTokenSql = "SELECT user_id FROM password_resets WHERE token = ? AND expiry_time > NOW()";
                PreparedStatement ps1 = conn.prepareStatement(checkTokenSql);
                ps1.setString(1, token);
                ResultSet rs = ps1.executeQuery();

                if (rs.next()) {
                    int userId = rs.getInt("user_id");

                    // Step 2: Update the password in users table (Hash this password in production!)
                    String updatePassSql = "UPDATE users SET password_hash = ? WHERE id = ?";
                    PreparedStatement ps2 = conn.prepareStatement(updatePassSql);
                    ps2.setString(1, newPassword); 
                    ps2.setInt(2, userId);
                    ps2.executeUpdate();

                    // Step 3: Delete token immediately so it cannot be used again
                    String deleteTokenSql = "DELETE FROM password_resets WHERE user_id = ?";
                    PreparedStatement ps3 = conn.prepareStatement(deleteTokenSql);
                    ps3.setInt(1, userId);
                    ps3.executeUpdate();

                    response.sendRedirect("message.jsp?text=Password updated successfully.");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
