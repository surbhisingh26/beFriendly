package com.social.frenz4.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.social.scframework.model.BaseObject;


@JsonIgnoreProperties(ignoreUnknown = true)

public class User extends BaseObject{

	private String uType;
	private String name;
	private String email;	
	private String gender;
	private Date dob;
	private String country;
	private String city;
	private String mobile;
	private String password;
	private String bgcolor;
	private String imagepath;
	private Date lastLoggedInAt;
	private Boolean loggedIn;
	private Date joiningDate;
	private String reference;
	private String referenceId;
	private int points;
	private Boolean reminder;
	private Boolean confirmMail;
	public String getName() {
		return name;
	}
	
	public void setName(String fname,String mname,String lname) {
		this.name = fname+" "+mname + lname;
	}
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getuType() {
		return uType;
	}
	public void setuType(String uType) {
		this.uType = uType;
	}
	public String getBgcolor() {
		return bgcolor;
	}
	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}
	public String getImagepath() {
		return imagepath;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}

	public void setEmail(String email) {
		this.email = email;
		
	}
	public String getEmail() {
		return email;
	}

	public Date getLastLoggedInAt() {
		return lastLoggedInAt;
	}

	public void setLastLoggedInAt(Date lastLoggedInAt) {
		this.lastLoggedInAt = lastLoggedInAt;
	}

	public Boolean getLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(Boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Boolean getReminder() {
		return reminder;
	}

	public void setReminder(Boolean reminder) {
		this.reminder = reminder;
	}

	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	public Boolean getConfirmMail() {
		return confirmMail;
	}

	public void setConfirmMail(Boolean confirmMail) {
		this.confirmMail = confirmMail;
	}
	
}
