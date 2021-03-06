package com.social.beFriendly.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.social.beFriendly.model.Points;
import com.social.beFriendly.model.ProfilePic;
import com.social.beFriendly.model.UploadPic;
import com.social.beFriendly.model.User;
import com.social.beFriendly.model.UserInfo;
import com.social.beFriendly.service.EmailService;
import com.social.beFriendly.service.FriendService;
import com.social.beFriendly.service.NotificationService;
import com.social.beFriendly.service.UserService;
import com.social.scframework.App.Email;
import com.social.scframework.App.Utility;
import com.social.scframework.highchart.HighChart;



/**
 * Servlet implementation class UserActions
 */
public class UserActions extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Utility utility = new Utility();
	ObjectId uid;
	String templatePath = "C:/soft/apache-tomcat-8.5.23/webapps/beFriendly/WEB-INF/templates/fancy-colorlib";
	public static final String SALT = "my-salt-text";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserActions() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserActions useraction = new UserActions();


		String path = request.getPathInfo();
		System.out.println("path "+ path);


		if(path==null||path.equals("/")){
			Map<String, Object> hmap  = new HashMap<String, Object>();
			//hmap = utility.checkSession(request);
			utility.getHbs(response,"home",hmap,templatePath);
		}

		else{
			try {
				String ur = path.replace("/", "");
				Method method = UserActions.class.getDeclaredMethod(ur,HttpServletRequest.class,HttpServletResponse.class);

				method.invoke(useraction,request,response);
			} catch (Exception e) {

				e.printStackTrace();
			} 
		}
	}
	public Map<String, Object> getUserDetails(HttpServletRequest request,HttpServletResponse response){


		Map<String, Object> hmap = new HashMap<String, Object>();
		uid = new ObjectId(utility.getSession(request));
		UserService userservice = new UserService();
		User user = userservice.findOneById(uid.toString());
		if(user.getuType().equals("Admin")){
			hmap.put("admin", true);
		}
		hmap.put("loggedInUser", user);
		hmap.put("uid", uid);
		if(uid!=null)
			hmap.put("login", true);
		NotificationService notificationService = new NotificationService();
		hmap.putAll(notificationService.notificationRead(uid));

		return hmap;
	}
	public void login(HttpServletRequest request,HttpServletResponse response){

		try {
			utility.getHbs(response,"login_page",null,templatePath);
		} catch (ServletException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void register(HttpServletRequest request,HttpServletResponse response){

		try {
			utility.getHbs(response,"register_page",null,templatePath);
		} catch (ServletException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public void adduser(HttpServletRequest request,HttpServletResponse response){

		try {
			String fname = request.getParameter("fname");
			String mname = request.getParameter("mname");
			String lname = request.getParameter("lname");
			String email = request.getParameter("email");
			String country = request.getParameter("country");
			String city = request.getParameter("city");
			String password = request.getParameter("pass");
			String dob = request.getParameter("dob");
			String mobile =request.getParameter("mobile");

			String saltedPassword = SALT + password;
			String hashedPassword = utility.generateHash(saltedPassword);
			
			
			
			String reference = request.getParameter("reference");
			if(reference==null)
				reference = "No reference";
			String referenceId = request.getParameter("referenceId");
			if(referenceId==null)
				referenceId="null";
			String gender = request.getParameter("gender");
			String bgcolor = "#000000";
			String rootPath = System.getProperty("catalina.home");
			String savePath = rootPath + File.separator + "webapps/images/beFriendlyimages";
			File fileSaveDir=new File(savePath);
			//File file = new File(rootPath + File.separator + "images");
			//File fileSaveDir=new File(file);
			if(!fileSaveDir.exists()){
				fileSaveDir.mkdir();
			}
			//String imagePath= fileSaveDir + File.separator + "default.jpg";
			String filePath=null;
			if(reference.equals("fb")){
				filePath = request.getParameter("picUrl");
			}
			else
				filePath = File.separator +"images/beFriendlyimages" + File.separator + "default.jpg";

			UserService rs = new UserService();
			Boolean result = rs.registerUser(fname, lname, mname,country,city,mobile,hashedPassword,gender,dob,bgcolor,filePath,email,reference,referenceId);
			Map<String, Object> hmap  = new HashMap<String, Object>();
			if(result == false){

				String msg = "You are already registered with this email";
				hmap.put("message", msg);
				//utility.getHbs(response,"message",hmap);
				utility.getHbs(response,"register_page",hmap,templatePath);
			}
			else{
				String registeredMsg = "You are successfully registered!!! Login to Continue";
				hmap.put("message", registeredMsg);


				if(!referenceId.equals("null")){
					response.sendRedirect("/beFriendly");
				}
				else
				{					
					utility.getHbs(response,"login_page",hmap,templatePath);
				}

			}
		} catch (ServletException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void signin(HttpServletRequest request,HttpServletResponse response){

		try {
			String email = request.getParameter("email");
			System.out.println("username "+email);
			if(email==null)
				email="";
			String password = request.getParameter("pass");
			System.out.println("password "+password);
			if(password==null)
				password="";
			String reference = request.getParameter("reference");
			if(reference==null)
				reference = "No reference";
			String referenceId = request.getParameter("referenceId");
			if(referenceId==null)
				referenceId="null";
			System.out.println("reference is "+reference);
			System.out.println("ref Id is "+referenceId);
			
			String saltedPassword = SALT + password;
			String hashedPassword = utility.generateHash(saltedPassword);
			
			
			UserService userservice = new UserService();
			String result = userservice.checkValid(email,hashedPassword,reference,referenceId);
			Map<String, Object> hmap  = new HashMap<String, Object>();
			//System.out.println(request.getParameter("firstname"));
			System.out.println(result);
			String msg = null;
			if(result.equals(email)){
				msg = "No such username exists!!!"
						+ " Register or login with another username";

				hmap.put("message", msg);

				utility.getHbs(response,"login_page",hmap,templatePath);

			}
			else if(result.equals(password)){
				msg = "Wrong password entered";
				hmap.put("message", msg);
				utility.getHbs(response,"login_page",hmap,templatePath);
			}

			else if(result.equals("Register First")){
				hmap.put("register", true);
				
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(new Gson().toJson(hmap));


			}
			else {

				Cookie loginCookie = new Cookie("uid",result);

				System.out.println("uid is "+result);
				response.addCookie(loginCookie);
				loginCookie.setMaxAge(30*60); 			
				//UserService userservice = new UserService();
				Boolean loggedIn = userservice.login(result);
				hmap.put("LoggedIn", loggedIn);

				//hmap.put("register", false);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(new Gson().toJson(hmap));
				if(referenceId.equals("null")){

					response.sendRedirect("friendactivity");
				}

			}


		} catch (ServletException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void dashboard(HttpServletRequest request,HttpServletResponse response){

		try {
			Map<String, Object> hmap  = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			UserService userService = new UserService();
			uid = (ObjectId) hmap.get("uid");
			List<Object> friendList = new ArrayList<Object>();
			hmap.putAll(userService.myActivity(uid));
			FriendService friendService = new FriendService();
			friendList = friendService.getFriends(uid, 6);
			hmap.put("friendList", friendList);
			
			utility.getHbs(response,"dashboard",hmap,templatePath);
		} catch (ServletException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public void profile(HttpServletRequest request,HttpServletResponse response){

		try {
			Map<String, Object> hmap  = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));			
			UserService userService = new UserService();
			UserInfo myinfo = userService.getmyInfo(uid);
			hmap.put("myinfo", myinfo);
			response.sendRedirect("profile");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public void logout(HttpServletRequest request,HttpServletResponse response){
		try{
			uid = new ObjectId(utility.getSession(request));
			String reference = request.getParameter("reference");

			Cookie loginCookie=new Cookie("uid","");  
			loginCookie.setMaxAge(0);  
			response.addCookie(loginCookie); 

			UserService userservice = new UserService();
			userservice.logout(uid);

			//System.out.println(request.getContextPath());
			//request.getRequestDispatcher("").forward(request, response);
			if(reference==null)
				response.sendRedirect("login");
			return;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void gallery(HttpServletRequest request,HttpServletResponse response){
		try{
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			UserService userservice = new UserService();
			uid = (ObjectId) hmap.get("uid");

			List<ProfilePic> profilePicList = userservice.getProfilePic(uid);
			hmap.put("profilePicList", profilePicList);

			List<UploadPic> uploadPicList = userservice.getUploadPic(uid);
			hmap.put("uploadPicList", uploadPicList);
			System.out.println("................."+hmap);
			utility.getHbs(response,"picture_gallery",hmap,templatePath);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


	public void search(HttpServletRequest request,HttpServletResponse response){
		try{
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			uid = (ObjectId) hmap.get("uid");
			UserService userservice = new UserService();
			String search = request.getParameter("search");

			hmap.putAll(userservice.searchUser(search,uid));			
			hmap.put("search",search);
			utility.getHbs(response,"search",hmap,templatePath);

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void profilepic(HttpServletRequest request, HttpServletResponse response){
		try {
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			uid = (ObjectId) hmap.get("uid");
			System.out.println("profile pic "+uid);
			String rootPath = System.getProperty("catalina.home");
			String savePath = rootPath + File.separator + "webapps/images/beFriendlyimages/"+uid+"/dp";
			File fileSaveDir=new File(savePath);
			if(!fileSaveDir.exists()){
				fileSaveDir.mkdirs();
			}
			String fileName = request.getParameter("filename");
			System.out.println("File is ................ " + fileName);
			Part file = request.getPart("file");
			System.out.println("File is ................ " + file);

			hmap.put("file", file);
			hmap.put("filename",fileName);
			file.write(fileSaveDir + File.separator + fileName);
			String filePath= File.separator +"images/beFriendlyimages/"+uid+"/dp" + File.separator + fileName;
			UserService userService = new UserService();
			userService.updatePic(filePath,uid);


			//hmap.put("loggedInUser",user);
			System.out.println("filepath  ...  "+filePath);
			//String path = request.getPathInfo();


		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void addPictures(HttpServletRequest request,HttpServletResponse response){
		try{
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			uid = (ObjectId) hmap.get("uid");
			String rootPath = System.getProperty("catalina.home");
			String savePath = rootPath + File.separator + "webapps/images/beFriendlyimages/"+uid+"/uploads";
			File fileSaveDir=new File(savePath);
			if(!fileSaveDir.exists()){
				fileSaveDir.mkdirs();
			}
			String fileName = request.getParameter("filename");
			System.out.println("File is ................ " + fileName);
			Part file = request.getPart("file");
			System.out.println("File is ................ " + file);

			hmap.put("file", file);
			hmap.put("filename",fileName);
			file.write(fileSaveDir + File.separator + fileName);
			String filePath= File.separator +"images/beFriendlyimages/"+uid+"/uploads" + File.separator + fileName;
			UserService userService = new UserService();
			userService.uploadPic(filePath,uid);


			//hmap.put("loggedInUser",user);
			System.out.println("filepath  ...  "+filePath);

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void unsubscribe(HttpServletRequest request,HttpServletResponse response){
		try{
			String email = request.getParameter("email");
			EmailService emailservice = new EmailService();
			emailservice.unsubscribe(email);

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void notifications(HttpServletRequest request,HttpServletResponse response){
		try{
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			uid = (ObjectId) hmap.get("uid");
			NotificationService notifyservice = new NotificationService();
			hmap.putAll(notifyservice.getNotification(uid));

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(new Gson().toJson(hmap));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void allnotifications(HttpServletRequest request,HttpServletResponse response){
		try{
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			uid = (ObjectId) hmap.get("uid");
			NotificationService notifyservice = new NotificationService();
			hmap.putAll(notifyservice.getNotification(uid));
			utility.getHbs(response, "notification", hmap, templatePath);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void addcomment(HttpServletRequest request,HttpServletResponse response){
		try{
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			uid = (ObjectId) hmap.get("uid");
			User user = (User) hmap.get("loggedInUser");
			String comment = request.getParameter("comment");
			ObjectId fid = new ObjectId(request.getParameter("fid"));
			ObjectId activityId =  new ObjectId(request.getParameter("activityId"));
			UserService userService = new UserService();
			//Activity activity = userService.findActivityLink(activityId.toString());
			//String type = activity.getType();
			userService.addComment(comment,uid,activityId);
			NotificationService notiService = new NotificationService();
			if(fid!=uid){
			notiService.sendNotification(fid, user.getImagepath(), user.getName() + " commented on your post","post?activityId="+activityId+"&" , "New comment");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void showcomments(HttpServletRequest request,HttpServletResponse response){
		try{
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			uid = (ObjectId) hmap.get("uid");
			String skipStr = request.getParameter("skip");
			String limitStr = request.getParameter("limit");
			System.out.println("Skip " + skipStr);
			System.out.println("Limit " + limitStr);
			int skip = 0;
			int limit = 5;
			if(skipStr!=null&&!skipStr.equals("")){
				skip = Integer.parseInt(skipStr);
			}
			if(limitStr!=null&&!limitStr.equals("")){
				limit = Integer.parseInt(limitStr);
			}
			ObjectId activityId = new ObjectId(request.getParameter("activityId"));
			UserService userService = new UserService();
			hmap.putAll(userService.showComments(activityId,skip,limit));
			System.out.println(hmap);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(new Gson().toJson(hmap));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void post(HttpServletRequest request,HttpServletResponse response){
		try{
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			uid = (ObjectId) hmap.get("uid");
			
			UserService userservice = new UserService();
			ObjectId activityId = new ObjectId(request.getParameter("activityId"));
			String read = request.getParameter("read");
			if(read==null)
				read = request.getParameter("?read");
			String id = request.getParameter("id");
			NotificationService notificationService = new NotificationService();

			if(read!=null&&read.equals("true")){
				notificationService.markRead(id);
			}
					
			hmap.putAll(userservice.post(activityId,uid));
			
			utility.getHbs(response, "post", hmap, templatePath);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void heartincrease(HttpServletRequest request,HttpServletResponse response){
		try{
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			uid = (ObjectId) hmap.get("uid");
			User user = (User) hmap.get("loggedInUser");
			UserService userservice = new UserService();
			ObjectId activityId = new ObjectId(request.getParameter("activityId"));
			String fidStr = request.getParameter("fid");
			String brokenStr = request.getParameter("broken");
			boolean broken = Boolean.parseBoolean(brokenStr);
			System.out.println("Broken..........." + broken);
			hmap.putAll(userservice.heartIncrease(activityId,uid,broken));
			//System.out.println("fid.......... " + fid);
			if(fidStr!=null&&!fidStr.equals(uid.toString())){
				ObjectId fid = new ObjectId(fidStr);
			NotificationService notiservice = new NotificationService();
			String notification = null;
			if(broken){
				notification = "You have a HeartBreak from " + user.getName();
			}
			else{
				notification = "You have a Heart from " + user.getName();
			}
			
			notiservice.sendNotification(fid, user.getImagepath(), notification, "post?activityId="+activityId+"&", "New Reaction");
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(new Gson().toJson(hmap));
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void addstatus(HttpServletRequest request,HttpServletResponse response){
		try{
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			uid = (ObjectId) hmap.get("uid");
			
			UserService userservice = new UserService();
			String status = request.getParameter("status");
			userservice.addStatus(uid, status);
			response.sendRedirect("friendactivity");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void gethighcharts(HttpServletRequest request, HttpServletResponse response){
		try {
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			List<HighChart> highcharts = new ArrayList<HighChart>();
			uid = (ObjectId) hmap.get("uid");
			String fidStr = request.getParameter("fid");
			
			if(fidStr!=null){
				ObjectId fid = new ObjectId(fidStr);
				uid=fid;
			}
			UserService userservice = new UserService();
			highcharts.add(userservice.requestpiechart(uid));
			highcharts.add(userservice.stackedgraph(uid));

			hmap.put("highcharts",highcharts);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(new Gson().toJson(hmap));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void updateprofile(HttpServletRequest request, HttpServletResponse response){
		try {
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			
			uid = (ObjectId) hmap.get("uid");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String address = request.getParameter("address");
			String mobile = request.getParameter("mobile");
			String aboutme = request.getParameter("aboutme");
			String country = request.getParameter("country");
			String city = request.getParameter("city");
			
			UserService userService = new UserService();
			userService.updateProfile(uid,name,email,address,mobile,aboutme,country,city);
			utility.getHbs(response,"profile",hmap,templatePath);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void earnpoints(HttpServletRequest request, HttpServletResponse response){
		try {
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			
			uid = (ObjectId) hmap.get("uid");
			UserService userService = new UserService();
			List<Points> pointsList = userService.getPoints(uid);
			hmap.put("pointsList", pointsList);
			utility.getHbs(response,"points",hmap,templatePath);

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void invite(HttpServletRequest request, HttpServletResponse response){
		try {
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			
			uid = (ObjectId) hmap.get("uid");
			String inviteEmail = request.getParameter("inviteEmail");
			System.out.println(inviteEmail);
			UserService userservice = new UserService();
			String from = userservice.invite(uid,inviteEmail);
			if(from==null){
				String msg = "User with this email is already our member!!! Try another";
				System.out.println(msg);
				hmap.put("mailMessage", msg);
				utility.getHbs(response,"points",hmap,templatePath);
				//response.sendRedirect("points");
				System.out.println("Message is .... "+hmap.get("mailMessage"));
			}
			else if (from.equalsIgnoreCase("Already Invited")){
				String msg = "You already invited this user";
				hmap.put("mailMessage", msg);
				utility.getHbs(response,"points",hmap,templatePath);
			}
			else{
				Email email = new Email();
				EmailService emailservice = new EmailService();
				Boolean subscription = emailservice.checkSubscription(inviteEmail);
				String subject = "Get more connected with your friends..";
				String purpose = "inviteToJoin";
				String status = "Pending...";
				System.out.println("Subscription for "+ inviteEmail +"...."+subscription);

				String id = emailservice.email(from, purpose, inviteEmail, status, subject);
				if(subscription==true){

					email.send(inviteEmail, inviteEmail, subject, "invitationTemplate" , id, hmap);
					status = "Sent";
				}
				else{
					status="Failed";
				}
				emailservice.updateEmail(id,status);
				String msg = "Mail Sent...";
				hmap.put("mailMessage", msg);
				utility.getHbs(response,"points",hmap,templatePath);
				//response.sendRedirect("points");
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void settings(HttpServletRequest request, HttpServletResponse response){
		try {
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			
			uid = (ObjectId) hmap.get("uid");
			
			utility.getHbs(response,"settings",hmap,templatePath);

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void changepassword(HttpServletRequest request, HttpServletResponse response){
		try {
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			
			uid = (ObjectId) hmap.get("uid");
			String currentPass = request.getParameter("currentPass");
			String newPass = request.getParameter("newPass");
			String confirmPass = request.getParameter("confirmPass");
			
			String result = null;
			if(!newPass.equals(confirmPass)){
				result = "Not Matched";
			}			
			else{
				String saltedPassword = SALT + currentPass;
				String hashedCurrentPassword = utility.generateHash(saltedPassword);
				
				String saltedPassword1 = SALT + newPass;
				String hashedNewPassword = utility.generateHash(saltedPassword1);
				
			UserService userService = new UserService();
			result = userService.changePassword(hashedCurrentPassword,hashedNewPassword,uid);
			hmap.put("result", result);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(new Gson().toJson(hmap));
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void googlemap(HttpServletRequest request, HttpServletResponse response){
		try {
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.putAll(getUserDetails(request, response));
			
			uid = (ObjectId) hmap.get("uid");

			FriendService friendService = new FriendService();
			List<Object> friendList = friendService.getFriends(uid,30);
			hmap.put("friendList", friendList);
			UserService userService = new UserService();
			UserInfo myInfo  = userService.getmyInfo(uid);
			hmap.put("myInfo", myInfo);
			utility.getHbs(response,"google_map",hmap,templatePath);

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}


