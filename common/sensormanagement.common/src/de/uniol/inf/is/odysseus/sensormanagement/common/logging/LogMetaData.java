package de.uniol.inf.is.odysseus.sensormanagement.common.logging;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.XmlMarshalHelperHandler;


@XmlRootElement(name = "log")
public class LogMetaData implements XmlMarshalHelperHandler
{
	@XmlTransient
	public String path;
	
	public String sensorId;
	public long startTime;
	public long endTime = 0;
	public String loggingStyle;
	
	public static class AdditionalData
	{
		public String fileName;
		public String header;
	};
	
	public AdditionalData additionalData;
	
	@Override public void onUnmarshalling(File xmlFile) 
	{
		path = xmlFile.getParent() + File.separator;
	}
	
	@Override public void onUnmarshalling(String xmlString) 
	{
	}
}