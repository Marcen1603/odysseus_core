package de.uniol.inf.is.odysseus.sensors.types;

import java.util.HashMap;
import java.util.Map;

public class OptrisCameraSensor extends VideoSensor 
{
	public OptrisCameraSensor(SensorModel sensorInfo) 
	{
		super(sensorInfo);
	}
	
	public String getSourceQueryText2()
	{
		String formatStr =	"%(sourceName) := ACCESS({source='%(sourceName)',\n"
							+ "               wrapper='GenericPush',\n"
							+ "               transport='OptrisCamera',\n"
							+ "               protocol='none',\n"
							+ "               dataHandler='Tuple',\n"
							+ "               schema=[['Image', 'ImageJCV']],\n"
							+ "               options=[%(options)]})\n";

		Map<String, String> options = new HashMap<>();
		
		Map<String, Object> formatMap = new HashMap<>();
		formatMap.put("sourceName", getSourceName());
		formatMap.put("options", MapToString(options));		
		
		return namedFormatStr(formatStr, formatMap);
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
