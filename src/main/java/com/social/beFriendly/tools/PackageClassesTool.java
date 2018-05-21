package com.social.beFriendly.tools;

import java.util.Set;

import org.reflections.Reflections;


public class PackageClassesTool {
public static void main(String args[]){
	String packageName = "com.social.beFriendly.actions";
	Reflections reflections = new Reflections(packageName);
	Set<Class<? extends Object>> allClasses =  reflections.getSubTypesOf(Object.class);
	for (Class<?> clas : allClasses) {
	       try {
	        Class<?> name =  Class.forName(clas.toString());
	        System.out.println(name);
	       } catch (Exception e) {
	         e.printStackTrace();
	       } 
}
}
}


