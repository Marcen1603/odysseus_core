package de.uniol.inf.is.odysseus.sensors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.sensors.types.SensorModel;
import de.uniol.inf.is.odysseus.sensors.types.SensorModel2;

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
				+ "               options=[%(options)]})\n";

		String logQuery = 
				  "#PARSER PQL\n"		
				+ "#RUNQUERY\n"			
				+ "raw = KEYVALUETOTUPLE({schema=[['RAWDATA','String']], type='Tuple', keepinput='false'}, %(sourceName))"
				+ "$(sinkName) = SENDER({sink='$(sinkName)',\n"
				+ "			             wrapper='GenericPush',\n"
				+ "                      transport='none',\n"
				+ "			             protocol='TextFileLogger',\n"
				+ "                      dataHandler='Tuple',\n"
				+ "                      options=[%(options)]},\n"
				+ "                     raw)\n";
		
		return new SensorType(dataQuery, logQuery);
	}

	static private SensorType getIntegratedCamera()
	{
		String dataQuery = 
				"#PARSER PQL\n"		
			  + "#RUNQUERY\n"				
			  + "%(sourceName) := ACCESS({source='%(sourceName)',\n"
			  + "                         wrapper='GenericPull',\n"
			  + "                         transport='IntegratedCamera',\n"
			  + "                         protocol='none',\n"
			  + "                         dataHandler='Tuple',\n"
			  + "                         schema=[['Image', 'ImageJCV']],\n"
			  + "                         options=[%(options)]})\n";

		String logQuery = 
				"#PARSER PQL\n"
			  + "#RUNQUERY\n"			
			  + "$(sinkName) = SENDER({sink='$(sinkName)',\n"
			  + "			           wrapper='GenericPush',\n"
			  + "                      transport='none',\n"
			  + "			           protocol='VideoLogger',\n"
			  + "                      dataHandler='Tuple',\n"
			  + "                      options=[%(options)]},\n"
			  + "                     %(sourceName))\n";		
		
		return new SensorType(dataQuery, logQuery);
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
			  + "$(sinkName) = SENDER({sink='$(sinkName)',\n"
			  + "			           wrapper='GenericPush',\n"
			  + "                      transport='none',\n"
			  + "			           protocol='VideoLogger',\n"
			  + "                      dataHandler='Tuple',\n"
			  + "                      options=[%(options)]},\n"
			  + "                     %(sourceName))\n";		
		
		return new SensorType(dataQuery, logQuery);
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
			  + "$(sinkName) = SENDER({sink='$(sinkName)',\n"
			  + "			           wrapper='GenericPush',\n"
			  + "                      transport='none',\n"
			  + "			           protocol='VideoLogger',\n"
			  + "                      dataHandler='Tuple',\n"
			  + "                      options=[%(options)]},\n"
			  + "                     %(sourceName))\n";		
		
		return new SensorType(dataQuery, logQuery);
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
				+ "$(sinkName) = SENDER({sink='$(sinkName)',\n"
				+ "			             wrapper='GenericPush',\n"
				+ "                      transport='none',\n"
				+ "			             protocol='TextFileLogger',\n"
				+ "                      dataHandler='Tuple',\n"
				+ "                      options=[%(options)]},\n"
				+ "                     %(sourceName))\n";
		
		return new SensorType(dataQuery, logQuery);
	}	
	
	static private SensorFactory instance = null;
	static public  SensorFactory getInstance() 
	{
		if (instance == null)
		{
			instance = new SensorFactory();
			instance.addSensorType("LMS1xx", getLMS1xx());
			instance.addSensorType("IntegratedCamera", getIntegratedCamera());
			instance.addSensorType("BaslerCamera", getBaslerCamera());			
			instance.addSensorType("OptrisCamera", getOptrisCamera());
			instance.addSensorType("GPS", getGPS());
		}				
		
		return instance;
	}
	
	private SensorFactory() {}
	
	// *************************************************************************************

	public static class SensorType
	{
		public String dataQueryText;
		public String loggingQueryText;
		
		public SensorType(String dataQueryText, String loggingQueryText) 
		{
			this.dataQueryText = dataQueryText;
			this.loggingQueryText = loggingQueryText;
		}		
	}	
	
	private Map<String, SensorType> entries = new HashMap<>();
	private List<Sensor> sensors = new LinkedList<>();
	
	public void addSensorType(String name, SensorType e)
	{
		entries.put(name.toLowerCase(), e);
	}
	
	public SensorType getSensorType(String name)
	{
		return entries.get(name.toLowerCase());
	}
	
	public Sensor getSensorById(String id)
	{
		for (Sensor s : sensors)
			if (s.config.id.equals(id))
				return s;
		
		return null;
	}
	
	public void addSensor(ISession session, SensorModel2 sensor)
	{
		if (getSensorById(sensor.id) != null)
			throw new RuntimeException("Sensor with id \"" + sensor.id + "\" already exists!");
		
		Sensor s = new Sensor(sensor, session);
		sensors.add(s);
	}
	
	public void removeSensor(ISession session, String id)
	{
		Sensor s = getSensorById(id);
		if (s == null)
			throw new RuntimeException("Sensor with id \"" + id + "\" does not exist!");

		s.remove(session);
		sensors.remove(s);
	}	
}
