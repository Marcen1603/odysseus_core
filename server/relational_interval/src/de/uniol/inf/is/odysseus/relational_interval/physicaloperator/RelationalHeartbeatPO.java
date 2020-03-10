package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class RelationalHeartbeatPO<T extends ITimeInterval> extends AbstractPipe<Tuple<T>, Tuple<T>>{

	
	private final RelationalExpression<T> timeExpression;
	private final boolean createOnHeartbeat;

	private PointInTime lastTS = null;
	
	public RelationalHeartbeatPO(RelationalExpression<T> timeExpression, boolean createOnHeartbeat) {
		this.timeExpression = timeExpression;
		this.createOnHeartbeat = createOnHeartbeat;
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation.isHeartbeat() && createOnHeartbeat && lastTS != null) {
			sendPunctuation(Heartbeat.createNewHeartbeat(lastTS));
		}else {
			// TODO: What to do with other types of punctuations?
		}
		
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(Tuple<T> object, int port) {
		Object value = timeExpression.evaluate(object, null, null);
		if (value instanceof PointInTime) {
			lastTS = (PointInTime) value;
		}else if (value != null) {
			lastTS = new PointInTime(((Number)value).longValue());
		}
		
		transfer(object);
	}

}
