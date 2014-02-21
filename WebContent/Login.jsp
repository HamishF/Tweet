<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
    <%@ page import="com.example.hamish.stores.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="mystyle.css">
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Tweets</title>
</head>
<body> 
<h2>Login Page</h2>

<br><br/>
<div class="Displaydiv3">
<%
Boolean fail = (Boolean)request.getAttribute("invalidlog");
if(fail != null){
%>
<form action="Tweet" method="post">
	<strong>Invalid Details Used</strong>
	</br>
	<b>Username: </b><input type="text" name="Username"/>
	<br></br>
	<b>Password: </b><input type="Password" name="Password"/>
	<br></br>
	<input type= "submit" value="Login" name="Login" class="myButton">
</form>

<%}else{%>

<form action="Tweet" method="post">
	<b>Username: </b><input type="text" name="Username"/>
	<br></br>
	<b>Password: </b><input type="Password" name="Password"/>
	<br></br>
	<input type= "submit" value="Login" name="Login" class="myButton">
</form>
<%}%>


<br><br/>

<form action="Tweet" method="post">
<a href="Register.jsp">Register</a>
</form>

<br><br/>
</div>
</body>
</html>