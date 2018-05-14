package com.social.beFriendly.actions;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;



public class Filters implements Filter{


	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("Filter init called");
		System.out.println("Task scheduled");
		//Reminder reminder = new Reminder(5);
		//	int n=2;
		/* for(int i=0;i<n;i++){
   	 new Reminder(5*i);

   	 }*/

	}

	public void destroy() {
		System.out.println("Filter destroy called");

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getRequestURI().substring(req.getContextPath().length());
		System.out.println("filtering path: " + path);

		if(!path.contains(".")) {
			if(path.contains("friend")||path.contains("Friend")){

				request.getRequestDispatcher("/friendpaths" + path).forward(request, response);
				System.out.println("friend");
			}

			/*if(path.contains("passenger")||path.contains("Passenger"))	{		
				request.getRequestDispatcher("/passengerpaths" + path).forward(request, response);
				System.out.println("passenger");
			}

			else if(path.contains("friend")||path.contains("Friend")){

				request.getRequestDispatcher("/friendpaths" + path).forward(request, response);
				System.out.println("friend");
			}
			else if(path.contains("email")||path.contains("Email")){

				request.getRequestDispatcher("/emailpaths" + path).forward(request, response);
				System.out.println("email");
			}

			else{*/
			else{
				request.getRequestDispatcher("/userpaths" + path).forward(request, response);
				System.out.println("user");
			}
			//	}


		} 
		else {
			chain.doFilter(request, response);
		}
	}
}