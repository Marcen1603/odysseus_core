package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.util.List;

/**
 * A preprocessor object that calculates the datarate in milliseconds by returning
 * the mean time between two corresponding read_done and read_init events.
 * 
 * The first tuple gets skipped because the latency can be significantly higher
 * than all other reads (think set oriented planoperators).
 * If you want the datarate calculation based on next_init and next_done
 * events (needed for set oriented operators) use
 * {@link DatarateNextProcessor} instead.
 * @author Jonas Jacobi
 */
public class DatarateReadProcessor implements IPreprocessorObject<Float> {
	private static final long serialVersionUID = 1L;

	public void processData(List<POEventData> events, PreprocessedMetadata data) {
		long initTime = 0;
		long sumTime = 0;
		long eventCount = 0;
		for (POEventData curEvent : events) {
			switch (curEvent.getType()) {
			case ReadInit:
				initTime = curEvent.getTime();
				break;
			case ReadDone:
				sumTime += curEvent.getTime() - initTime;
				++eventCount;
				break;
			default:
				continue;
			}
		}

		data.putResult(this, (float) sumTime / eventCount);	}
}
