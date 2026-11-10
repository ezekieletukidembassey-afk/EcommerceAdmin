/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ecommerceAdmin;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;




/**
 *
 * @author Ezekiel
 */
public class ResetPasswordServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */


    // Displays the password reset form if token is valid
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        
        // Forward token directly to the JSP page
        request.setAttribute("token", token);
        request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
    }

    // Processes the password change
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword"); 
        
        String jdbcUrl = "jdbc:mysql://localhost:3306/ecommerceAdmin";
        String dbUser = "root";
        String dbPass = "password";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass)) {
                
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

        response.sendRedirect("message.jsp?text=Error: Invalid or expired password reset link.");
    }
}

