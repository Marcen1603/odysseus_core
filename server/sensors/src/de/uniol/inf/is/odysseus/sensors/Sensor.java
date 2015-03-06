package de.uniol.inf.is.odysseus.sensors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.sensors.SensorFactory.SensorType;
import de.uniol.inf.is.odysseus.sensors.types.SensorModel2;
import de.uniol.inf.is.odysseus.sensors.utilities.StringUtils;
import de.uniol.inf.is.odysseus.sensors.utilities.XmlMarshalHelper;

public class Sensor 
{
	SensorModel2 config;
	SensorType type;
	
	boolean isLogging;
	
	LinkedList<String> liveViewQueries = new LinkedList<String>();
	
	public String getDataQueryName()
	{
		return config.id;
	}

	public String getLoggingQueryName()
	{
		return config.id + "_log";
	}	
	
	public String getLiveViewQueryName(ISession session) 
	{
		return config.id + "_live_" + session.getUser().getName();
	}
	
	
	public static void safeAddQuery(String queryName, String queryText, ISession session)
	{
		if (ExecutorServiceBinding.getExecutor().getLogicalQueryByName(queryName, session) != null)
			ExecutorServiceBinding.getExecutor().removeQuery(queryName, session);		
		
		try
		{
			ExecutorServiceBinding.getExecutor().addQuery(queryText, "OdysseusScript", session, Context.empty());
		}
		catch (Exception e)
		{
			try
			{
				ExecutorServiceBinding.getExecutor().removeQuery(queryName, session);
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
			
			throw e;
		}
	}
	
	public Sensor(SensorModel2 sensor, ISession session) 
	{
		this.config = sensor;
		
		type = SensorFactory.getInstance().getSensorType(sensor.type);

		String options = StringUtils.mapToString(config.options);
		
		Map<String, Object> formatMap = new HashMap<>();
		formatMap.put("sourceName", getDataQueryName());
		formatMap.put("options", options);
		formatMap.put("optionsEx", (options.length() > 0) ? (options + ",") : "");
		
		String queryText = StringUtils.namedFormatStr(type.dataQueryText, formatMap);
		safeAddQuery(getDataQueryName(), queryText, session);
	}
	
	public void remove(ISession session)
	{
		stopLogging(session);
		
		while (!liveViewQueries.isEmpty())
		{
			try
			{
				ExecutorServiceBinding.getExecutor().removeQuery(liveViewQueries.pop(), session);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		try
		{
			ExecutorServiceBinding.getExecutor().removeViewOrStream(getDataQueryName(), session);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void startLogging(ISession session, String directory)
	{
		if (isLogging) return;
		
		Map<String, String> loggingOptionsMap = new HashMap<>();
		loggingOptionsMap.put("sensorXml", XmlMarshalHelper.toXml(config));
		loggingOptionsMap.put("directory", directory);
		loggingOptionsMap.put("sizeLimit", Integer.toString(300*1024*1024)); // 300MB		
		
		Map<String, Object> formatMap = new HashMap<>();
		formatMap.put("sourceName", getDataQueryName());
		formatMap.put("sinkName", getLoggingQueryName());
		formatMap.put("options", StringUtils.mapToString(loggingOptionsMap));		
		
		String queryText = StringUtils.namedFormatStr(type.loggingQueryText, formatMap);
		safeAddQuery(getLoggingQueryName(), queryText, session);
		isLogging = true;
	}
	
	public void stopLogging(ISession session)
	{
		if (!isLogging) return;
		
		try
		{
			ExecutorServiceBinding.getExecutor().removeQuery(getLoggingQueryName(), session);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		isLogging = false;
	}
	
	public String startLiveView(ISession session, String targetHost, int targetPort)
	{
		String queryName = getLiveViewQueryName(session);	
		if (liveViewQueries.contains(queryName)) return null;
		
		Map<String, Object> formatMap = new HashMap<>();
		formatMap.put("sourceName", getDataQueryName());
		formatMap.put("sinkName", queryName);
		formatMap.put("host", targetHost);
		formatMap.put("port", targetPort);
				
		String queryText = StringUtils.namedFormatStr(type.liveViewQueryText, formatMap);
		safeAddQuery(queryName, queryText, session);
		
		liveViewQueries.add(queryName);
		return StringUtils.namedFormatStr(type.liveViewUrl, formatMap);
	}
	
	public void stopLiveView(ISession session)
	{
		String queryName = getLiveViewQueryName(session);
		if (!liveViewQueries.contains(queryName)) return;		
		liveViewQueries.remove(queryName);
		
		try
		{
			ExecutorServiceBinding.getExecutor().removeQuery(queryName, session);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
}
