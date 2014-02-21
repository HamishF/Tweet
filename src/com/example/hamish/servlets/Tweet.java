package com.example.hamish.servlets;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.utils.UUIDs;
import com.example.hamish.lib.*;
import com.example.hamish.models.*;
import com.example.hamish.stores.*;

/**
 * Servlet implementation class Tweet
 */
@WebServlet({ "/Tweet", "/Tweet/*" })
public class Tweet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Cluster cluster;
	String name = null; 									// name of the user logged in
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Tweet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		cluster = CassandraHosts.getCluster(); 
	}
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		TweetModel tm= new TweetModel(); 									//creates an instance of the tweet model
		tm.setCluster(cluster);												//sets the tweet model 
		tm.createDB();														//calls the method createDB() in tweet model
		LinkedList<TweetStore> tweetList = tm.getTweets(); 					//creates a linked list of tweet strores and holds the linked list returned
		request.setAttribute("Tweets", tweetList); 							//Set a bean with the list in it
		RequestDispatcher rd = request.getRequestDispatcher("/Login.jsp");	//Send the user to the login page
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String tweet = request.getParameter("Textbox_tweet");	//stores the data held in a text box on a jsp page
		String user = request.getParameter("Textbox_search");	//stores the data held in a text box on a jsp page
		String user_name = request.getParameter("Textbox_name");//stores the data held in a text box on a jsp page
		Boolean logged = false;
		
		TweetModel tm= new TweetModel(); 
		tm.setCluster(cluster);
		
		
		//These if statements are used to check if a button on any of the jsp page has been clicked
		if(request.getParameter("Post") != null ){												//Used to post a tweet
			tm.postTweet(tweet, name);
			LinkedList<TweetStore> tweetList = tm.getTweets(); 
			request.setAttribute("Tweets", tweetList); 
			RequestDispatcher rd = request.getRequestDispatcher("/RenderTweets.jsp"); 
			rd.forward(request, response);
		}
		else if(request.getParameter("Search") != null){										//used when searching for a particular user
			LinkedList<TweetStore> tweetList = tm.searchTweet(user); 
			request.setAttribute("Tweets", tweetList); 
			RequestDispatcher rd = request.getRequestDispatcher("/RenderTweets.jsp"); 
			rd.forward(request, response);
		}
		else if(request.getParameter("Delete") != null){										//Used when user clicks delete on rendertweets.jsp
			LinkedList<TweetStore> tweetsList = tm.getTweetsUser(name); 						//takes user to the delete jsp page
			request.setAttribute("User_Tweets", tweetsList); 
			RequestDispatcher rd = request.getRequestDispatcher("/Delete.jsp"); 
			rd.forward(request, response);
		}
		else if(request.getParameter("DeleteTweet") != null){									//used to delete a tweet
			String time = request.getParameter("ID");
			UUID id = UUID.fromString(time);
			tm.deleteTweet(name, id);
			LinkedList<TweetStore> tweetList = tm.getTweets(); 
			request.setAttribute("Tweets", tweetList); 
			RequestDispatcher rd = request.getRequestDispatcher("/RenderTweets.jsp"); 
			rd.forward(request, response);
		}
		else if(request.getParameter("Login") != null){											//Used to login
			String username = request.getParameter("Username");
			name = username;
			String password = request.getParameter("Password");
			logged = tm.login(username, password);
			if(logged == true){
				LinkedList<TweetStore> tweetList = tm.getTweets(); 
				request.setAttribute("Tweets", tweetList);
				RequestDispatcher rd = request.getRequestDispatcher("/RenderTweets.jsp"); 
				rd.forward(request, response);
			}
			else{
				boolean loginfail = true;
				request.setAttribute("invalidlog", loginfail); 
				RequestDispatcher rd = request.getRequestDispatcher("/Login.jsp"); 
				rd.forward(request, response);	
			}
		}
		else if(request.getParameter("Register") != null){										//used to register a new user
			String regUser = request.getParameter("Register_Username");
			String regEmail = request.getParameter("Register_Email");
			String regPass = request.getParameter("Register_Password");
			Boolean registered = tm.register(regUser,regEmail,regPass);
			if(registered == true){
				RequestDispatcher rd = request.getRequestDispatcher("/Login.jsp"); 
				rd.forward(request, response);
			}
			else{
				System.out.print("User already registered");
				RequestDispatcher rd = request.getRequestDispatcher("/Register.jsp"); 
			}
		}
		else if(request.getParameter("Logout") != null){										//logs the user out
			RequestDispatcher rd = request.getRequestDispatcher("/Login.jsp"); 
			rd.forward(request, response);
		}	
		else if(request.getParameter("friend") != null){										//used to add a friend
			String add = request.getParameter("Add");
			tm.add(name, add);
			LinkedList<TweetStore> tweetList = tm.getTweets(); 
			request.setAttribute("Tweets", tweetList); 
			RequestDispatcher rd = request.getRequestDispatcher("/RenderTweets.jsp"); 
			rd.forward(request, response);
		}	
		else if(request.getParameter("Update") != null){										//used to update the details of the user logged in
			String Email = request.getParameter("Update_email");
			String Passwords = request.getParameter("update_Password");
			String previous_pass = request.getParameter("previouspass");
			System.out.println(Email);
			System.out.print(Passwords);
			tm.updated(name, Email, Passwords, previous_pass);
			LinkedList<TweetStore> tweetList = tm.getTweets();
			request.setAttribute("Tweets", tweetList); 
			RequestDispatcher rd = request.getRequestDispatcher("/RenderTweets.jsp");  
			rd.forward(request, response);
		}
		else if(request.getParameter("Update_details") != null){								//Used to take the user to the update jsp page
			String em = tm.getEmails(name);
			String pass = tm.getPass(name);
			request.setAttribute("Emails", em); 
			request.setAttribute("Passwords", pass); 
			RequestDispatcher rd = request.getRequestDispatcher("/Update.jsp");  
			rd.forward(request, response);
		}
		else if(request.getParameter("viewall") != null){										//used to view all the tweets available
			LinkedList<TweetStore> tweetList = tm.getTweets();
			request.setAttribute("Tweets", tweetList); 
			RequestDispatcher rd = request.getRequestDispatcher("/RenderTweets.jsp");  
			rd.forward(request, response);
		}
		else if(request.getParameter("viewfriend") != null){									//Used to view all a users friends tweets
			LinkedList<TweetStore> tweetList = tm.getfriendsTweets(name); 
			request.setAttribute("Tweets", tweetList); 
			RequestDispatcher rd = request.getRequestDispatcher("/RenderTweets.jsp");   
			rd.forward(request, response);
		}
		else if(request.getParameter("viewallfriend") != null){									//used to take the user to the friend jsp page
			Set<String> mySet = tm.getfriends(name); 
			request.setAttribute("Friends", mySet); 
			RequestDispatcher rd = request.getRequestDispatcher("/Friends.jsp");   
			rd.forward(request, response);
		}
		else if(request.getParameter("DeleteFriends") != null){									//Used to delete a friend
			String friend = request.getParameter("DeleteFriend");
			tm.Del(name, friend); 
			LinkedList<TweetStore> tweetList = tm.getTweets();
			request.setAttribute("Tweets", tweetList); 
			RequestDispatcher rd = request.getRequestDispatcher("/RenderTweets.jsp"); 
			rd.forward(request, response);
		}
		else if(request.getParameter("DeleteProfile") != null){									//Used to delete a users profile
			tm.DelProf(name); 
			RequestDispatcher rd = request.getRequestDispatcher("/Login.jsp");  
			rd.forward(request, response);
		}
		else if(request.getParameter("Return") != null){											//Used to when return button pressed
			LinkedList<TweetStore> tweetList = tm.getTweets(); 					
			request.setAttribute("Tweets", tweetList); 						
			RequestDispatcher rd = request.getRequestDispatcher("/RenderTweets.jsp");
			rd.forward(request, response);
		}
	}
}