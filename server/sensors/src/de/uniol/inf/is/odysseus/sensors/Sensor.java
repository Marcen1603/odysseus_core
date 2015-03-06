package de.uniol.inf.is.odysseus.sensors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.sensors.SensorFactory.SensorType;
import de.uniol.inf.is.odysseus.sensors.types.SensorModel;
import de.uniol.inf.is.odysseus.sensors.types.SensorModel2;

public class Sensor 
{
	SensorModel2 config;
	SensorType type;
	
	String dataQueryName;
	
	public Sensor(SensorModel2 sensor, ISession session) 
	{
		this.config = sensor;
		
		type = SensorFactory.getInstance().getSensorType(sensor.type);

		Map<String, Object> formatMap = new HashMap<>();
		formatMap.put("sourceName", config.id);
		formatMap.put("options", SensorModel.MapToString(config.options));		
		
		String queryText = SensorModel.namedFormatStr(type.dataQueryText, formatMap);
		Collection<Integer> result = ExecutorServiceBinding.getExecutor().addQuery(queryText, "PQL", session, Context.empty());
		if (result.size() == 0)
			throw new RuntimeException("no query added?!");
		
		dataQueryName = ExecutorServiceBinding.getExecutor().getLogicalQueryById(result.iterator().next(), session).getName();		
	}
	
	public void remove(ISession session)
	{
		ExecutorServiceBinding.getExecutor().stopQuery(dataQueryName, session);		
	}
	
	public void startLogging(ISession session)
	{
		ExecutorServiceBinding.getExecutor().addQuery(type.loggingQueryText, "PQL", session, Context.empty());
	}
	
	public void stopLogging(ISession session)
	{
		
	}
}
