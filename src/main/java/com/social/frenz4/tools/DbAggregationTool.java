package com.social.frenz4.tools;

import org.bson.types.ObjectId;

import com.social.frenz4.service.FriendService;
import com.social.frenz4.service.UserService;

public class DbAggregationTool {
public static void main(String args[]){
	String uid = "5b36721b759a7510108e7850";
	FriendService friendService = new FriendService();
	UserService userService = new UserService();
	userService.myActivity(new ObjectId(uid));
//	friendService.friendsActivity(new ObjectId(uid));
	//friendService.heartFriend(new ObjectId(uid));
	//userService.showComments(new ObjectId(uid),0,5);
	//friendService.heartFriend(new ObjectId(uid),0,5, false);
	//FriendDAO dao = new FriendDAO();
	//dao.doubleAggregation("user", "userinfo", 10, new ObjectId(uid), "uid");
}
}