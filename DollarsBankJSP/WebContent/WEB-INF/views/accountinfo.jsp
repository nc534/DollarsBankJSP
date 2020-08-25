<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.dollarsbank.models.Customer"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<style><%@include file="/WEB-INF/css/mainstyle.css"%></style>
<title>Account Details</title>
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
	<div class="accountInfo">
		<h3>Your Account Information</h3>
		
		<% Customer customer = (Customer) request.getAttribute("customer"); %>
		
		<table>
			<tr><td>Name: <%out.print(customer.getName()); %></td></tr>
			<tr><td>Phone: <%out.print(customer.getPhone()); %></td></tr>
			<tr><td>Address: <%out.print(customer.getAddress()); %></td></tr>
			<tr><td>User Id: <%out.print(customer.getUserId()); %></td></tr>
		</table>

    </div>

</body>
</html>