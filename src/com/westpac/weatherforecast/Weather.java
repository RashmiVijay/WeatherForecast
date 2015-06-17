package com.westpac.weatherforecast;

import android.content.Context;

public class Weather {

	private String title;
	private String description;
	private  int temperature;
	
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	private String image;
	//Context mContext;
	public Weather() {
		// TODO Auto-generated constructor stub
		
		}	
//getter and setter functions for getting title, description and image from the JSON object 
	public Weather(String name, String description,String image) {
		super();
		this.title = name;
		this.description = description;
		this.image = image;
	}


	public String getName() {
		return title;
	}

	public void setName(String name) {
		this.title = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
