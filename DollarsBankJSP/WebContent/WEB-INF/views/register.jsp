<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<style><%@include file="/WEB-INF/css/homestyle.css"%></style>
<title>Registration</title>
</head>
<body>
	<div class="main">
		<div class="container">
			<div class="header">
				Register
            </div>
                    
            <div class="error">${errorMessage}</div>
                    
            <form action="<%= request.getContextPath() %>/register" method="post" class="form">
            	<div class="form-group">
                     <label for="account">Account</label>
                     <select name="account">
                     	<option value="">--Please choose an option--</option>
    					<option value="savings">Savings</option>
    					<option value="checking">Checking</option>
                     </select>
                </div>
                <div class="form-group">
            		<label for="initial_deposit">Amount</label>
            		<input type="number" name="initial_deposit" required placeholder="0.00" min="0.00" step="0.01"/>
                </div>
            	<div class="form-group">
            		<label for="name">Name</label>
            		<input type="text" name="name" required placeholder="name"/>
                </div>
                <div class="form-group">
                    <label for="address">Address</label>
                    <input type="text" name="address" required placeholder="address"/>
                </div>
                <div class="form-group">
                    <label for="phone">Phone</label>
                    <input type="text" name="phone" required placeholder="phone"/>
                </div>
                <div class="form-group">
                     <label for="username">Username</label>
                     <input type="text" name="username" required placeholder="username"/>
                </div>
                <div class="form-group">
                     <label for="password">Password</label>
                     <input type="password" name="password" required placeholder="password"
                     pattern="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}" 
					 title="Must contain at least one number, one uppercase, one lowercase letter, and at least 8 or more characters"/>
                </div>
                     <button type="submit" class="btn">Register</button>
               	<div>
               		<p>Already have an account?</p><a href="login">Login</a>
               	</div>
            </form>
		</div>
	</div>
</body>
</html>