/**
 * 
 */
package de.uniol.inf.is.soop.sensorregistry.sensors;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jbrode
 * 
 */
public class SensorManager {

	private static SensorManager manager = null;

	private HashMap<String, ISensor> sensors = new HashMap<String, ISensor>();
	
	public static SensorManager getInstance() {
		if (manager == null) {
			manager = new SensorManager();
			
		}
		return manager;
	}

	private SensorManager() {
	}


	public ISensor getSensorByName(String name) throws Exception {
		ISensor u = null;
		
		if (sensors.containsKey(name)) {
			u = sensors.get(name);
		}
		
		if ( !(u instanceof ISensor) ) {
			throw new Exception("Unknown Sensor: " + name);
		}
		
		return u;
	}

	public void addSensor(ISensor s) {
		sensors.put(s.getName(), s);
	}

}
