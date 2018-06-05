package de.uniol.inf.is.odysseus.sensormanagement.common.logging;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "textFileLog")
public class TextLogMetaData extends LogMetaData
{
	public String rawFile;
}