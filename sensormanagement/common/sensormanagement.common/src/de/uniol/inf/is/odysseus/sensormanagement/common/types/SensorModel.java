package de.uniol.inf.is.odysseus.sensormanagement.common.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.uniol.inf.is.odysseus.sensormanagement.common.types.position.Position;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.SimpleMapXmlAdapter;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.StringMapMapXmlAdapter;

@XmlRootElement(name = "sensor")
public class SensorModel 
{	
	@XmlID
	public String id;
	public String type;
	public String displayName;
	
	@XmlJavaTypeAdapter(SimpleMapXmlAdapter.class)
	public Map<String, String> options = new HashMap<>();
	public Position position;
	
	@XmlJavaTypeAdapter(StringMapMapXmlAdapter.class)
	@XmlElement(name = "loggingStyle")
	private Map<String, Map<String, String>> loggingStyles = new HashMap<>();	
	
	// If this value is set, this sensor will be set up as a simulated sensor in Odysseus which receives simulated data
	public String simulationConfig;
	public static final String DEFAULT_LOGGING_STYLE = "default";
			
	public SensorModel()
	{
	}

	public SensorModel(SensorModel other) 
	{
		assign(other);
	}
	
	public void generateId()
	{
		id = type + "_" + Integer.toString((int) (Math.random() * Integer.MAX_VALUE));
	}

	public void assign(SensorModel other) 
	{
		id = other.id;
		type = other.type;
		displayName = other.displayName;
		
		options = new HashMap<>(other.options);
		position = other.position == null ? null : other.position.clone();
		simulationConfig = other.simulationConfig;
		loggingStyles = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		loggingStyles.putAll(other.loggingStyles);
	}
	
	public void removeLoggingStyle(String loggingStyle)
	{
		loggingStyles.remove(loggingStyle.toLowerCase());
	}

	public void putLoggingStyle(String loggingStyle, Map<String, String> loggingStyleOptions)
	{
		loggingStyles.put(loggingStyle.toLowerCase(), loggingStyleOptions);
	}

	public Map<String, String> getLoggingStyle(String loggingStyle)
	{
		return loggingStyles.get(loggingStyle.toLowerCase());
	}

	public Set<String> getLoggingStyles() 
	{
		return loggingStyles.keySet();
	}

	
	// TODO: Implement equals and hashCode
}
