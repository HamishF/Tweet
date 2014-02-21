package com.example.hamish.models;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace3;
  CREATE TABLE Tweets (
  user varchar,
  interaction_time uuid,
   tweet varchar,
   PRIMARY KEY (user,interaction_time)
  ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */

//Is the model, used to get the data from the cassandra database and stores it in a linked list of tweetstores
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.example.hamish.stores.TweetStore;

public class TweetModel {
	Cluster cluster;
	public TweetModel(){

	}

	public void setCluster(Cluster cluster){
		this.cluster=cluster;
	}
	
	//creates database if it doesn't exist
	public void createDB(){
		
		Session session = cluster.connect();
		session.execute("create keyspace if not exists HamishDB WITH replication = {'class':'SimpleStrategy','replication_factor':1};");
		
		Session session1 = cluster.connect("HamishDB"); 	
		session1.execute("CREATE TABLE if not exists Tweets (user varchar,interaction_time uuid,tweet varchar,PRIMARY KEY (user,interaction_time)) WITH CLUSTERING ORDER BY (interaction_time DESC);");
		session1.execute("CREATE TABLE if not exists Users (user varchar,email varchar,password varchar, PRIMARY KEY (user,password))");
		session1.execute("CREATE TABLE if not exists Subscribe_to (user varchar,subscription set<text>,PRIMARY KEY (user))");
	}
	
	
	
	//Method to get all the tweets available 
	public LinkedList<TweetStore> getTweets() { 								
		LinkedList<TweetStore> tweetList = new LinkedList<TweetStore>();		//creates a linked list made up of the TweetStore class 
		Session session = cluster.connect("HamishDB"); 							//session connect to the cluster and keyspace called HamishDB

		PreparedStatement statement = session.prepare("SELECT * from tweets");	//create the cql statement to be executed
		BoundStatement boundStatement = new BoundStatement(statement); 			
		ResultSet rs = session.execute(boundStatement);							// creates a result set to hold the data from the cql statement
		if (rs.isExhausted()) { 												//if the result set is empty
			System.out.println("No Tweets returned");
		} else { 																//else (meaning there is more data)
			for (Row row : rs) { 												//for each row
				TweetStore ts = new TweetStore(); 								//create a tweetstore
				ts.setTweet(row.getString("tweet")); 							//set the tweet in the tweetstore to the tweet returned in the row
				ts.setUser(row.getString("user")); 								//set the user in the tweetstore to the user returned in the row
				ts.setId(row.getUUID("interaction_time"));						//set the id in the tweetstore to the interaction_time in the row
				tweetList.add(ts); 												//add the tweetstore to linked list of tweetstores
			}
		}
		session.close();
		return tweetList;														//return the linked list of tweet stores
	}
	
	//Method to get all the tweets from a specific user
	public LinkedList<TweetStore> getTweetsUser(String name) { 					 
		LinkedList<TweetStore> tweetList = new LinkedList<TweetStore>();		//creates a linked list made up of the TweetStore class
		Session session = cluster.connect("HamishDB"); 							//session connect to the cluster and keyspace called HamishDB

		PreparedStatement statement = session.prepare("SELECT * from tweets where user = ?");		//cql statement to be executed
		BoundStatement boundStatement = new BoundStatement(statement); 			
		ResultSet rs = session.execute(boundStatement.bind(name));				// creates a result set to hold the data from the cql statement used on the session connected to keyspace2 
		if (rs.isExhausted()) { 												//if there is no more data left
			System.out.println("No Tweets returned");
		} else { 																//else (meaning there is more data)
			for (Row row : rs) { 												//for each row
				TweetStore ts = new TweetStore(); 								//create a tweetstore
				ts.setTweet(row.getString("tweet")); 							//set the tweet in the tweetstore to the tweet returned in the row
				ts.setId(row.getUUID("interaction_time"));						//set the id in the tweetstore to the interaction_time in the row
				tweetList.add(ts); 												//add the tweetstore to linked list of tweetstores
			}
		}
		session.close();
		return tweetList;
	}
	
	//Method to get all the friends of a user
	public Set<String> getfriends(String name) { 								
		Session session = cluster.connect("HamishDB"); 							
		Set<String> set = new HashSet<String> ();
		PreparedStatement statement = session.prepare("SELECT subscription from Subscribe_to where user = ?");	
		BoundStatement boundStatement = new BoundStatement(statement); 			
		ResultSet rs = session.execute(boundStatement.bind(name));							
		if (rs.isExhausted()) { 												
			System.out.println("No Tweets returned");
		} else { 																
			for (Row row : rs) { 	
				set = row.getSet("subscription", String.class);
				}
		}
		session.close();
		return set;
	}
	
	//Method to get all the tweets from a users set of friends
	public LinkedList<TweetStore> getfriendsTweets(String name) { 								
		LinkedList<TweetStore> tweetList = new LinkedList<TweetStore>();
		Session session = cluster.connect("HamishDB"); 						
		Set<String> set = new HashSet<String> ();
		PreparedStatement statement = session.prepare("SELECT subscription from Subscribe_to where user = ?");	
		BoundStatement boundStatement = new BoundStatement(statement); 			
		ResultSet rs = session.execute(boundStatement.bind(name));							  
		if (rs.isExhausted()) { 												
			System.out.println("No Tweets returned");
		} else { 																
			for (Row row : rs) { 												
				set = row.getSet("subscription", String.class);
				Iterator iterator = set.iterator();
				while(iterator.hasNext()){
					String friend = (String) iterator.next();
					PreparedStatement statement1 = session.prepare("SELECT * from tweets where user = ?");	
					BoundStatement boundStatement1 = new BoundStatement(statement1); 			
					ResultSet rs1 = session.execute(boundStatement1.bind(friend));
					if (rs1.isExhausted()) { 												
						System.out.println("No Tweets returned");
					} else { 																
						for (Row row1 : rs1) { 												
							TweetStore ts = new TweetStore(); 				
							ts.setUser(row1.getString("user")); 							
							ts.setTweet(row1.getString("tweet")); 							
							ts.setId(row1.getUUID("interaction_time"));						
							tweetList.add(ts); 												
						}
					}
				}
			}
		}
		
		session.close();
		return tweetList;
	}
	
	
	//Method to get the email of the user logged in
	public String getEmails(String user){
		String email = null;
		Session session = cluster.connect("HamishDB");
		PreparedStatement statement = session.prepare("SELECT email from users where user = ?");
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement.bind(user));
		if (rs.isExhausted()) { 												
			System.out.println("No Tweets returned");
		} else {
			for (Row row : rs) { 												
				email = row.getString("email");
			}
		}
		session.close();
		return email;
	}
	
	//Method to get the passwrod of the user logged in
	public String getPass(String user){
		String password = null;
		Session session = cluster.connect("HamishDB");
		PreparedStatement statement = session.prepare("SELECT password from users where user = ?");
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement.bind(user));
		if (rs.isExhausted()) { 												
			System.out.println("No Tweets returned");
		} else {
			for (Row row : rs) { 												
				password = row.getString("password");
			}
		}
		session.close();
		return password;
	}
	
	//Method to search for all of a users tweets
	public LinkedList<TweetStore> searchTweet(String user){		
		LinkedList<TweetStore> tweetList = new LinkedList<TweetStore>();
		Session session = cluster.connect("HamishDB"); 
		
		PreparedStatement statement = session.prepare("select * from tweets where user = ?");
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement.bind(user));
		if (rs.isExhausted()) { 												
			System.out.println("No Tweets returned");
		} else { 																
			for (Row row : rs) { 												
				TweetStore ts = new TweetStore();
				ts.setTweet(row.getString("tweet")); 							
				ts.setUser(row.getString("user")); 								
				ts.setId(row.getUUID("interaction_time"));														
			    tweetList.add(ts);
			}
		}
		session.close();
		return tweetList;
	}
	
	//Method to post a tweet
	public void postTweet(String tweet, String name){										
		Session session = cluster.connect("HamishDB"); 
		PreparedStatement statement = session.prepare("insert into tweets (user,interaction_time,tweet) values (?,?,?);");
		BoundStatement boundStatement = new BoundStatement(statement);
		UUID time = UUIDs.timeBased();
		session.execute(boundStatement.bind(name,time,tweet));
		session.close();
	}
	
	//Method to delete a tweet
	public void deleteTweet(String user, UUID id){
		Session session = cluster.connect("HamishDB"); 
		PreparedStatement statement = session.prepare("delete from tweets where user = ? AND interaction_time = ?;");
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement.bind(user, id));
		session.close();
	}
	
	//Method to login
	public boolean login(String username, String password){
		boolean loggedin = false;
		Session session = cluster.connect("HamishDB");
		PreparedStatement statement = session.prepare("select * from Users where user = ? AND password =?");
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement.bind(username, password));
		if (rs.isExhausted()) { //if there is no data returned
			loggedin = false;
		} else { 																
			loggedin = true;
		}
		return loggedin;
	}
	
	//Method to Register
	public boolean register(String username, String Email, String Password){
		boolean registered = false;
		Session session = cluster.connect("HamishDB");
		PreparedStatement statement1 = session.prepare("select * from Users where user = ? AND password =?");
		BoundStatement boundStatement1 = new BoundStatement(statement1);
		ResultSet rs = session.execute(boundStatement1.bind(username, Password));
		if (rs.isExhausted()) { //if there is no data returned
			PreparedStatement statement = session.prepare("insert into Users (user,email,password) values(?,?,?);");
			BoundStatement boundStatement = new BoundStatement(statement);
			session.execute(boundStatement.bind(username, Email,Password));
			PreparedStatement state = session.prepare("insert into subscribe_to (user) values(?);");
			BoundStatement bound = new BoundStatement(state);
			session.execute(bound.bind(username));
			registered = true;
		} else { 															
			registered = false;
		}
		return registered;
	}
	
	//Method to add a friend
	public void add(String user, String subscribe){
		Session session = cluster.connect("HamishDB");
		Set<String> sub = new HashSet<String>();
		sub.add(subscribe);
		PreparedStatement statement = session.prepare("update Subscribe_to set subscription = subscription + ? where user = ?;");
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement.bind(sub, user));
	}
	
	//Method to delete a friend
	public void Del(String user, String friend){
		Session session = cluster.connect("HamishDB");
		Set<String> sub = new HashSet<String>();
		sub.add(friend);
		PreparedStatement statement = session.prepare("update Subscribe_to set subscription = subscription - ? where user = ?;");
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement.bind(sub, user));
		session.close();
	}
	
	//Method to delete a users profile
	public void DelProf(String user){
		Session session = cluster.connect("HamishDB");
		PreparedStatement statement = session.prepare("delete from Subscribe_to where user = ?;");
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement.bind(user));
		PreparedStatement statement1 = session.prepare("delete from Users where user = ?;");
		BoundStatement boundStatement1 = new BoundStatement(statement1);
		session.execute(boundStatement1.bind(user));
		PreparedStatement statement2 = session.prepare("delete from tweets where user = ?;");
		BoundStatement boundStatement2 = new BoundStatement(statement2);
		session.execute(boundStatement2.bind(user));
		session.close();
	}
	
	//Method to update details
	public void updated(String username, String Email, String Password, String previous){
		Session session = cluster.connect("HamishDB");
		PreparedStatement statement = session.prepare("delete from Users where user = ? and password = ?;");
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement.bind(username, previous));
		statement = session.prepare("insert into Users (user, password, email) values (?,?,?);");
		boundStatement = new BoundStatement(statement);
		session.execute(boundStatement.bind(username, Password, Email));
		session.close();
	}
	
	}
