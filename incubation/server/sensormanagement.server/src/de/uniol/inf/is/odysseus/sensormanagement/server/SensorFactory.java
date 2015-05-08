package de.uniol.inf.is.odysseus.sensormanagement.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel2;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorType;

public class SensorFactory 
{
	static private SensorType getLMS1xx()
	{
		String dataQuery =
				  "#PARSER PQL\n"		
				+ "#RUNQUERY\n"				
				+ "%(sourceName) := ACCESS({source='%(sourceName)',\n"
		    	+ "               wrapper='GenericPush',\n"
				+ "               transport='TCPClient',\n"
				+ "               protocol='LMS1xx',\n"
				+ "               dataHandler='KeyValueObject',\n"
				+ "               options=[%(optionsEx) ['rawData','true']]})\n";

		String logQuery = 
				  "#PARSER PQL\n"		
				+ "#RUNQUERY\n"			
				+ "raw = KEYVALUETOTUPLE({schema=[['RAWDATA','String']], type='Tuple', keepinput='false'}, %(sourceName))"
				+ "%(sinkName) = SENDER({sink='%(sinkName)',\n"
				+ "			             wrapper='GenericPush',\n"
				+ "                      transport='none',\n"
				+ "			             protocol='TextFileLogger',\n"
				+ "                      dataHandler='Tuple',\n"
				+ "                      options=[%(options)]},\n"
				+ "                     raw)\n";
		
		String liveViewQuery = 
				  "#PARSER PQL\n"		
				+ "#RUNQUERY\n"
				+ "raw = KEYVALUETOTUPLE({schema=[['RAWDATA','String']], type='Tuple', keepinput='false'}, %(sourceName))"
				+ "%(sinkName) = SENDER({sink='%(sinkName)',\n"
				+ "			             wrapper='GenericPush',\n"
				+ "                      transport='UDPClient',\n"
				+ "			             protocol='SimpleCSV',\n"
				+ "                      dataHandler='Tuple',\n"
				+ "                      options=[['host', '%(host)'], ['port', '%(port)'], ['write', '20480']]},\n"
				+ "                     raw)\n";
		
		String liveViewUrl = "udp://%(host):%(port)";
		
		Map<String, String> optionsInformation = new HashMap<>();
		optionsInformation.put("host", "The ethernet address for the scanner");
		optionsInformation.put("port", "The port number for the scanner");
		
		return new SensorType("LMS1xx", dataQuery, logQuery, liveViewQuery, liveViewUrl, optionsInformation);
	}

	static private SensorType getDummy()
	{
		String dataQuery =
				  "#PARSER PQL\n"		
				+ "#RUNQUERY\n"				
				+ "%(sourceName) := ACCESS({source='%(sourceName)',\n"
		    	+ "               wrapper='GenericPush',\n"
				+ "               transport='Timer',\n"
				+ "               protocol='None',\n"
				+ "               dataHandler='Tuple',\n"
				+ "				  schema=[['Timestamp', 'Long']],"
				+ "               options=[%(optionsEx) ['period','100']]})\n";

		String logQuery = 
				  "#PARSER PQL\n"		
				+ "#RUNQUERY\n"			
				+ "%(sinkName) = SENDER({sink='%(sinkName)',\n"
				+ "			             wrapper='GenericPush',\n"
				+ "                      transport='none',\n"
				+ "			             protocol='TextFileLogger',\n"
				+ "                      dataHandler='Tuple',\n"
				+ "                      options=[%(options)]},\n"
				+ "                     %(sourceName))\n";
		
		String liveViewQuery = 
				  "#PARSER PQL\n"		
				+ "#RUNQUERY\n"
				+ "%(sinkName) = SENDER({sink='%(sinkName)',\n"
				+ "			             wrapper='GenericPush',\n"
				+ "                      transport='UDPClient',\n"
				+ "			             protocol='SimpleCSV',\n"
				+ "                      dataHandler='Tuple',\n"
				+ "                      options=[['host', '%(host)'], ['port', '%(port)'], ['write', '20480']]},\n"
				+ "                     %(sourceName))\n";
		
		String liveViewUrl = "udp://%(host):%(port)";
		
		Map<String, String> optionsInformation = new HashMap<>();
		
		return new SensorType("Dummy", dataQuery, logQuery, liveViewQuery, liveViewUrl, optionsInformation);
	}	
	
