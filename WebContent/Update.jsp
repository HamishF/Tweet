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

<h2>Update Details Page</h2>

<br><br/>
<% 
String emails = (String)request.getAttribute("Emails");
String passwords = (String)request.getAttribute("Passwords");
%>
<div class="Displaydiv3">
<form action="Tweet" method="post">
	<p>Email: </p><input type="text" name="Update_email" value= <%=emails%> />
	<p>Previous Password: </p><input type="text" name="previouspass" value= <%=passwords%> />
	<p>New Password: </p><input type="password" name="update_Password" value= <%=passwords%> />
	</br>
	</br>
	<input type= "submit" value="Update" name="Update" class="myButton">
	</br>
</form>
</br>
<form action="Tweet" method="post">
	</br>
	<input type= "submit" value="Delete Profile" name="DeleteProfile" class="myButton">
	</br>
</form>
</br>
<form action="Tweet" method="post">
	<input type= "submit" value="Return" name="Return" class="myButton">
</form>

</div>
</body>
</html>

