package com.social.beFriendly.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

public class Utility {
	String uid;
	public void getHbs(HttpServletResponse response,String file,Map<String, Object> hmap) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		System.out.println("I am here in hbs...");

		TemplateLoader loader = new FileTemplateLoader("C:/soft/apache-tomcat-8.5.23/webapps/beFriendly/WEB-INF/templates/fancy-colorlib",".hbs");
		Handlebars handlebars = new Handlebars(loader);
		Template template = handlebars.compile(file);
		/*if(hmap==null){

			hmap  = new HashMap<String, Object>();}
		if(uid==null){


			String bgcolor = "#000000";
			hmap.put("bgcolor", bgcolor);
			hmap.put("login",true);
		}

		else{
			//UserService userservice = new UserService();
			//User user = userservice.findOneById(uid);
			//if(user.getuType().equalsIgnoreCase("Admin"))
				hmap.put("admin", true);
			hmap.put("login",false);
		}
		System.out.println("I am here in hbs...");*/
		out.print(template.apply(hmap));
	}

	public String getHbsAsString(String file,Map<String, Object> hmap) throws ServletException, IOException {
		System.out.println("I am here in hbs...");

		TemplateLoader loader = new FileTemplateLoader("C:/soft/apache-tomcat-8.5.23/webapps/webProject1/WEB-INF/Templates/EmailTemplates",".hbs");
		Handlebars handlebars = new Handlebars(loader);
		Template template = handlebars.compile(file);
		if(hmap==null){			
			hmap  = new HashMap<String, Object>();
		}

		System.out.println("I am here in hbs...");
		template.apply(hmap);
		return template.apply(hmap);
	}


	/*public Map<String, Object> checkSession(HttpServletRequest request){

		Cookie[] cookies = request.getCookies();
		if(cookies !=null){
			for(Cookie cookie : cookies){
				System.out.println("cookies "+cookie);

				if(cookie.getName().equals("uid")){
					uid = cookie.getValue();

				}}
		} else{
			uid=null;
		}

		System.out.println("cookie "+uid);
		UserService userservice = new UserService();
		User user = userservice.findOneById(uid);
		Map<String, Object> hmap  = new HashMap<String, Object>();
		hmap.put("uid", uid);
		hmap.put("loggedInUser", user);
		return hmap;
	}*/
}
