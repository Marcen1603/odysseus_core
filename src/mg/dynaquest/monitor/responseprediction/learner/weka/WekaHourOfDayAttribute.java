package mg.dynaquest.monitor.responseprediction.learner.weka;

import java.util.Calendar;

import mg.dynaquest.monitor.responseprediction.preprocessor.PreprocessedMetadata;


/**
 * Attribute for the hour of the day (0-24)
 * @author Jonas Jacobi
 */
public class WekaHourOfDayAttribute extends WekaAttribute<Integer> {

	Calendar calendar;

	public WekaHourOfDayAttribute() {
		super(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
				19, 20, 21, 22, 23);
		calendar = (Calendar) Calendar.getInstance().clone();
	}

	@Override
	public String classify(PreprocessedMetadata d) {
		calendar.setTimeInMillis(d.getStartTime());
		return String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
	}

}
