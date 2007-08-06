package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.util.ArrayList;
import java.util.List;

/**
 * A preprocessor object that calculates the standard deviation
 * between next_init and nex_done events. The standard deviation
 * can be an indicator for bursty deliveries.
 * @author Jonas Jacobi
 */
public class StandardDeviationProcessor implements IPreprocessorObject<Double> {

	private static final long serialVersionUID = -5025686726789226360L;

	public void processData(List<POEventData> events, PreprocessedMetadata data) {
		long initTime = 0;
		long sumTime = 0;
		List<Long> entries = new ArrayList<Long>();
		for (POEventData curEvent : events) {
			switch (curEvent.getType()) {
			case NextInit:
				initTime = curEvent.getTime();
				break;
			case NextDone:
				long time = initTime - curEvent.getTime();
				sumTime += time;
				entries.add(time);
			default:
				continue;
			}
		}
		if (entries.size() - 1 < 1) {
			return;
		}
		float mediumTime = (float) sumTime / entries.size();
		float stddev = 0;
		for (Long l : entries) {
			stddev += Math.pow(Math.abs(l - mediumTime), 2);
		}

		data.putResult(this, Math.sqrt(stddev / (entries.size() - 1)));
	}

}