	static private SensorType getIntegratedCamera()
	{
		String dataQuery = 
				"#PARSER PQL\n"		
			  + "#RUNQUERY\n"				
			  + "%(sourceName) := ACCESS({source='%(sourceName)',\n"
			  + "                         wrapper='GenericPull',\n"
			  + "                         transport='none',\n"
			  + "                         protocol='OpenCVVideoStream',\n"
			  + "                         dataHandler='Tuple',\n"
			  + "                         schema=[['Image', 'ImageJCV']],\n"
			  + "                         options=[%(optionsEx) ['timestampmode', 'none']})\n";

		String logQuery = 
				"#PARSER PQL\n"
			  + "#RUNQUERY\n"			
			  + "%(sinkName) = SENDER({sink='%(sinkName)',\n"
			  + "			           wrapper='GenericPush',\n"
			  + "                      transport='none',\n"
			  + "			           protocol='VideoLogger',\n"
			  + "                      dataHandler='Tuple',\n"
			  + "                      options=[%(options)]},\n"
			  + "                     %(sourceName))\n";		
		
		String liveViewQuery = 
				  "#PARSER PQL\n"		
				+ "#RUNQUERY\n"
				+ "resizedVideo = MAP({expressions=['resizeCV(image,300,300)']}, %(sourceName))\n"
				+ "%(sinkName) = SENDER({sink='%(sinkName)',\n"
				+ "			             wrapper='GenericPush',\n"
				+ "                      transport='none',\n"
				+ "			             protocol='FFmpegVideoStream',\n"
				+ "                      dataHandler='Tuple',\n"
				+ "                      options=[['streamurl', 'udp://%(host):%(port)'],\n"
				+ "                               ['format', 'h264'],\n"
				+ "                               ['codec:tune', 'zerolatency'],\n"
				+ "                               ['codec:preset', 'ultrafast']]},\n"
				+ "                     resizedVideo)\n";		
		
		String liveViewUrl = "udp://%(host):%(port)";
		
		Map<String, String> optionsInformation = new HashMap<>();
		optionsInformation.put("streamUrl", "Specify camera id as \"camera://[ID]\"");
		
		return new SensorType("IntegratedCamera", dataQuery, logQuery, liveViewQuery, liveViewUrl, optionsInformation);
	}	

	static private SensorType getBaslerCamera()
	{
		String dataQuery =
				"#PARSER PQL\n"		
			  + "#RUNQUERY\n"				
			  + "%(sourceName) := ACCESS({source='%(sourceName)',\n"
			  + "                         wrapper='GenericPull',\n"
			  + "                         transport='BaslerCamera',\n"
			  + "                         protocol='none',\n"
			  + "                         dataHandler='Tuple',\n"
			  + "                         schema=[['Image', 'ImageJCV']],\n"
			  + "                         options=[%(options)]})\n";

		String logQuery = 
				"#PARSER PQL\n"
			  + "#RUNQUERY\n"			
			  + "%(sinkName) = SENDER({sink='%(sinkName)',\n"
			  + "			           wrapper='GenericPush',\n"
			  + "                      transport='none',\n"
			  + "			           protocol='VideoLogger',\n"
			  + "                      dataHandler='Tuple',\n"
			  + "                      options=[%(options)]},\n"
			  + "                     %(sourceName))\n";		
		
		String liveViewQuery = 
				  "#PARSER PQL\n"		
				+ "#RUNQUERY\n"
				+ "resizedVideo = MAP({expressions=['resizeCV(image,300,300)']}, %(sourceName))\n"
				+ "%(sinkName) = SENDER({sink='%(sinkName)',\n"
				+ "			             wrapper='GenericPush',\n"
				+ "                      transport='none',\n"
				+ "			             protocol='FFmpegVideoStream',\n"
				+ "                      dataHandler='Tuple',\n"
				+ "                      options=[['streamurl', 'udp://%(host):%(port)'],\n"
				+ "                               ['format', 'h264'],\n"
				+ "                               ['codec:tune', 'zerolatency'],\n"
				+ "                               ['codec:preset', 'ultrafast']]},\n"
				+ "                     resizedVideo)\n";		
		
		String liveViewUrl = "udp://%(host):%(port)";
		
		Map<String, String> optionsInformation = new HashMap<>();
		optionsInformation.put("serialNumber", "The serial number of the camera");		
		
		return new SensorType("BaslerCamera", dataQuery, logQuery, liveViewQuery, liveViewUrl, optionsInformation);
	}		

