package de.uniol.inf.is.odysseus.sensormanagement.common.logging;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "videoLog")
public class VideoLogMetaData extends LogMetaData
{
	public String videoFile;
	public boolean doRotate180;
	public String fps;
	public String decoder;
	public Integer bitsPerPixel;
	public Integer pixelFormat;
	
	@Deprecated
	public String syncFile;	
}