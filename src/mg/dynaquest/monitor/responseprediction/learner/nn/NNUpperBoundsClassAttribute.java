package mg.dynaquest.monitor.responseprediction.learner.nn;

import mg.dynaquest.monitor.responseprediction.learner.Attribute;
import mg.dynaquest.monitor.responseprediction.preprocessor.IPreprocessorObject;
import mg.dynaquest.monitor.responseprediction.preprocessor.PreprocessedMetadata;

public class NNUpperBoundsClassAttribute extends Attribute<Integer, Integer> {
	private IPreprocessorObject<? extends Number> ppo;

	public NNUpperBoundsClassAttribute(
			IPreprocessorObject<? extends Number> ppo, Integer... values) {
		super(values);
		this.ppo = ppo;
	}

	@Override
	public Integer classify(PreprocessedMetadata d) {
		Number value = d.getResult(this.ppo);
		for (int i = 0; i < getClassValues().length; ++i) {
			if (value.intValue() <= getClassValues()[i]) {
				return i;
			}
		}
		throw new RuntimeException("Value '" + value.toString()
				+ "' is out of bounds");
	}

}
