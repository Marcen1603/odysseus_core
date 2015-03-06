package de.uniol.inf.is.odysseus.sensors.types;

import java.util.HashMap;
import java.util.Map;

public class GPSSensor extends SensorModel
{
	public GPSSensor(SensorModel sensorInfo) 
	{
		super(sensorInfo);
	}
	
	public String getSourceQueryText2()
	{
		String formatStr =	
			"gps = ACCESS({source='%(sourceName)',\n"
		  + "              wrapper='GenericPush',\n"
		  + "              transport='RS232',\n"
		  + "              protocol='NMEA',\n"
		  + "              dataHandler='KeyValueObject',\n"
		  + "              options=[%(options)]})\n"
		  
		  + "GLL = SELECT({predicate=\"sentenceId='GLL'\"}, gps)	\n" 
		  + "RMC = SELECT({predicate=\"sentenceId='RMC'\"}, gps)	\n"
		
		  + "Pos = KEYVALUETOTUPLE({schema=[['longitude', 'Double'], ['latitude', 'Double'], ['longitudeHem', 'String'], ['latitudeHem', 'String'], \n"
		  + "                               ['time.hours', 'Integer'], ['time.minutes', 'Integer'], ['time.seconds', 'Integer'], ['time.miliseconds', 'Integer']],	\n"
		  + "                      type='Tuple', keepinput='false'}, GLL)	\n"

		  + "Signal = KEYVALUETOTUPLE({schema=[['signalIntegrity', 'String']], type='Tuple', keepinput='false'}, RMC)	\n"
		      
		  + "Input = JOIN({card='ONE_ONE', assureOrder=false}, Pos, Signal)	\n"      
		  + "out = MAP({expressions=[['streamtime()', 'timestamp'], 'signalIntegrity', 'longitude', 'latitude', 'longitudeHem', 'latitudeHem', 	\n"
		  + "                        ['ToString(time.hours) + \":\" + ToString(time.minutes) + \":\" + ToString(time.seconds)', 'time']]}, Input)	\n"           						
		  + "%(sourceName) := out\n";
		
		Map<String, String> options = new HashMap<>();
		options.put("port", getEthernetAddr());
		options.put("baud", "9600");
		
		Map<String, Object> formatMap = new HashMap<>();
		formatMap.put("sourceName", getSourceName());
		formatMap.put("options", MapToString(options));		
		
		return namedFormatStr(formatStr, formatMap);
	}	
	
	@Override public String getSourceQueryText()
	{
		// Prepare options
		Map<String, String> options = new HashMap<>();
		options.put("port", getEthernetAddr());
		options.put("baud", "9600");

		// Generate data query
		String dataQuery; 
		dataQuery  = "#PARSER PQL	\n";		
		dataQuery += "#RUNQUERY		\n";			
			
		dataQuery += "gps = " + getAccessQuery(getSourceName(), "GenericPush", "RS232", "NMEA", "KeyValueObject", options, null);
		dataQuery += "GLL = SELECT({predicate=\"sentenceId='GLL'\"}, gps)	\n"; 
		dataQuery += "RMC = SELECT({predicate=\"sentenceId='RMC'\"}, gps)	\n";
		
		dataQuery += "Pos = KEYVALUETOTUPLE({schema=[['longitude', 'Double'], ['latitude', 'Double'], ['longitudeHem', 'String'], ['latitudeHem', 'String'], \n";
		dataQuery += "								 ['time.hours', 'Integer'], ['time.minutes', 'Integer'], ['time.seconds', 'Integer'], ['time.miliseconds', 'Integer']],	\n";
		dataQuery += "						 type='Tuple', keepinput='false'}, GLL)	\n";

		dataQuery += "Signal = KEYVALUETOTUPLE({schema=[['signalIntegrity', 'String']], type='Tuple', keepinput='false'}, RMC)	\n";
		      
		dataQuery += "Input = JOIN({card='ONE_ONE', assureOrder=false}, Pos, Signal)	\n";      
		dataQuery += "out = MAP({expressions=[['streamtime()', 'timestamp'], 'signalIntegrity', 'longitude', 'latitude', 'longitudeHem', 'latitudeHem', 	\n";
		dataQuery += "						  ['ToString(time.hours) + \":\" + ToString(time.minutes) + \":\" + ToString(time.seconds)', 'time']]}, Input)	\n";           						
		dataQuery += getSourceName() + " := out\n";

		return dataQuery;
	}	
	
	@Override public String getLiveViewURL(String destinationIP, int destinationPort)
	{
		// TODO: Use "GPS URL type" here? Or do not use URL in method name...
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
		liveQuery		 += getSenderQuery(sinkName, sinkName, "GenericPush", "UDPClient", "SimpleCSV", "Tuple", getSourceName(), options);

		return liveQuery;
	}
	
	@Override public String getLoggingQueryText(Map<String, String> options)
	{
		String sinkName = getLoggingQueryName();
		String logQuery; 
		logQuery  = "#PARSER PQL	\n";		
		logQuery += "#RUNQUERY		\n";			
		
		logQuery += getSenderQuery(sinkName, sinkName, "GenericPush", "none", "TextFileLogger", "Tuple", getSourceName(), options);
		
		return logQuery;
	}
}
