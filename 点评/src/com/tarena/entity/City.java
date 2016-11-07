package com.tarena.entity;

import java.util.ArrayList;

public class City {
	private String status;
	private ArrayList<String> cities;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<String> getCities() {
		return cities;
	}

	public void setCities(ArrayList<String> cities) {
		this.cities = cities;
	}

	public City(String status, ArrayList<String> cities) {
		super();
		this.status = status;
		this.cities = cities;
	}

	public City() {
		super();
	}

	@Override
	public String toString() {
		return "City [status=" + status + ", cities=" + cities + "]";
	}

}
