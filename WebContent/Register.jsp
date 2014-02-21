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

<h2>Register Page</h2>

</br>

<div class="Displaydiv3">
<form action="Tweet" method="post">
	Username:  	<input type="text" name="Register_Username"/></br>
	Email:		<input type="text" name="Register_Email"/></br>
	Password:	<input type="Password" name="Register_Password"/></br>
	<input type= "submit" value="Register" name="Register" class="myButton">
</form>
<form method = "link" action="Login.jsp">
	<input type= "submit" value="Return" name="Post" class="myButton">
</form>
</div>
</body>
</html>