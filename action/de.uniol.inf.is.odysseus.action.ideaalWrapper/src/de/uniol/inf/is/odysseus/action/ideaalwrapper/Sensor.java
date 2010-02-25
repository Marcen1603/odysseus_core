package de.uniol.inf.is.odysseus.action.ideaalwrapper;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

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
	private static Map<Sensor, SDFAttributeList> schema;
	
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
	
	public static SDFAttributeList getSchema(Sensor sensor) {
		if (schema == null){
			schema = new HashMap<Sensor, SDFAttributeList>();
			
			//BedBalance
			SDFAttributeList schema = new SDFAttributeList();
			String[] identifiers = {"timestamp", "weight"};
			SDFDatatype[] types = {SDFDatatypeFactory.getDatatype("Long"), SDFDatatypeFactory.getDatatype("Double")};
			
			for (int i=0; i<identifiers.length; i++){
				SDFAttribute attribute = new SDFAttribute(identifiers[i]);
				attribute.setDatatype(types[i]);
				schema.add(attribute);
			}
			Sensor.schema.put(Sensor.BedBalance, schema);
			

		}
		return schema.get(sensor);
	}
}