	static private SensorType getOptrisCamera()
	{
		String dataQuery =
				"#PARSER PQL\n"		
			  + "#RUNQUERY\n"				
			  + "%(sourceName) := ACCESS({source='%(sourceName)',\n"
			  + "                         wrapper='GenericPush',\n"
			  + "                         transport='OptrisCamera',\n"
			  + "                         protocol='none',\n"
			  + "                         dataHandler='Tuple',\n"
			  + "                         schema=[['Image', 'ImageJCV']],\n"
			  + "                         options=[%(options)]})\n";

		String logQuery = 
				"#PARSER PQL\n"
			  + "#RUNQUERY\n"			
			  + "%(sinkName) = SENDER({sink='%(sinkName)',\n"
			  + "			           wrapper='GenericPush',\n"
			  + "                      transport='none',\n"
			  + "			           protocol='VideoLogger',\n"
			  + "                      dataHandler='Tuple',\n"
			  + "                      options=[%(options)]},\n"
			  + "                     %(sourceName))\n";		
		
		String liveViewQuery = 
				  "#PARSER PQL\n"		
				+ "#RUNQUERY\n"
				+ "resizedVideo = MAP({expressions=['resizeCV(image,300,300)']}, %(sourceName))\n"
				+ "%(sinkName) = SENDER({sink='%(sinkName)',\n"
				+ "			             wrapper='GenericPush',\n"
				+ "                      transport='none',\n"
				+ "			             protocol='FFmpegVideoStream',\n"
				+ "                      dataHandler='Tuple',\n"
				+ "                      options=[['streamurl', 'udp://%(host):%(port)'],\n"
				+ "                               ['format', 'h264'],\n"
				+ "                               ['codec:tune', 'zerolatency'],\n"
				+ "                               ['codec:preset', 'ultrafast']]},\n"
				+ "                     resizedVideo)\n";		
		
		String liveViewUrl = "udp://%(host):%(port)";
		
		Map<String, String> optionsInformation = new HashMap<>();
		optionsInformation.put("ethernetAddress", "The ethernet address for the camera");
		
		return new SensorType("OptrisCamera", dataQuery, logQuery, liveViewQuery, liveViewUrl, optionsInformation);
	}	

	static private SensorType getGPS()
	{
		String dataQuery =
			"#PARSER PQL\n"		
		  + "#RUNQUERY\n"				
		  + "gps = ACCESS({source='%(sourceName)',\n"
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

		String logQuery = 
				  "#PARSER PQL\n"		
				+ "#RUNQUERY\n"			
				+ "%(sinkName) = SENDER({sink='%(sinkName)',\n"
				+ "			             wrapper='GenericPush',\n"
				+ "                      transport='none',\n"
				+ "			             protocol='TextFileLogger',\n"
				+ "                      dataHandler='Tuple',\n"
				+ "                      options=[%(options)]},\n"
				+ "                     %(sourceName))\n";
		
		String liveViewQuery = 
				  "#PARSER PQL\n"		
				+ "#RUNQUERY\n"
				+ "%(sinkName) = SENDER({sink='%(sinkName)',\n"
				+ "			             wrapper='GenericPush',\n"
				+ "                      transport='UDPClient',\n"
				+ "			             protocol='SimpleCSV',\n"
				+ "                      dataHandler='Tuple',\n"
				+ "                      options=[['host', '%(host)'], ['port', '%(port)'], ['write', '20480']]},\n"
				+ "                     %(sourceName))\n";
		
		String liveViewUrl = "udp://%(host):%(port)";
		
		Map<String, String> optionsInformation = new HashMap<>();
		optionsInformation.put("port", "The port number for the GPS receiver (F. ex. COM3)");
		
		return new SensorType("GPS", dataQuery, logQuery, liveViewQuery, liveViewUrl, optionsInformation);
	}	
	
