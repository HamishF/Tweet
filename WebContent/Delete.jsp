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

<h2>Delete Page</h2>
<div class="Displaydiv3">
<%
System.out.println("In render");
List<TweetStore> lTweet = (List<TweetStore>)request.getAttribute("User_Tweets");
int count = 0;
if (lTweet==null){
 %>
	<p>No Tweet found</p>
	<% 
}else{

Iterator<TweetStore> iterator;


iterator = lTweet.iterator();     
while (iterator.hasNext()){
	TweetStore ts = (TweetStore)iterator.next();
	count++;
	%>
	<%=ts.getTweet()%>
	<br/>
	<form action="Tweet" method="post">
		<input type="hidden" name="ID" value= <%=ts.getId()%>>
		<input type="submit" value="Delete" name="DeleteTweet" class="myButton">
	</form>
	<br></br>
	
<%
}
}
%>
<form action="Tweet" method="post">
	<input type= "submit" value="Return" name="Return" class="myButton">
</form>
</div>
</body>
</html>