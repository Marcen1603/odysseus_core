package de.uniol.inf.is.odysseus.sensors;

import java.util.HashMap;
import java.util.Map;

public class OptrisCameraSensor extends VideoSensor 
{
	public OptrisCameraSensor(SensorModel sensorInfo) 
	{
		super(sensorInfo);
	}
	
	@Override public String getSourceQueryText()
	{
		Map<String, String> schema = new HashMap<>();
		schema.put("Image", "ImageJCV");
		
		String dataQuery; 
		dataQuery  = "#PARSER PQL	\n";		
		dataQuery += "#RUNQUERY		\n";			
			
		dataQuery += "Video = " + getAccessQuery(getSourceName(), "GenericPush", "OptrisCamera", "none", "Tuple", null, schema);
		dataQuery += getSourceName() + " := Video\n";
			
		return dataQuery;
	}
}
