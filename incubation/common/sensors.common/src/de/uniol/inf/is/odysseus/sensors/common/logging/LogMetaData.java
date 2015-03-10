package de.uniol.inf.is.odysseus.sensors.common.logging;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import de.uniol.inf.is.odysseus.sensors.common.types.SensorModel2;
import de.uniol.inf.is.odysseus.sensors.common.utilities.XmlMarshalHelperHandler;

@XmlRootElement(name = "log")
public class LogMetaData implements XmlMarshalHelperHandler
{
	@XmlTransient
	public String path;
	
	public SensorModel2 sensor;
	public long startTime;
	public long endTime = 0;
	
	@Override public void onUnmarshalling(File xmlFile) 
	{
		path = xmlFile.getParent() + File.separator;
	}
	
	@Override public void onUnmarshalling(String xmlString) 
	{
	}
}