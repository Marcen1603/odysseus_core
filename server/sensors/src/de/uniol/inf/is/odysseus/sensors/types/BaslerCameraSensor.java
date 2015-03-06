package de.uniol.inf.is.odysseus.sensors.types;

import java.util.HashMap;
import java.util.Map;

public class BaslerCameraSensor extends VideoSensor 
{
	public BaslerCameraSensor(SensorModel sensorInfo) 
	{
		super(sensorInfo);
	}

//	@Override 
	public String getSourceQueryText2()
	{
		String formatStr =	"%(sourceName) := ACCESS({source='%(sourceName)',\n"
							+ "               wrapper='GenericPull',\n"
							+ "               transport='BaslerCamera',\n"
							+ "               protocol='none',\n"
							+ "               dataHandler='Tuple',\n"
							+ "               schema=[['Image', 'ImageJCV']],\n"
							+ "               options=[%(options)]})\n";

		Map<String, String> optionsMap = new HashMap<>();
		optionsMap.put("serialNumber", getEthernetAddr());		
		String options = MapToString(optionsMap); 
		
		Map<String, Object> formatMap = new HashMap<>();
		formatMap.put("sourceName", getSourceName());
		formatMap.put("options", options);		
		
		return namedFormatStr(formatStr, formatMap);
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
