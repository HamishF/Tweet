package com.example.hamish.stores;

import java.util.UUID;

//Get and Set methods use by the model

public class TweetStore {
    String Tweet;
    String User;
    UUID id;
    
    public String getTweet(){
   	 return Tweet;
    }
    public String getUser(){
   	 return User;
    }
    public UUID getId(){
     return id;
    }
    
    public void setTweet(String Tweet){
   	 this.Tweet=Tweet;
    }
    public void setUser(String User){
   	 this.User=User;
    }
    public void setId(UUID id){
     this.id=id;	
    }
    
}