	static private SensorFactory instance = null;
	static public  SensorFactory getInstance() 
	{
		if (instance == null)
		{
			instance = new SensorFactory();
			instance.addSensorType(getLMS1xx());
			instance.addSensorType(getIntegratedCamera());
			instance.addSensorType(getBaslerCamera());			
			instance.addSensorType(getOptrisCamera());
			instance.addSensorType(getGPS());
			instance.addSensorType(getDummy());
		}				
		
		return instance;
	}

	private String loggingDirectory;
	
	private SensorFactory() 
	{
		loggingDirectory = "C:/test/records/";
	}
	
	// *************************************************************************************	
	
	private Map<String, SensorType> sensorTypes = new HashMap<>();
	private List<Sensor> sensors = new LinkedList<>();
	
	public void addSensorType(SensorType e)
	{
		sensorTypes.put(e.name.toLowerCase(), e);
	}
	
	public SensorType getSensorType(String name)
	{
		return sensorTypes.get(name.toLowerCase());
	}
	
	public List<String> getSensorTypes()
	{
		List<String> idList = new ArrayList<String>(sensorTypes.size());
		
		for (String s : sensorTypes.keySet())
			idList.add(s);
		
		return idList;
	}	
	
	public List<String> getSensorIds()
	{
		List<String> idList = new ArrayList<String>(sensors.size());
		
		for (Sensor s : sensors)
			idList.add(s.config.id);
		
		return idList;
	}
	
	public Sensor getSensorById(String id)
	{
		for (Sensor s : sensors)
			if (s.config.id.equals(id))
				return s;
		
		return null;
	}
	
	private Sensor getSensorByIdException(String id)
	{
		Sensor s = getSensorById(id);
		if (s == null)
			throw new RuntimeException("Sensor with id \"" + id + "\" does not exist!");
		return s;
	}
	
	public Sensor addSensor(ISession session, SensorModel2 sensor)
	{
		if (getSensorById(sensor.id) != null)
			throw new RuntimeException("Sensor with id \"" + sensor.id + "\" already exists!");

		Sensor s = new Sensor(sensor, session);
		sensors.add(s);
		return s;
	}

	public void modifySensor(ISession session, String id, SensorModel2 newInfo)
	{
		Sensor s = getSensorByIdException(id);
		s.modify(session, newInfo);
	}	
	
	public void removeSensor(ISession session, String id)
	{
		Sensor s = getSensorByIdException(id);
		s.remove(session);
		sensors.remove(s);
	}

	public void startLogging(ISession session, String sensorId) 
	{
		Sensor s = getSensorByIdException(sensorId);
		s.startLogging(session, loggingDirectory);
	}

	public void stopLogging(ISession session, String sensorId) 
	{
		Sensor s = getSensorByIdException(sensorId);
		s.stopLogging(session);		
	}
	
	public String startLiveView(ISession session, String sensorId, String targetHost, int targetPort) 
	{
		Sensor s = getSensorByIdException(sensorId);
		return s.startLiveView(session, targetHost, targetPort);
	}

	public void stopLiveView(ISession session, String sensorId) 
	{
		Sensor s = getSensorByIdException(sensorId);
		s.stopLiveView(session);		
	}	
	
}
