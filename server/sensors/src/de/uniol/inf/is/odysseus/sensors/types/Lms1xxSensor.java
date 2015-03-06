package de.uniol.inf.is.odysseus.sensors.types;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.lms1xx.physicaloperator.access.LMS1xxProtocolHandler;

public class Lms1xxSensor extends SensorModel
{
	public Lms1xxSensor(SensorModel sensorInfo) 
	{
		super(sensorInfo);
	}
	
	public String getSourceQueryText2()
	{
		String formatStr =	"%(sourceName) := ACCESS({source='%(sourceName)',\n"
							+ "               wrapper='GenericPush',\n"
							+ "               transport='TCPClient',\n"
							+ "               protocol='LMS1xx',\n"
							+ "               dataHandler='KeyValueObject',\n"
							+ "               options=[%(options)]})\n";

		// Prepare host name and port
		String hostName = getEthernetAddr();
		String portNum = "2111";
				
		String[] lines = hostName.split(":");
		if (lines.length == 2)
		{
			hostName = lines[0];
			portNum = lines[1];
		}			

		// Prepare options
		Map<String, String> options = new HashMap<>();
		options.put("host", hostName);
		options.put("port", portNum);
		
		options.put(LMS1xxProtocolHandler.INIT_RAWDATA, "true");
		
		Map<String, Object> formatMap = new HashMap<>();
		formatMap.put("sourceName", getSourceName());
		formatMap.put("options", MapToString(options));		
		
		return namedFormatStr(formatStr, formatMap);
	}	
	
	@Override public String getSourceQueryText()
	{
		// Prepare host name and port
		String hostName = getEthernetAddr();
		String portNum = "2111";
				
		String[] lines = hostName.split(":");
		if (lines.length == 2)
		{
			hostName = lines[0];
			portNum = lines[1];
		}			

		// Prepare options
		Map<String, String> options = new HashMap<>();
		options.put("host", hostName);
		options.put("port", portNum);
		
		options.put(LMS1xxProtocolHandler.INIT_RAWDATA, "true");

		// Generate data query
		String dataQuery; 
		dataQuery  = "#PARSER PQL	\n";		
		dataQuery += "#RUNQUERY		\n";			
			
		dataQuery += "LMS = " + getAccessQuery(getSourceName(), "GenericPush", "tcpclient", "LMS1xx", "KeyValueObject", options, null);
		dataQuery += getSourceName() + " := LMS\n";

		return dataQuery;
	}	
	
	@Override public String getLiveViewURL(String destinationIP, int destinationPort)
	{
		// TODO: Use "LMS URL type" here? Or do not use URL in method name...
		return Integer.toString(destinationPort);
	}	
	
		
	@Override public String getLiveViewQueryText(String userName, String destinationIP, int destinationPort)
	{		
		// Prepare options
		Map<String, String> options = new HashMap<>();
		options.put("port",  Integer.toString(destinationPort));
		options.put("host",  destinationIP);
		options.put("write", "20480");
		
		// Generate live view query
		String sinkName = getLiveViewQueryName(userName); 		
		String liveQuery  = "#PARSER PQL	\n";		
		liveQuery		 += "#RUNQUERY		\n";		
		liveQuery		 += "raw = KEYVALUETOTUPLE({schema=[['RAWDATA','String']], type='Tuple', keepinput='false'}, " + getSourceName() + ")\n";		
		liveQuery		 += getSenderQuery(sinkName, sinkName, "GenericPush", "UDPClient", "SimpleCSV", "Tuple", "raw", options);

		return liveQuery;
	}
	
	@Override public String getLoggingQueryText(Map<String, String> options)
	{
		String sinkName = getLoggingQueryName();
		String logQuery; 
		logQuery  = "#PARSER PQL	\n";		
		logQuery += "#RUNQUERY		\n";			
		logQuery += "raw = KEYVALUETOTUPLE({schema=[['RAWDATA','String']], type='Tuple', keepinput='false'}, " + getSourceName() + ")";
		
		logQuery += getSenderQuery(sinkName, sinkName, "GenericPush", "none", "TextFileLogger", "Tuple", "raw", options);
		
		return logQuery;
	}
}
