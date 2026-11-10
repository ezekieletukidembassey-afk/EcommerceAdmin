/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ecommerceAdmin;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        
        // Database credentials (adjust for your setup)
        String jdbcUrl = "jdbc:mysql://localhost:3306/ecommerce";
        String dbUser = "root";
        String dbPass = "password";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass)) {
                
                // Step 1: Find user_id by email
                String findUserSql = "SELECT id FROM users WHERE email = ?";
                PreparedStatement ps1 = conn.prepareStatement(findUserSql);
                ps1.setString(1, email);
                ResultSet rs = ps1.executeQuery();

                if (rs.next()) {
                    int userId = rs.getInt("id");

                    // Step 2: Delete any old existing tokens for this user
                    String deleteOldSql = "DELETE FROM password_resets WHERE user_id = ?";
                    PreparedStatement psDelete = conn.prepareStatement(deleteOldSql);
                    psDelete.setInt(1, userId);
                    psDelete.executeUpdate();

                    // Step 3: Generate a secure token and 30-minute expiry
                    String token = UUID.randomUUID().toString();
                    Timestamp expiry = new Timestamp(System.currentTimeMillis() + (30 * 60 * 1000));

                    // Step 4: Insert token into password_resets table
                    String insertSql = "INSERT INTO password_resets (user_id, token, expiry_time) VALUES (?, ?, ?)";
                    PreparedStatement ps2 = conn.prepareStatement(insertSql);
                    ps2.setInt(1, userId);
                    ps2.setString(2, token);
                    ps2.setTimestamp(3, expiry);
                    ps2.executeUpdate();

                    // Step 5: Construct the link (Send this via email in real app)
                    String resetLink = "http://localhost:8080/your-app/reset-password?token=" + token;
                    System.out.println("Simulated Email Sent Link: " + resetLink);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Always show generic message for security
        response.sendRedirect("message.jsp?text=If that email is in our system, instructions were sent.");
    }
}

