package de.uniol.inf.is.odysseus.sensormanagement.common.types;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.SimpleMapXmlAdapter;

@XmlRootElement(name = "sensortype")
public class SensorType
{
	public final String name;
	public final String liveDataQueryText;
	public final String simulatedDataQueryText;
	public final String loggingQueryText;
	public final String liveViewQueryText;
	public final String liveViewUrl;
	
	@XmlJavaTypeAdapter(SimpleMapXmlAdapter.class)
	public final Map<String, String> optionsInformation;	
	
	public SensorType()
	{		
		name = liveDataQueryText = simulatedDataQueryText = loggingQueryText = liveViewQueryText = liveViewUrl = null;
		optionsInformation = null;
	}
	
	public SensorType(String name, String liveDataQueryText, String simulatedDataQueryText, String loggingQueryText, String liveViewQueryText, String liveViewUrl, Map<String, String> optionsInformation) 
	{
		this.name = name;
		this.liveDataQueryText = liveDataQueryText;
		this.simulatedDataQueryText = simulatedDataQueryText;
		this.loggingQueryText = loggingQueryText;
		this.liveViewQueryText = liveViewQueryText;
		this.liveViewUrl = liveViewUrl;
		this.optionsInformation = optionsInformation;
	}		
	
	@Override public String toString()
	{
		return name;
	}
}