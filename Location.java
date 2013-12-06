package com.bigdata.impala.dataobjects;

public class Location {
	
	private String name;
	private String latitude;
	private String longitud;
	
	
	public Location(String name, String latitude, String longitud) {
		super();
		this.name = name;
		this.latitude = latitude;
		this.longitud = longitud;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	
}
