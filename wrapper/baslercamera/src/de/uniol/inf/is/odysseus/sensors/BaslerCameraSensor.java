package de.uniol.inf.is.odysseus.sensors;

import java.util.HashMap;
import java.util.Map;

public class BaslerCameraSensor extends VideoSensor 
{
	public BaslerCameraSensor(SensorModel sensorInfo) 
	{
		super(sensorInfo);
	}
	
	@Override public String getSourceQueryText()
	{
		Map<String, String> options = new HashMap<>();
		options.put("serialNumber", getEthernetAddr());

		Map<String, String> schema = new HashMap<>();
		schema.put("Image", "ImageJCV");
		
		String dataQuery; 
		dataQuery  = "#PARSER PQL	\n";		
		dataQuery += "#RUNQUERY		\n";			
			
		dataQuery += "Video = " + getAccessQuery(getSourceName(), "GenericPull", "BaslerCamera", "none", "Tuple", options, schema);
		dataQuery += getSourceName() + " := Video\n";
			
		return dataQuery;
	}
}
