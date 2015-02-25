package de.uniol.inf.is.odysseus.sensors;

import java.util.HashMap;
import java.util.Map;

public abstract class VideoSensor extends SensorModel
{
	public VideoSensor(SensorModel sensorInfo) 
	{
		super(sensorInfo);
	}
	
	@Override public String getLiveViewQueryText(String userName, String destinationIP, int destinationPort)
	{
		String liveViewQueryName = getLiveViewQueryName(userName); 		
		String streamUrl = getLiveViewURL(destinationIP, destinationPort);
		
		Map<String, String> options = new HashMap<>();
		options.put("streamUrl", streamUrl);		
		
		String liveQuery; 
		liveQuery  = "#PARSER PQL	\n";		
		liveQuery += "#RUNQUERY		\n";			
		
		liveQuery += "resizedVideo = MAP({expressions=['resizeCV(image,300,300)']}, " + getSourceName() + ")	\n";
		liveQuery += getSenderQuery(liveViewQueryName, liveViewQueryName, "GenericPush", "none", "VideoStream", "Tuple", "resizedVideo", options);		
		
		return liveQuery;
	}
	
	@Override public String getLiveViewURL(String destinationIP, int destinationPort)
	{
		return "udp://" + destinationIP + ":" + destinationPort;
	}	
	
	@Override public String getLoggingQueryText(Map<String, String> options)
	{
		options.put("framerate", "30.0");

		String sinkName = getLoggingQueryName();
		String logQuery; 
		logQuery  = "#PARSER PQL	\n";		
		logQuery += "#RUNQUERY		\n";			
		
		logQuery += getSenderQuery(sinkName, sinkName, "GenericPush", "File", "VideoLogger", "Tuple", getSourceName(), options);
		
		return logQuery;
	}	
}
