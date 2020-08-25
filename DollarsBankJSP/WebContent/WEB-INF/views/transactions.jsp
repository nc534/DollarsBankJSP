<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.dollarsbank.models.Transaction"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<style><%@include file="/WEB-INF/css/mainstyle.css"%></style>
<title>Transactions</title>
</head>
<body class="customerMain">
	<div>
		<nav>
            <div class="bank">Dollars Bank</div>
                <ul class="nav">
                    <li><a href='<%=request.getContextPath()%>/myaccount' class='link'>Account Overview</a></li>
                    <li><a href='<%=request.getContextPath()%>/transaction' class='link'>Deposit/Withdraw</a></li>
                    <li><a href='<%=request.getContextPath()%>/transfer' class='link'>Transfer</a></li>
                    <li><a href='<%=request.getContextPath()%>/transactions' class='link'>Recent Transactions</a></li>
                    <li><a href='<%=request.getContextPath()%>/accountinfo' class='link'>Account Information</a></li>
                    <li><a href='<%=request.getContextPath()%>/logout' class='link'>Log Out</a></li>
                </ul>
        </nav>
	</div>
	<div class="transactions">
		<h3>Recent Transactions</h3>
		
			<table>
				<tr>
					<th>Account ID</th>
					<th>Transaction Date</th>
					<th>Transaction Type</th>
					<th>Transfer From</th>
					<th>Transfer To</th>
					<th>Transaction Amount</th>
				</tr>
				<%! @SuppressWarnings("unchecked") %>
				<% List<Transaction> transactions = (ArrayList<Transaction>) request.getAttribute("transactions");
	 				
			    for(Transaction transaction : transactions)
				    {
				    	%>
				    	<tr>
				    		<td><%out.print(transaction.getAccountId());%></td>
				    		<td><%out.print(transaction.getTransactionDate());%></td>
				    		<td><%out.print(transaction.getTransactionType());%></td>
				    		<td><%out.print(transaction.getTransferFrom());%></td>
				    		<td><%out.print(transaction.getTransferTo());%></td>
				    		<td><%out.print(transaction.getTransactionAmount());%></td>
				    	</tr>
		            	<%
				    }
			 	
				%>
			</table>
    </div>

</body>
</html>