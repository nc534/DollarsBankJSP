<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<style><%@include file="/WEB-INF/css/mainstyle.css"%></style>
<script type="text/javascript">

	function other() {
    	if (document.getElementById("selection").value === "other") {
        	document.getElementById("another").style.display = "block";
    	} else {
        	document.getElementById("another").style.display = "none";
    	}
	}
	
	var date = function(){
		today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1; //As January is 0.
		var yyyy = today.getFullYear();

		if(dd<10) dd='0'+dd;
		if(mm<10) mm='0'+mm;
		document.getElementById("date").placeholder = mm + "/" + dd + "/" + yyyy;
	};

</script>
<title>Transfer</title>
</head>
<body class="customerMain" onload="date()">
	<div>
		<div>
			<h3>Transfer</h3>
                    
            <div class="error">${errorMessage}</div>
                    
            <form action="<%= request.getContextPath() %>/transfer" method="post" class="form">
            
                <div>
                     <label for="account_from">Transfer From: </label>
                     <select name="account_from">
                     	<option value="">--Please choose an option--</option>
    					<option value="savings">Savings</option>
    					<option value="checking">Checking</option>
                     </select>
                </div>
                <div>
                     <label for="account_to">Transfer To: </label>
                     <select id="selection" name="account_to" onchange="other()">
                     	<option value="">--Please choose an option--</option>
    					<option value="savings">Savings</option>
    					<option value="checking">Checking</option>
    					<option value="other">Another Customer</option>
                     </select>
                </div>
                <div id="another"  style="display:none">
                	<label for="other_account">To Account Id:</label>
                	<input type="number" name="other_account" min="0" step="1" placeholder="account id"/>
                	<label for="phone">Phone of Other Customer: </label>
                	<input type="text" name="phone" placeholder="phone"/>
                </div>
                <div>
            		<label for="transaction_amount">Amount</label>
            		<input type="number" name="transaction_amount" required placeholder="0.00" min="0.00" step="0.01"/>
                </div>
                <div>
                	<p>Date: <input type="text" id="date" readonly placeholder=""></input></p>
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