<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="charset=UTF-8">
<style><%@include file="/WEB-INF/css/homestyle.css"%></style>
<title>DollarsBank</title>
</head>
<body>
	<div class="main">
		<h1>DOLLARSBANK Welcomes You!!</h1>
			<div class="container">
				<div class="header">
					Login
                </div>
                
                <div class="error">${errorMessage}</div>
				<div class="message">${Message}</div>
				
                <form action="<%= request.getContextPath() %>/login" method="post" class="form">
                	<div class="form-group">
                		<label for="username">Username</label>
                        <input type="text" name="username" required placeholder="username"/>
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" name="password" required placeholder="password"/>
                    </div>
                    <div>
                        <button type="submit" class="btn">Login</button>
                    </div>
                    <div>
                        <p>Don't have an account? </p><a href="register">Register</a>
                    </div>
                </form>
            </div>
    </div>
</body>
</html>