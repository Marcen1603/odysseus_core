package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.util.List;

/**
 * A preprocessor object that calculates wether a open or read cycle blocked.
 * 
 * @author Jonas Jacobi
 */
public class BlockedProcessor implements IPreprocessorObject<Boolean> {
	private static final long serialVersionUID = -1938703872074841411L;

	private long openLimit;

	private long readLimit;

	/**
	 * Constructor
	 * 
	 * @param openLimit
	 *            the timelimit in milliseconds, after which a open is
	 *            considered blocked
	 * @param readLimit
	 *            the timelimit in milliseconds, after which a read is
	 *            considered blocked
	 */
	public BlockedProcessor(long openLimit, long readLimit) {
		this.openLimit = openLimit;
		this.readLimit = readLimit;
	}

	public void processData(List<POEventData> events, PreprocessedMetadata data) {
		long openInit = 0;
		long readInit = 0;
		for (POEventData curEvent : events) {
			switch (curEvent.getType()) {
			case OpenInit:
				openInit = curEvent.getTime();
				break;
			case OpenDone:
				if ((curEvent.getTime() - openInit) > openLimit) {
					data.putResult(this, true);
					return;
				}
			case ReadInit:
				readInit = curEvent.getTime();
				break;
			case ReadDone:
				if ((curEvent.getTime() - readInit) > readLimit) {
					data.putResult(this, true);
					return;
				}
			default:
				continue;
			}
		}

		data.putResult(this, false);
	}

}
