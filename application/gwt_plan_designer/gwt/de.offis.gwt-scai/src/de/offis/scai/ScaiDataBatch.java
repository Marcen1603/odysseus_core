package de.offis.scai;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.offis.client.DataStreamType;
import de.offis.client.OperatorType;
import de.offis.client.Sensor;
import de.offis.client.SensorType;

/**
 * Data model for batched Scai data.
 * 
 * @author Alexander Funk
 *
 */
public class ScaiDataBatch implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1862834980879819142L;
	
	private String[] sensorDomainsString;
	private String[] sensorTypesString;
	private Map<String, String[]> dataElementsForSensorType;
	private List<Sensor> sensors;
	private List<DataStreamType> dataStreamTypes;
	private List<SensorType> sensorTypes;
	private List<OperatorType> operatorTypes;
	
	protected ScaiDataBatch(){
		
	}
	
	public ScaiDataBatch(	String[] sensorDomainsString, 
							String[] sensorTypesString,
							Map<String, String[]> dataElementsForSensorType,
							List<Sensor> sensors,
							List<DataStreamType> dataStreamTypes,
							List<SensorType> sensorTypes,
							List<OperatorType> operatorTypes) {
		this.sensorDomainsString = sensorDomainsString;
		this.sensorTypesString = sensorTypesString;
		this.dataElementsForSensorType = dataElementsForSensorType;
		this.sensors = sensors;
		this.dataStreamTypes = dataStreamTypes;
		this.sensorTypes = sensorTypes;
		this.operatorTypes = operatorTypes;
	}
	
	public String[] getDataElementsForSensorType(String sensorType) {
		if(dataElementsForSensorType.containsKey(sensorType)){
			return dataElementsForSensorType.get(sensorType);
		} else {
			return new String[0];
		}		
	}
	
	public List<String> getDataElementsFromSensorTypeKeys(){		
		return new ArrayList<String>(dataElementsForSensorType.keySet());
	}
	
	public String[] getSensorTypesString() {
//		String[] senTypes = new String[sensorTypes.size()];
//		for(SensorType e : sensorTypes){
//			senTypes[sen] se.get
//		}
		return sensorTypesString; // TODO
	}
	
	public String[] getSensorDomainsString() {
		return sensorDomainsString;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public List<DataStreamType> getDataStreamTypes() {
		return dataStreamTypes;
	}

	public List<SensorType> getSensorTypes() {
		return sensorTypes;
	}

	public List<OperatorType> getOperatorTypes() {
		return operatorTypes;
	}
}
