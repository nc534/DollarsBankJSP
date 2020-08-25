<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<style><%@include file="/WEB-INF/css/mainstyle.css"%></style>
<title>New Account</title>
</head>
<body class="customerMain">
	<div>
		<div>
			<h3>New Account</h3>
                    
            <div class="error">${errorMessage}</div>
                    
            <form action="<%= request.getContextPath() %>/new-account" method="post" class="form">
            
            	<div>
                     <label for="account">Account</label>
                     <select name="account">
                     	<option value="">--Please choose an option--</option>
    					<option value="savings">Savings</option>
    					<option value="checking">Checking</option>
                     </select>
                </div>
                <div>
            		<label for="initial_deposit">Amount</label>
            		<input type="number" name="initial_deposit" required placeholder="0.00" min="0.00" step="0.01"/>
                </div>
            	
            	<div>
            		<button type="submit" class="btn">Open</button>
                     
                    <button type="button" name="cancel" onclick="history.back()" class="cancel">Cancel</button>
            	</div>
            </form>
		</div>
	</div>
</body>
</html>