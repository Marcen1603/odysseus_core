package de.uniol.inf.is.odysseus.sensormanagement.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorType;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.StringUtils;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.XmlMarshalHelper;

public class Sensor 
{
	private final static IServerExecutor executor = ExecutorServiceBinding.getExecutor();
	
	SensorModel config;
	SensorType type;
	
	boolean isLogging;
	
	public String getDataQueryName()
	{
		return config.id;
	}

	public String getLoggingQueryName()
	{
		return config.id + "_log";
	}	
	
	private String getLiveViewQueryNameBase()
	{
		return config.id + "_live_";
	}
	
	public String getLiveViewQueryName(ISession session) 
	{
		return getLiveViewQueryNameBase() + session.getUser().getName();
	}

	public static void safeRemoveQuery(String queryName, ISession session)
	{
		if (executor.getLogicalQueryByName(queryName, session) != null)
			executor.removeQuery(queryName, session);		
	}
	
	public static void safeAddQuery(String queryName, String queryText, ISession session)
	{
		safeRemoveQuery(queryName, session);
		
		try	{
			executor.addQuery(queryText, "OdysseusScript", session, Context.empty());
		} catch (Exception e) {
			try {
				executor.removeQuery(queryName, session);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
			throw e;
		}
	}
	
	public Sensor(SensorModel sensor, ISession session) 
	{
		config = sensor;
		if (config.id == null) config.generateId();
		
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
		
		Collection<Integer> querys = executor.getLogicalQueryIds(session);		
		for (int id : querys)
		{
			if (executor.getLogicalQueryById(id, session).getName().startsWith(getLiveViewQueryNameBase())) {
				try {
					executor.removeQuery(id, session);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
				
		try {
			executor.removeViewOrStream(getDataQueryName(), session);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void modify(ISession caller, SensorModel newSensorInfo)
	{
		if (!config.id.equals(newSensorInfo.id))
		{
			// TODO: Change exception type
			throw new RuntimeException("This is not the same sensor!");
		}		
		
		if (!config.type.equals(newSensorInfo.type))
		{
			// TODO: Do this here
			throw new RuntimeException("Cannot change type of sensor. Create a new one!");
		}

		// TODO: things like ethernetAddr etc. can have changed. Restart query then!
		config = newSensorInfo;
	}
	
	public void startLogging(ISession session, String directory)
	{
		String queryName = getLoggingQueryName();		
		if (executor.getLogicalQueryByName(queryName, session) != null) return;
		
		Map<String, String> loggingOptionsMap = new HashMap<>();
		loggingOptionsMap.put("sensorXml", XmlMarshalHelper.toXml(config));
		loggingOptionsMap.put("directory", directory);
		loggingOptionsMap.put("sizeLimit", Integer.toString(300*1024*1024)); // 300MB		
		
		Map<String, Object> formatMap = new HashMap<>();
		formatMap.put("sourceName", getDataQueryName());
		formatMap.put("sinkName", getLoggingQueryName());
		formatMap.put("options", StringUtils.mapToString(loggingOptionsMap));		
		
		String queryText = StringUtils.namedFormatStr(type.loggingQueryText, formatMap);
		safeAddQuery(queryName, queryText, session);
		isLogging = true;
	}
	
	public void stopLogging(ISession session)
	{
		safeRemoveQuery(getLoggingQueryName(), session);
	}
	
	public String startLiveView(ISession session, String targetHost, int targetPort)
	{
		String queryName = getLiveViewQueryName(session);	
		
		Map<String, Object> formatMap = new HashMap<>();
		formatMap.put("sourceName", getDataQueryName());
		formatMap.put("sinkName", queryName);
		formatMap.put("host", targetHost);
		formatMap.put("port", targetPort);
				
		String queryText = StringUtils.namedFormatStr(type.liveViewQueryText, formatMap);
		safeAddQuery(queryName, queryText, session);
		
		return StringUtils.namedFormatStr(type.liveViewUrl, formatMap);
	}
	
	public void stopLiveView(ISession session)
	{
		safeRemoveQuery(getLiveViewQueryName(session), session);
	}	
}