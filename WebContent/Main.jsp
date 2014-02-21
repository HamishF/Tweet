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
<h1>Twitter Clone</h1>
<ul>

<form method = "link" action="message.jsp">
<li><input type= "submit" value="Submit" name="Post" class="myButton"></li>
</form>

<form method = "link" action="message.jsp">
<li><input type="submit" value="Search" name="Search" class="myButton"></li>
</form>

<form method = "link" action="Delete.jsp">
<li><input type="submit" value="Delete Your Tweets" name="Delete" class="myButton"></li>
</form>

<form method = "link" action="Login.jsp">
<li><input type="submit" value="Logout" name="Logout" class="myButton"></li>
</form>

<form method = "link" action="message.jsp">
<li><input type="submit" value="Add friend" name="friend" class="myButton"></li>
</form>

<form method = "link" action="Update.jsp">
<li><input type="submit" value="Update" name="Update_details" class="myButton"></li>
</form>
</ul>

<div class="Displaydiv2">
<%
System.out.println("In render");
List<TweetStore> lTweet = (List<TweetStore>)request.getAttribute("Tweets");
if (lTweet==null){
 %>
	<p>No Tweet found</p>
	<% 
}else{
%>

<% 
Iterator<TweetStore> iterator;


iterator = lTweet.iterator();     
while (iterator.hasNext()){
	TweetStore ts = (TweetStore)iterator.next();

	%>
	<%=ts.getUser() %> -
	<%=ts.getTweet() %>
	<br/>
	<br></br>
<%
}
}
%>
</div>
</body>
</html>