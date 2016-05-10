package com.lyp.myweatherdemo.bean;

public class Weather {

	private String currentCity 	= null;
	private String currentTemp 	= null;
	private String pm25			= null;
	private String date[] 		= new String[4];
	private String date_weather[]= new String[4];
	private String date_wind[] 	= new String[4];
	private String date_temp[] 	= new String[4];
	
	public Weather() {
		currentCity 	= "null";
		currentTemp 	= "null";
		pm25			= "null";
		date 			= new String[4];
		date_weather	= new String[4];
		date_wind 		= new String[4];
		date_temp 		= new String[4];
	}
	
	public String getCurrentCity() {
		return currentCity;
	}
	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}
	public String getCurrentTemp() {
		return currentTemp;
	}
	public void setCurrentTemp(String currentTemp) {
		this.currentTemp = currentTemp;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public String[] getDate() {
		return date;
	}
	public void setDate(String[] date) {
		this.date = date;
	}
	public String[] getDate_weather() {
		return date_weather;
	}
	public void setDate_weather(String[] date_weather) {
		this.date_weather = date_weather;
	}
	public String[] getDate_wind() {
		return date_wind;
	}
	public void setDate_wind(String[] date_wind) {
		this.date_wind = date_wind;
	}
	public String[] getDate_temp() {
		return date_temp;
	}
	public void setDate_temp(String[] date_temp) {
		this.date_temp = date_temp;
	}
	
}
