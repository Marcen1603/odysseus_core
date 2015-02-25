package de.uniol.inf.is.odysseus.sensors;

import java.util.HashMap;
import java.util.Map;

public class IntegratedCameraSensor extends VideoSensor 
{
	public IntegratedCameraSensor(SensorModel sensorInfo) 
	{
		super(sensorInfo);
	}
	
	@Override public String getSourceQueryText()
	{
		Map<String, String> options = new HashMap<>();

		String dataQuery; 
		dataQuery  = "#PARSER PQL	\n";		
		dataQuery += "#RUNQUERY		\n";			
			
		dataQuery += getSourceName() + " := " + getAccessQuery(getSourceName(), "GenericPull", "IntegratedCamera", "none", "Tuple", options, null);
			
		return dataQuery;
	}
}
