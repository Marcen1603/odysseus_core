package de.uniol.inf.is.odysseus.sensormanagement.common.types;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "serverinstance")
@XmlAccessorType (XmlAccessType.FIELD)
public class ServerInstance
{
	public String name;
	
	@XmlIDREF
	public SensorModel positionSensor;
	
	public List<SensorModel> sensors = new ArrayList<>();
	
	// Contains sensors to which recordings exist
	@XmlTransient
	public Set<SensorModel> recordingSet = new HashSet<>();
	
	
/*	public Position getPosition() 
	{
		return positionSensor == null ? null : Position.sub(positionSensor.getMeasuredPosition(), (RelativePosition) positionSensor.getPosition());
	}*/
}
