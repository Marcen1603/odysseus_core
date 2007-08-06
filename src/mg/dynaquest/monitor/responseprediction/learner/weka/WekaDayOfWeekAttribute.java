package mg.dynaquest.monitor.responseprediction.learner.weka;

import java.util.Calendar;

import mg.dynaquest.monitor.responseprediction.preprocessor.PreprocessedMetadata;


/**
 * Attribute for the day of the week (1=monday,...,7=sunday)
 * @author Jonas Jacobi
 */
public class WekaDayOfWeekAttribute extends WekaAttribute<Integer> {
	
	Calendar calendar;

	public WekaDayOfWeekAttribute() {
		super(1,2,3,4,5,6,7);
		calendar = (Calendar) Calendar.getInstance().clone();
	}

	@Override
	public String classify(PreprocessedMetadata d) {
		calendar.setTimeInMillis(d.getStartTime());
		return String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
	}

}
