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

<h2>Friends Page</h2>

<br><br/>

<div class="Displaydiv2">
<% 

Set<String> friends = (Set)request.getAttribute("Friends");

Iterator iterator = friends.iterator();
while(iterator.hasNext()){
	String friend = (String) iterator.next();
	%>
	<%=friend%>
	</br>
	<form action="Tweet" method="post">
		<input type="hidden" name="DeleteFriend" value= <%=friend%>>
		<input type="submit" value="Delete Friend" name="DeleteFriends" class="myButton">
		<hr>
	</form>
	</br>
	<%
}

%>


<form action="Tweet" method="post">
	<input type= "submit" value="Return" name="Return" class="myButton">
</form>

</div>
</body>
</html>
