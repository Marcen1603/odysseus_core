package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.util.List;

/**
 * A preprocessor object that calculates the mean time needed for
 * the reading of one tuple.
 * 
 * The first tuple gets skipped because the latency can be significantly higher
 * than all other reads (think set oriented planoperators).
 * the time for on read is calculated as difference between next_done and the related next_init
 * event. If you need the calculation based on read_done and read_init use
 * {@link DatarateReadProcessor} instead (invalid for set oriented operators).
 * @author Jonas Jacobi
 *
 */
public class DatarateNextProcessor implements IPreprocessorObject<Float> {
	private static final long serialVersionUID = 4745343095412442506L;

	public void processData(List<POEventData> events, PreprocessedMetadata data) {
		long initTime = 0;
		long sumTime = 0;
		long eventCount = 0;
		boolean first = true;
		for (POEventData curEvent : events) {
			switch (curEvent.getType()) {
			case NextInit:
				initTime = curEvent.getTime();
				break;
			case NextDone:
				if (first) {
					first = false;
				} else {
					sumTime += curEvent.getTime() - initTime;
					++eventCount;
				}
				break;
			default:
				continue;
			}
		}
		if (eventCount != 0) {
			data.putResult(this, (float) sumTime / eventCount);
		}
	}

}
