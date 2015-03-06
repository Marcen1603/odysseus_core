package de.uniol.inf.is.odysseus.sensors.types;

import java.util.HashMap;
import java.util.Map;

public class IntegratedCameraSensor extends VideoSensor 
{
	public IntegratedCameraSensor(SensorModel sensorInfo) 
	{
		super(sensorInfo);
	}
	
	public String getSourceQueryText2()
	{
		String formatStr =	"%(sourceName) := ACCESS({source='%(sourceName)',\n"
						  + "                         wrapper='GenericPull',\n"
						  + "                         transport='IntegratedCamera',\n"
						  + "                         protocol='none',\n"
						  + "                         dataHandler='Tuple',\n"
						  + "                         schema=[['Image', 'ImageJCV']],\n"
						  + "                         options=[%(options)]})\n";

		Map<String, String> options = new HashMap<>();
		
		Map<String, Object> formatMap = new HashMap<>();
		formatMap.put("sourceName", getSourceName());
		formatMap.put("options", MapToString(options));		
		
		return namedFormatStr(formatStr, formatMap);
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
