package de.uniol.inf.is.odysseus.sensors.common.logging;

import javax.xml.bind.annotation.XmlRootElement;

import de.uniol.inf.is.odysseus.sensors.common.types.SensorModel2;

@XmlRootElement(name = "log")
public class LogMetaData
{
	public SensorModel2 sensor;
	public long startTime;
	public long endTime = 0;
}