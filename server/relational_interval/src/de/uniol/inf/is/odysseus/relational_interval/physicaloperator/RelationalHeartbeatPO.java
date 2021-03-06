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
	private final RelationalExpression<T> sendHeartbeatExpression;
	private final boolean suppressFireOnElement;

	private PointInTime lastTS = null;
	
	public RelationalHeartbeatPO(RelationalExpression<T> timeExpression, RelationalExpression<T> sendHeartbeatExpression, boolean createOnHeartbeat, boolean suppressFireOnElement) {
		this.timeExpression = timeExpression;
		this.createOnHeartbeat = createOnHeartbeat;
		this.sendHeartbeatExpression = sendHeartbeatExpression;		
		this.suppressFireOnElement = suppressFireOnElement;
	}
	
	@Override
	protected void process_next(Tuple<T> object, int port) {
		evaluateTimeExpression(object);
		boolean heartBeatExpression = evaluateHeartbeatExpression(object);		
		
		if (!heartBeatExpression) {
			transfer(object);
		}else {
			if (!suppressFireOnElement) {
				transfer(object);
			}
		}
		
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation.isHeartbeat() && createOnHeartbeat && lastTS != null) {
			sendHeartbeat();
		}else {
			// TODO: What to do with other types of punctuations?
		}		
	}
	
	private boolean evaluateHeartbeatExpression(Tuple<T> object) {
		if (sendHeartbeatExpression != null && sendHeartbeatExpression.evaluate(object)){
			sendHeartbeat();
			return true;
		}
		return false;
	}

	private void evaluateTimeExpression(Tuple<T> object) {
		Object value = timeExpression.evaluate(object, null, null);
		if (value instanceof PointInTime) {
			lastTS = (PointInTime) value;
		}else if (value != null) {
			lastTS = new PointInTime(((Number)value).longValue());
		}
	}

	private void sendHeartbeat() {
		sendPunctuation(Heartbeat.createNewHeartbeat(lastTS));
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}




}
