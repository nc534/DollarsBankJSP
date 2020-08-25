<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<style><%@include file="/WEB-INF/css/mainstyle.css"%></style>
<title>Deposit/Withdraw</title>
</head>
<body class="customerMain">
	<div>
		<div>
			<h3>Deposit/Withdraw</h3>
                    
            <div class="error">${errorMessage}</div>
                    
            <form action="<%= request.getContextPath() %>/transaction" method="post" class="form">
            
            	<div>
                     <label for="transaction">Transaction Type</label>
                     <select name="transaction">
                     	<option value="">--Please choose an option--</option>
    					<option value="deposit">Deposit</option>
    					<option value="withdraw">Withdraw</option>
                     </select>
                </div>
                <div>
                     <label for="account">Account</label>
                     <select name="account">
                     	<option value="">--Please choose an option--</option>
    					<option value="savings">Savings</option>
    					<option value="checking">Checking</option>
                     </select>
                </div>
                <div>
            		<label for="transaction_amount">Amount</label>
            		<input type="number" name="transaction_amount" required placeholder="0.00" min="0.00" step="0.01"/>
                </div>
            	
            	<div>
            		<button type="submit" class="btn">Submit</button>
                     
                    <button type="button" name="cancel" class="cancel" onclick="history.back()">Cancel</button>
            	</div>
            </form>
		</div>
	</div>
</body>
</html>