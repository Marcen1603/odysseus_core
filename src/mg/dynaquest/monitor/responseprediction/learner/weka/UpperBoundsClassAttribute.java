package mg.dynaquest.monitor.responseprediction.learner.weka;

import mg.dynaquest.monitor.responseprediction.preprocessor.IPreprocessorObject;
import mg.dynaquest.monitor.responseprediction.preprocessor.PreprocessedMetadata;


/**
 * Attribute 
 * @author Jonas Jacobi
 */
public class UpperBoundsClassAttribute extends WekaAttribute<Integer> {

	private IPreprocessorObject<? extends Number> object;

	public UpperBoundsClassAttribute(IPreprocessorObject<? extends Number> object, Integer... classValues) {
		super(classValues);
		this.object = object;
	}

	@Override
	public String classify(PreprocessedMetadata d) {
		Integer[] bounds = getClassValues();
		Number value = d.getResult(this.object);
		for(int i = 0; i < bounds.length; ++i) {
			if (value.intValue() <= bounds[i]) {
				return bounds[i].toString();
			}
		}
		throw new RuntimeException("Value '" + value.toString() + "' is out of bounds");
	}
}
