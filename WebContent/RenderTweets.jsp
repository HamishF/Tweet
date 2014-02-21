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
<style>
li{
display:inline;
}
</style>
</head>
<body>
<h1>Twitter Clone</h1>

<div class="Displaydiv4">
<ul>

<form action="Tweet" method="post">
	<li><input type= "submit" value="Post Tweet" name="Post" class="myButton"></li>
	<input type="text" name="Textbox_tweet" placeholder="Enter tweet"/>
</form>

<form action="Tweet" method="post">
	<li><input type="submit" value="View all tweets" name="viewall" class="myButton"></li>
</form>

<form action="Tweet" method="post">
	<li><input type="submit" value="View all friends tweets" name="viewfriend" class="myButton"></li>
</form>

<form action="Tweet" method="post">
	<li><input type="submit" value="View all friends" name="viewallfriend" class="myButton"></li>
</form>

<form action="Tweet" method="post">
	<li><input type="submit" value="Search for a Users Tweets" name="Search" class="myButton"></li>
	<input type="text" name="Textbox_search" placeholder="Enter users name"/>
</form>

<form action="Tweet" method="post">
	<li><input type="submit" value="Delete Your Tweets" name="Delete" class="myButton"></li>
</form>

<form action="Tweet" method="post">
	<li><input type="submit" value="Add a friend" name="friend" class="myButton"></li>
	<input type="text" name="Add" placeholder="Enter friends username"/>
</form>

<form action="Tweet" method="post">
	<li><input type="submit" value="Update Details" name="Update_details" class="myButton"></li>
</form>
</div>
</ul>

<div class="Displaydiv5">
<form action="Tweet" method="post">
	<li><input type="submit" value="Logout" name="Logout" class="myButton"></li>
</form>
</div>

<br></br>

<div class="Displaydiv2">
<h2>Tweets</h2>
<%
System.out.println("In render");
List<TweetStore> lTweet = (List<TweetStore>)request.getAttribute("Tweets");
if (lTweet==null){
 %>
	<p>No Tweet found</p>
	<% 
}else{
Iterator<TweetStore> iterator;


iterator = lTweet.iterator();     
while (iterator.hasNext()){
	TweetStore ts = (TweetStore)iterator.next();

	%>
	<%=ts.getUser() %> -
	<%=ts.getTweet() %>
	</br>
	<hr>
	<br></br>
<%
}
}
%>
</div>
</body>
</html>