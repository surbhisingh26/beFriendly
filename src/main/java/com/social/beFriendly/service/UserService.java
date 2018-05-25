package com.social.beFriendly.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.social.beFriendly.DAO.ActivityDAO;
import com.social.beFriendly.DAO.CommentDAO;
import com.social.beFriendly.DAO.HeartDAO;
import com.social.beFriendly.DAO.ProfilePicDAO;
import com.social.beFriendly.DAO.StatusDAO;
import com.social.beFriendly.DAO.UploadPicDAO;
import com.social.beFriendly.DAO.UserDAO;
import com.social.beFriendly.model.Activity;
import com.social.beFriendly.model.Comment;
import com.social.beFriendly.model.Heart;
import com.social.beFriendly.model.ProfilePic;
import com.social.beFriendly.model.Status;
import com.social.beFriendly.model.UploadPic;
import com.social.beFriendly.model.User;


public class UserService {
	UserDAO userdao = new UserDAO();
	JacksonDBCollection<User, String> userCollection =  userdao.userDAO();
	ProfilePicDAO profilepicdao = new ProfilePicDAO();
	JacksonDBCollection<ProfilePic, String> dpCollection =  profilepicdao.profilePicDAO();
	UploadPicDAO uploadpicdao = new UploadPicDAO();
	JacksonDBCollection<UploadPic, String> uploadCollection =  uploadpicdao.uploadPicDAO();
	ActivityDAO activitydao = new ActivityDAO();
	JacksonDBCollection<Activity, String> activityCollection =  activitydao.activityDAO();
	public Boolean registerUser(String fname, String lname, String mname, String country, String city, String mobile,
			String password, String gender, String dob, String bgcolor, String filePath, String email, String reference,
			String referenceId) {

		if(!mname.equals(""))
			mname = mname + " ";
		User registration = new User();
		Date date = null;
		try {	
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			date = format.parse(dob);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//---------- Creating Collection ------------
		BasicDBObject query = new BasicDBObject();

		query.put("email", email);
		DBCursor<User> cursor = userCollection.find(query);

		if (cursor.hasNext()) {
			return false;
		}
		registration.setuType("User");
		registration.setName(fname,mname,lname);
		registration.setEmail(email);
		registration.setGender(gender);
		registration.setCountry(country);
		registration.setCity(city);
		registration.setMobile(mobile);
		registration.setPassword(password);
		registration.setDob(date);
		registration.setBgcolor(bgcolor);
		registration.setLastLoggedInAt(null);
		registration.setImagepath(filePath);
		registration.setLoggedIn(false);
		registration.setReference(reference);
		registration.setReferenceId(referenceId);
		registration.setPoints(50);

		WriteResult<User, String> reg = userCollection.insert(registration);
		registration = reg.getSavedObject();
		//DBCollection collection = mongo.getCollection("invitation");
		/*JacksonDBCollection<Invite, String> coll1 = JacksonDBCollection.wrap(collection,Invite.class, String.class);

		NotificationService notificationservice = new NotificationService();
		String link = "points";
		Date Ndate = new Date();
		BasicDBObject query1 = new BasicDBObject();
		query1.put("recieverEmail", email);
		DBCursor<Invite> cursor1 = coll1.find(query1);

		while(cursor1.hasNext()){
			Invite invite = cursor1.next();
			String userId = invite.getSenderId();

			User user = coll.findOneById(userId);
			EmailService emailservice = new EmailService();
			String mailStatus = emailservice.checkStatus(email, user.getUsername(),"inviteToJoin");
			if(mailStatus.equalsIgnoreCase("Sent")){
				user.setPoints(user.getPoints()+50);
				String userEmail = user.getEmail();
				coll.updateById(userId, user);
				String username = user.getName();
				BasicDBObject query2 = new BasicDBObject();

				query2.put("recieverEmail", userEmail);
				DBCursor<Invite> cursor2 = coll1.find(query2);
				while(cursor2.hasNext()){
					Invite invite1 = cursor2.next();
					String secondaryUserId = invite1.getSenderId();
					User user1 = coll.findOneById(secondaryUserId);
					user1.setPoints(user1.getPoints()+10);
					coll.updateById(secondaryUserId, user1);
					coll1.remove(query2);
					notificationservice.send(secondaryUserId,"Congratulation!!! You have earned 10 points reward on joining of "+registration.getName()+" invited by your friend "+username,link,Ndate);
				}

				System.out.println("REGISTRATION ID....................."+ registration.getId());

				notificationservice.send(userId,"Congratulation!!! You have earned 50 points reward on joining of "+registration.getName(),link,Ndate);
			}
		}

		notificationservice.send(registration.getId(),"Welcome "+fname+" "+lname+"\n Congratulation!!! You have been rewarded by 50 points in your account",link,Ndate);

		 */

		return true;
	}
	public String checkValid(String email, String password, String reference, String referenceId) {

		if(email!=""){
			BasicDBObject query = new BasicDBObject();
			query.put("email", email);
			DBCursor<User> cursor = userCollection.find(query);
			String result = null;
			if (cursor.hasNext()) {
				User user = cursor.next();
				String pass = user.getPassword();
				System.out.println("pass "+user.getPassword());
				if(pass.equals(password)){

					result = user.getId();
					System.out.println("uid is ... "+result);

					return result;
				}				
				else{

					return password;
				}

			}}
		else{
			BasicDBObject query = new BasicDBObject();
			query.put("referenceId", referenceId);
			DBCursor<User> cursor = userCollection.find(query);
			if(cursor.hasNext()){
				User registration = cursor.next();
				String id = registration.getId();
				System.out.println("Id is "+id);
				return id;	
			}
			else{
				return "Register First";
			}
		}

		return email;

	}
	public Boolean login(String uid){

		User user = userCollection.findOneById(uid);
		System.out.println(uid);
		System.out.println(user.getName());
		user.setLoggedIn(true);
		userCollection.updateById(uid, user);
		return user.getLoggedIn();

	}
	public User findOneById(String uid) {

		User user = userCollection.findOneById(uid);
		return user;
	}

	public void logout(ObjectId uid){

		Date date = new Date();		

		//System.out.println("Date is "+now);
		User user = userCollection.findOneById(uid.toString());
		user.setLoggedIn(false);
		user.setLastLoggedInAt(date);
		userCollection.updateById(uid.toString(), user);
	}
	public Map<String,Object> searchUser(String search,ObjectId uid) {
		Map<String,Object> hmap = new HashMap<String, Object>();
		BasicDBObject query = new BasicDBObject();
		query.put("name", new BasicDBObject("$regex" , "(?i).*"+search+".*"));
		List<User> requestedUser = new ArrayList<User>();
		List<User> searchedFriend = new ArrayList<User>();
		List<User> pendingFriend = new ArrayList<User>();
		List<User> searchedUser = new ArrayList<User>();
		DBCursor<User> cursor = userCollection.find(query);
		FriendService friendservice = new FriendService();
		User user = new User();
		while(cursor.hasNext()){
			user = cursor.next();

			String status = friendservice.checkStatus(uid, new ObjectId(user.getId()));
			if(user.getId().equals(uid))
				hmap.put("user", true);
			else if(status.equalsIgnoreCase("My Request Accepted")||status.equals("I Accepted Request"))
				searchedFriend.add(user);
			else if(status.equalsIgnoreCase("Request Sent"))
				requestedUser.add(user);
			else if(status.equalsIgnoreCase("Pending Request"))
				pendingFriend.add(user);
			else
				searchedUser.add(user);
			System.out.println("name " + user.getName());
		}
		query.clear();
		query.put("email", new BasicDBObject("$regex" , "(?i).*"+search+".*"));
		DBCursor<User> EmailCursor = userCollection.find(query);
		while(EmailCursor.hasNext()){
			user = EmailCursor.next();
			Boolean flag = true;
			for(User u:searchedUser){

				if(u.getEmail().equals(user.getEmail())){
					flag = false;
					break;
				}
			}
			for(User u:searchedFriend){

				if(u.getEmail().equals(user.getEmail())){
					flag = false;
					break;
				}
			}
			for(User u:requestedUser){

				if(u.getEmail().equals(user.getEmail())){
					flag = false;
					break;
				}
			}
			for(User u:pendingFriend){

				if(u.getEmail().equals(user.getEmail())){
					flag = false;
					break;
				}
			}
			if(flag==true){
				String status = friendservice.checkStatus(uid,new ObjectId(user.getId()));
				if(user.getId().equals(uid))
					hmap.put("user", true);
				else if(status.equalsIgnoreCase("My Request Accepted")||status.equals("I Accepted Request"))
					searchedFriend.add(user);
				else if(status.equalsIgnoreCase("Request Sent"))
					requestedUser.add(user);
				else if(status.equalsIgnoreCase("Pending Request"))
					pendingFriend.add(user);
				else
					searchedUser.add(user);
			}


			System.out.println("email " + user.getName());
		}
		hmap.put("searchedFriend", searchedFriend);
		hmap.put("requestedUser", requestedUser);
		hmap.put("pendingFriend", pendingFriend);
		hmap.put("searchedUser", searchedUser);
		return hmap;
	}
	public User updatePic(String filePath, ObjectId uid) {
		User user = userCollection.findOneById(uid.toString());
		System.out.println("uid is ..."+uid);
		user.setImagepath(filePath);
		System.out.println("path is ..."+user.getImagepath());
		userCollection.updateById(uid.toString(), user);
		ProfilePic profilepic = new ProfilePic();
		BasicDBObject query = new BasicDBObject();
		query.put("current", true);
		DBCursor<ProfilePic> cursor = dpCollection.find(query);
		
		if(cursor.hasNext()){
			profilepic = cursor.next();
			profilepic.setCurrent(false);			
		}
		dpCollection.updateById(profilepic.getId(), profilepic);
		profilepic = new ProfilePic();
		Date date = new Date();
		profilepic.setUid(uid);
		profilepic.setPath(filePath);
		profilepic.setCurrent(true);
		profilepic.setUploadTime(date);
		
		WriteResult<ProfilePic, String> pic = dpCollection.insert(profilepic);
		profilepic = pic.getSavedObject();
		System.out.println("Profile pic id is ...... " + profilepic.getId());
		Activity activity = new Activity();
		activity.setUid(uid);
		activity.setActivityId(new ObjectId(profilepic.getId()));
		activity.setDate(date);
		activity.setType("profilepic");
		activity.setHeartBreaks(0);
		activity.setHearts(0);
		activity.setComments(0);
		activityCollection.insert(activity);



		return user;
	}
	public List<ProfilePic> getProfilePic(ObjectId uid) {

		List<ProfilePic> profilePicList = new ArrayList<ProfilePic>();
		BasicDBObject query = new BasicDBObject();
		query.put("uid", uid);
		DBCursor<ProfilePic> cursor = dpCollection.find(query);

		while(cursor.hasNext()){
			ProfilePic profilepic = cursor.next();
			profilePicList.add(profilepic);

		}
		return profilePicList;
	}
	public void uploadPic(String filePath, ObjectId uid) {

		Date time = new Date();
		UploadPic upload = new UploadPic();
		upload.setPath(filePath);
		upload.setUid(uid);
		upload.setUploadTime(time);
		
		WriteResult<UploadPic,String> pic = uploadCollection.insert(upload);
		upload = pic.getSavedObject();
		System.out.println("Upload id is ...... " + upload.getId());
		Activity activity = new Activity();
		activity.setUid(uid);
		activity.setActivityId(new ObjectId(upload.getId()));
		activity.setDate(time);
		activity.setType("uploadpic");
		activity.setHeartBreaks(0);
		activity.setHearts(0);
		activity.setComments(0);
		activityCollection.insert(activity);

	}
	public List<UploadPic> getUploadPic(ObjectId uid) {

		List<UploadPic> uploadPicList = new ArrayList<UploadPic>();
		BasicDBObject query = new BasicDBObject();
		BasicDBObject sort = new BasicDBObject();
		query.put("uid", uid);
		sort.put("time", -1);
		DBCursor<UploadPic> cursor = uploadCollection.find(query).sort(sort);

		while(cursor.hasNext()){

			UploadPic uploadpic = cursor.next();
			uploadPicList.add(uploadpic);

		}

		return uploadPicList;
	}

	public Map<String,Object> myActivity(ObjectId uid) {
		Map<String,Object> hmap = new HashMap<String, Object>();
		DBCollection coll = activitydao.activityCollectionDAO();
		List<Object> myActivityList = new ArrayList<Object>();
		List<DBObject> pipeline = new ArrayList<DBObject>();

		DBObject match = new BasicDBObject("$match",
				new BasicDBObject("uid" , uid)

				);
		pipeline.add(match);

		DBObject lookupFields = new BasicDBObject("from", "uploadpic");
		lookupFields.put("localField","activityId");
		lookupFields.put("foreignField","_id");
		lookupFields.put("as", "uploadpic");  
		pipeline.add(new BasicDBObject("$lookup",lookupFields));

		DBObject profilePicFields = new BasicDBObject("from", "profilepic");
		profilePicFields.put("localField","activityId");
		profilePicFields.put("foreignField","_id");
		profilePicFields.put("as", "profilepic");
		pipeline.add(new BasicDBObject("$lookup",profilePicFields));
		
		DBObject statusFields = new BasicDBObject("from", "status");
		statusFields.put("localField","activityId");
		statusFields.put("foreignField","_id");
		statusFields.put("as", "status");
		pipeline.add(new BasicDBObject("$lookup",statusFields));

		DBObject friendFields = new BasicDBObject("from", "friend");
		friendFields.put("localField","activityId");
		friendFields.put("foreignField","_id");
		friendFields.put("as", "friend");
		pipeline.add(new BasicDBObject("$lookup",friendFields));

		DBObject userfields = new BasicDBObject("from", "user");
		userfields.put("localField","friend.fid");
		userfields.put("foreignField","_id");
		userfields.put("as", "userFriend");
		pipeline.add(new BasicDBObject("$lookup",userfields));
		
		DBObject heartFields = new BasicDBObject("from", "heart");
		heartFields.put("localField","activityId");
		heartFields.put("foreignField","activityId");
		heartFields.put("as", "heart");
		pipeline.add(new BasicDBObject("$lookup",heartFields));

		DBObject sort = new BasicDBObject("$sort",
				new BasicDBObject("date",-1));

		pipeline.add(sort);

		System.out.println(pipeline);

		AggregationOutput output = coll.aggregate(pipeline);

		for (DBObject result : output.results()) {
			@SuppressWarnings("unchecked")
			List<Object> res = (List<Object>) result.get("heart");
			if(!res.isEmpty()){
				
				result.put("noAction",true);
				for(Object db:res){
					//System.out.println(db);
					BasicDBObject object = (BasicDBObject) db;
					object.get("fid");
					
				if(object.get("fid").toString().equals(uid.toString())){
					
					result.put("broken",object.get("broken"));
					result.put("noAction",false);
					break;
				}
					
				}									
			}
			else{
				result.put("noAction",true);
				
			}
			
			
			
			myActivityList.add(result);
			System.out.println(result);
		}

		hmap.put("myActivityList",myActivityList);
		return hmap;
	}
	public void addComment(String commentMessage, ObjectId fid, ObjectId activityId) {
		System.out.println("ADD COmment..................");
		Date date = new Date();
		CommentDAO commentdao = new CommentDAO();		
		JacksonDBCollection<Comment, String> commentCollection = commentdao.commentDAO();
		Comment comment = new Comment();
		comment.setActivityId(activityId);
		comment.setComment(commentMessage);
		comment.setFid(fid);
		comment.setTime(date);
		commentCollection.insert(comment);
		
		BasicDBObject query = new BasicDBObject();
		query.put("activityId", activityId);
		DBCursor<Activity> cursor = activityCollection.find(query);
		if(cursor.hasNext()){
			Activity activity = cursor.next();
			activity.setComments(activity.getComments()+1);
			activityCollection.updateById(activity.getId(), activity);
		}
		

	}
	public Map<String,Object> showComments(ObjectId activityId,int skip,int limit) {
		Map<String, Object> hmap = new HashMap<String, Object>();
		DBCollection coll = activitydao.activityCollectionDAO();
		CommentDAO commentdao = new CommentDAO();
		JacksonDBCollection<Comment, String> commentCollection = commentdao.commentDAO();
		List<DBObject> pipeline = new ArrayList<DBObject>();
		List<Object> commentList = new ArrayList<Object>();
		DBObject match = new BasicDBObject("$match",
				new BasicDBObject("activityId" , activityId)

				);
		pipeline.add(match);

		DBObject lookupFields = new BasicDBObject("from", "comment");
		lookupFields.put("localField","activityId");
		lookupFields.put("foreignField","activityId");
		lookupFields.put("as", "comments");  
		pipeline.add(new BasicDBObject("$lookup",lookupFields));
		DBObject unwindActivity = new BasicDBObject("$unwind","$comments");
		pipeline.add(unwindActivity);
		DBObject friendsFields = new BasicDBObject("from", "user");
		friendsFields.put("localField","comments.fid");
		friendsFields.put("foreignField","_id");
		friendsFields.put("as", "friend"); 

		pipeline.add(new BasicDBObject("$lookup",friendsFields));
		DBObject unwindFriend = new BasicDBObject("$unwind","$friend");
		pipeline.add(unwindFriend);
		DBObject sort = new BasicDBObject("$sort",
				new BasicDBObject("comments.time",-1));
		pipeline.add(sort);

		DBObject skipTo = new BasicDBObject("$skip",skip);
		pipeline.add(skipTo);
		DBObject limitCount = new BasicDBObject("$limit",limit);
		pipeline.add(limitCount);

		// pipeline.add(commentCount);

		System.out.println(pipeline);

		AggregationOutput output = coll.aggregate(pipeline);

		for (DBObject result : output.results()) {
			commentList.add(result);
			System.out.println(result);

		}
		BasicDBObject query = new BasicDBObject();
		query.put("activityId", activityId);
		long commentCount =  commentCollection.count(query);

		hmap.put("commentList", commentList);
		hmap.put("count", commentCount);
		hmap.put("skip", skip+limit);
		hmap.put("limit", 10);
		if(skip+limit<commentCount){
			hmap.put("showMore", true);
		}
		else
		{
			hmap.put("showMore", false);
		}
		return hmap;
	}

	public Activity findActivityLink(String id) {
		ActivityDAO activitydao = new ActivityDAO();
		JacksonDBCollection<Activity, String> activityCollection = activitydao.activityDAO();
		Activity activity = activityCollection.findOneById(id);

		return activity;
	}
	public Map<String, Object> post(ObjectId activityId, ObjectId uid) {
		Map<String, Object> hmap = new HashMap<String, Object>();
		ActivityDAO activitydao = new ActivityDAO();
		DBCollection activityCollection = activitydao.activityCollectionDAO();
		List<DBObject> pipeline = new ArrayList<DBObject>();

		DBObject match = new BasicDBObject("$match",
				new BasicDBObject("activityId" , activityId)

				);
		pipeline.add(match);
		DBObject lookupFields = new BasicDBObject("from", "profilepic");
		lookupFields.put("localField","activityId");
		lookupFields.put("foreignField","_id");
		lookupFields.put("as", "profilepic");  
		pipeline.add(new BasicDBObject("$lookup",lookupFields));
		DBObject uploadFields = new BasicDBObject("from", "uploadpic");
		uploadFields.put("localField","activityId");
		uploadFields.put("foreignField","_id");
		uploadFields.put("as", "uploadpic");  
		pipeline.add(new BasicDBObject("$lookup",uploadFields));
		DBObject heartFields = new BasicDBObject("from", "heart");
		heartFields.put("localField","activityId");
		heartFields.put("foreignField","activityId");
		heartFields.put("as", "heart");
		pipeline.add(new BasicDBObject("$lookup",heartFields));

		AggregationOutput output = activityCollection.aggregate(pipeline);
		
		for (DBObject result : output.results()) {
			//System.out.println(result.get("heart"));
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) result.get("heart");
			List<Object> res = list;
			if(!res.isEmpty()){
				
				result.put("noAction",true);
				for(Object db:res){
					//System.out.println(db);
					BasicDBObject object = (BasicDBObject) db;
					object.get("fid");
					
				if(object.get("fid").toString().equals(uid.toString())){
					
					result.put("broken",object.get("broken"));
					result.put("noAction",false);
					break;
				}
					
				}									
			}
			else{
				result.put("noAction",true);
				
			}
			
		}
		
		
		hmap.put("activity",output.results());

		return hmap;
	}
	public Map<String, Object> heartIncrease(ObjectId activityId, ObjectId uid, boolean broken) {
		Map<String, Object> hmap = new HashMap<String, Object>();
		HeartDAO heartdao = new HeartDAO();
		JacksonDBCollection<Heart, String> heartCollection = heartdao.heartDAO();
		BasicDBObject activityQuery = new BasicDBObject();
		activityQuery.put("activityId", activityId);
		DBCursor<Activity> activityCursor = activityCollection.find(activityQuery);
		
		Heart heart = new Heart();
		BasicDBObject query = new BasicDBObject();
		query.put("activityId", activityId);
		query.put("broken", broken);
		query.put("fid", uid);
		DBCursor<Heart> cursor = heartCollection.find(query);
		if(cursor.hasNext()){
			heart = cursor.next();
			heartCollection.remove(query);
			hmap.put("remove", true);
		}
		else{
		query.replace("broken",!broken);
		System.out.println("Broken revert is " + !broken);
		DBCursor<Heart> cursor1 = heartCollection.find(query);
		if(cursor1.hasNext()){
			heart = cursor1.next();
			heart.setBroken(broken);
			heartCollection.updateById(heart.getId(),heart);
			hmap.put("revert", true);
			hmap.put("remove", false);
		}
		else{
			heart.setActivityId(activityId);
			heart.setFid(uid);
			heart.setBroken(broken);
			heartCollection.insert(heart);
			hmap.put("revert", false);
			hmap.put("remove", false);
		}
		}
		query.remove("fid");
		query.replace("broken", false);
		long countHeart = heartCollection.count(query);
		query.replace("broken", true);
		long countBroken = heartCollection.count(query);
		hmap.put("heartCount", countHeart);
		hmap.put("brokenCount", countBroken);
		if(activityCursor.hasNext()){
			Activity activity = activityCursor.next();			
			activity.setHearts(countHeart);			
			activity.setHeartBreaks(countBroken);
			
			activityCollection.updateById(activity.getId(), activity);
		}
		return hmap;
	}
	public void addStatus(ObjectId uid, String statusText) {
		StatusDAO statusdao = new StatusDAO();
		JacksonDBCollection<Status, String> statusCollection = statusdao.statusDAO();
		Date date = new Date();
		Status status = new Status();
		status.setDate(date);
		status.setStatusText(statusText);
		status.setUid(uid);
		WriteResult<Status, String> stat = statusCollection.insert(status);
		status = stat.getSavedObject();
		Activity activity = new Activity();
		activity.setActivityId(new ObjectId(status.getId()));
		activity.setComments(0);
		activity.setDate(date);
		activity.setHeartBreaks(0);
		activity.setHearts(0);
		activity.setType("status");
		activity.setUid(uid);
		activityCollection.insert(activity);
		
	}	
}


