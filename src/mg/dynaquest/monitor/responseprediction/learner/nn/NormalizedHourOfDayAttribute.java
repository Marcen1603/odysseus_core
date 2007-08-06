package mg.dynaquest.monitor.responseprediction.learner.nn;

import java.util.Calendar;

import mg.dynaquest.monitor.responseprediction.learner.Attribute;
import mg.dynaquest.monitor.responseprediction.preprocessor.PreprocessedMetadata;


/**
 * Attribute which normalizes the hours of a day (0-24) between -1..1
 * needed for the {@link NeuralNetLearner}
 * @author Jonas Jacobi
 */
public class NormalizedHourOfDayAttribute extends Attribute<Integer, Double> {

	private Calendar calendar;

	public NormalizedHourOfDayAttribute() {
		super(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
				19, 20, 21, 22, 23);
		this.calendar = (Calendar) Calendar.getInstance().clone();
	}

	@Override
	public Double classify(PreprocessedMetadata d) {
		this.calendar.setTimeInMillis(d.getStartTime());
		int h = this.calendar.get(Calendar.HOUR_OF_DAY) + 1;
		
		return (h - 12.5d)/11.5d;
	}

}
