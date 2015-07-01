package de.uniol.inf.is.odysseus.sensormanagement.application.view.playback;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.SimpleMapXmlAdapter;

@XmlRootElement(name = "view")
@XmlAccessorType (XmlAccessType.FIELD)
public class VisualizationConstraints extends TimeInterval
{
	@XmlElement(name = "constraints")
	@XmlJavaTypeAdapter(SimpleMapXmlAdapter.class)
	final Map<String, String> constraintMap;
	
	public VisualizationConstraints()
	{
		constraintMap = new HashMap<>();
	}
	
	public VisualizationConstraints(double startTime, double endTime, Map<String, String> constraintMap) 
	{
		super(startTime, endTime);
		
		this.constraintMap = constraintMap;
	}
}
