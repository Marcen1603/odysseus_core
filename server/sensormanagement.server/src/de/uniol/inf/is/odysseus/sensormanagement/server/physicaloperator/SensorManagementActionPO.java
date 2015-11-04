package de.uniol.inf.is.odysseus.sensormanagement.server.physicaloperator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.sensormanagement.server.SensorFactory;
import de.uniol.inf.is.odysseus.sensormanagement.server.logicaloperator.SensorManagementActionAO;

public class SensorManagementActionPO extends AbstractSink<Tuple<IMetaAttribute>> {

	private static final Logger LOG = LoggerFactory.getLogger(SensorManagementActionPO.class);

	private final RelationalExpression<IMetaAttribute> commandExpression;
	private final RelationalExpression<IMetaAttribute> sensorIdExpression;

	private ISession caller;

	public SensorManagementActionPO(SensorManagementActionAO ao) {
		Preconditions.checkNotNull(ao, "ao must not be null!");

		this.commandExpression = new RelationalExpression<IMetaAttribute>(ao.getCommandExpression());
		this.commandExpression.initVars(ao.getInputSchema(0));
		this.sensorIdExpression = new RelationalExpression<IMetaAttribute>(ao.getSensorIdExpression());
		this.sensorIdExpression.initVars(ao.getInputSchema(0));
	}

	@Override
	protected void process_open() throws OpenFailedException {

		List<ISession> callers = getSessions();
		if (callers.size() != 1) {
			throw new OpenFailedException("This operator cannot be sharded");
		}
		caller = callers.get(0);
	}

	@Override
	protected void process_next(Tuple<IMetaAttribute> object, int port) {
		List<Tuple<IMetaAttribute>> preProcessResult = null;

		String command = null;
		try {
			Object v = this.commandExpression.evaluate(object, getSessions(), preProcessResult);
			if (v != null) {
				command = v.toString();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
		
		String sensorId = null;
		try{
			Object v = this.sensorIdExpression.evaluate(object, getSessions(), preProcessResult);
			if (v != null){
				sensorId = v.toString();
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
				
		if (!Strings.isNullOrEmpty(command) && !Strings.isNullOrEmpty(sensorId)) {
			tryExecuteCommand(sensorId, command);
		}else{
			LOG.warn("Cannot execute " + command + " on sensor " + sensorId);
		}
	}

	private void tryExecuteCommand(String sensorId, String command) {
		SensorFactory instance = SensorFactory.getInstance();
		
		try {
			switch (command.toUpperCase()) 
			{
				case "START_LOGGING":
					instance.startLogging(caller, sensorId);
					break;

				case "STOP_LOGGING":
					instance.stopLogging(caller, sensorId);
					break;

				default:
					LOG.error("Unknown command '{}' for sensor id {}", command, sensorId);
			}
		} catch (Throwable t) {
			LOG.error("Could not execute command '{}' for sensor id {}", new Object[] { command, sensorId, t });
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// ignore
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}
}
