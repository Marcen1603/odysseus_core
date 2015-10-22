package de.uniol.inf.is.odysseus.sensormanagement.common.types;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.uniol.inf.is.odysseus.sensormanagement.common.types.position.Position;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.SimpleMapXmlAdapter;

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
	
	// If this value is set, this sensor will be set up as a sensor in Odysseus which receives playback data
	public String playbackConfig;
			
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
		playbackConfig = other.playbackConfig;
	}
}
