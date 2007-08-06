package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.util.List;

import mg.dynaquest.queryexecution.event.POEventType;

/**
 * A preprocessor object that calculates the latency of a planoperatorcycle
 * in milliseconds
 * (difference between first next_done and first next_init event)
 * 
 * @author Jonas Jacobi
 */
public class LatencyProcessor implements IPreprocessorObject<Long> {
	private static final long serialVersionUID = -2276019292226219062L;
	
	public void processData(List<POEventData> events, PreprocessedMetadata data) {
		long initTime = 0;
		for (POEventData curEvent : events) {
			if (curEvent.getType() == POEventType.NextInit) {
				initTime = curEvent.getTime();
			} else {
				if (curEvent.getType() == POEventType.NextDone) {
					long doneTime = curEvent.getTime();
					data.putResult(this, doneTime - initTime);
					return;
				}
			}
		}
		data.putResult(this, -1L);
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
