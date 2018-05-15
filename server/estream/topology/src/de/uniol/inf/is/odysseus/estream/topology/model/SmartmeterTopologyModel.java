package de.uniol.inf.is.odysseus.estream.topology.model;

/**
 * Data Model for Estream Topology
 * 
 * @author Marcel Hamacher
 *
 */
public class SmartmeterTopologyModel {

	private String smartmeter;
	private String model;
	private String gateway;
	private String node;
	private String type;
	private String country;
	private String city;
	private String postcode;
	private String coordinate;
	
	/*
	 * Smartmeter ID, Model-Name, Gateway ID, Node-ID, Transformator-ID, Type
	 */
	public SmartmeterTopologyModel(String smartmeter, String model, String gateway, String node, String type,
			String country, String city, String postcode, String coordinates) {
		this.smartmeter = smartmeter;
		this.model = model;
		this.gateway = gateway;
		this.node = node;
		this.type = type;
		this.country = country;
		this.city = city;
		this.postcode = postcode;
		this.coordinate = coordinates;
	}

	public String getSmartmeter() {
		return smartmeter;
	}

	public void setSmartmeter(String smartmeter) {
		this.smartmeter = smartmeter;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getByName(String name) {
		switch (name) {
		case "gateway":
			return gateway;
		case "smartmeter":
			return smartmeter;
		case "node":
			return node;
		case "type":
			return type;
		case "model":
			return model;
		case "country":
			return country;
		case "city":
			return city;
		case "postcode":
			return postcode;
		case "coordinates":
			return coordinate;
		default:
			return null;
		}
	}
}
