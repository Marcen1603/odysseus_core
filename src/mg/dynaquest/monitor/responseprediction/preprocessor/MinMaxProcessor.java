package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.util.List;

/**
 * A preprocessor object that calculates the minimum and maximum times
 * in milliseconds between two corresponding next_init and next_done events
 * @author Jonas Jacobi
 */
public class MinMaxProcessor implements IPreprocessorObject<MinMaxProcessor.MinMax> {
	private static final long serialVersionUID = 1L;

	/**
	 * Class containing both minimum and maximum
	 * @author Jonas Jacobi
	 */
	public static class MinMax {
		private long min;

		private long max;

		public MinMax(long min, long max) {
			this.min = min;
			this.max = max;
		}

		public long getMax() {
			return this.max;
		}

		public long getMin() {
			return this.min;
		}
	}

	public void processData(List<POEventData> events,
			PreprocessedMetadata data) {
		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;

		long initTime = 0;
		for (POEventData curEvent : events) {
			switch (curEvent.getType()) {
			case NextInit:
				initTime = curEvent.getTime();
				break;
			case NextDone:
				long delta = curEvent.getTime() - initTime;
				if (delta < min) {
					min = delta;
				} else {
					if (delta > max) {
						max = delta;
					}
				}

			default:
				continue;
			}
		}
		
		 data.putResult(this, new MinMax(min, max));
	}

}
