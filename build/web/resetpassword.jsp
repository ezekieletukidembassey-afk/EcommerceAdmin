<%-- 
    Document   : resetpassword
    Created on : Sep 9, 2026, 1:47:48 PM
    Author     : Ezekiel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Your Password</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f6f9; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .card { background: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); width: 100%; max-width: 400px; box-sizing: border-box; }
        h2 { margin-bottom: 20px; color: #333333; text-align: center; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; color: #666666; font-size: 14px; }
        input[type="password"] { width: 100%; padding: 10px; border: 1px solid #cccccc; border-radius: 4px; box-sizing: border-box; font-size: 16px; }
        button { width: 100%; padding: 12px; background-color: #007bff; border: none; border-radius: 4px; color: white; font-size: 16px; cursor: pointer; font-weight: bold; margin-top: 10px; }
        button:hover { background-color: #0056b3; }
        .error-msg { color: #dc3545; font-size: 13px; margin-top: 5px; display: none; }
    </style>
</head>
<body>

<div class="card">
    <h2>Reset Password</h2>
    
    <form action="resetpassword" method="POST" onsubmit="return validatePasswords()">
        <!-- The critical hidden token passed from ResetPasswordServlet -->
        <input type="hidden" name="token" value="${token}">
        
        <div class="form-group">
            <label for="newPassword">New Password</label>
            <input type="password" id="newPassword" name="newPassword" required minlength="8">
        </div>
        
        <div class="form-group">
            <label for="confirmPassword">Confirm New Password</label>
            <input type="password" id="confirmPassword" required>
            <div id="error" class="error-msg">Passwords do not match.</div>
        </div>
        
        <button type="submit">Update Password</button>
    </form>
</div>

<script>
    // Simple client-side validation to ensure both fields match
    function validatePasswords() {
        var password = document.getElementById("newPassword").value;
        var confirm = document.getElementById("confirmPassword").value;
        var errorDiv = document.getElementById("error");
        
        if (password !== confirm) {
            errorDiv.style.display = "block";
            return false; // Blocks the form submission
        }
        errorDiv.style.display = "none";
        return true;
    }
</script>

</body>
</html>

