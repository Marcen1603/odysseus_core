package de.uniol.inf.is.odysseus.action.ideaalwrapper;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Enum containing all sensors available at IDEAAL-room
 * @author Simon Flandergan
 *
 */
public enum Sensor {
	BedBalance("192.168.0.56", 99, 1000, "b");
	
	private String ip;
	private int port;
	private long interval;
	private String message;
	private SDFAttributeList schema;
	
	Sensor(String ip, int port, long interval, String message){
		this.ip = ip;
		this.port = port;
		this.interval = interval;
		this.message = message;
	}
	
	public String getIp() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}

	/**
	 * Returns fetch interval in ms
	 * @return
	 */
	public long getInterval() {
		return interval;
	}

	/**
	 * Returns message which should be send to sensor for
	 * data retrieval
	 * @return
	 */
	public String getMessage() {
		return this.message;
	}
	
	public SDFAttributeList getSchema() {
		return schema;
	}
}
