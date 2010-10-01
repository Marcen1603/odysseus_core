package de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO;

import java.util.Set;

import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingTimeWindowTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;

public class SlidingTimeWindowTIPOTransformator implements
		IPOTransformator<WindowAO> {
	@Override
	public boolean canExecute(WindowAO windowAO,
			TransformationConfiguration config) {
		Set<String> metaTypes = config.getMetaTypes();
		WindowType windowType = windowAO.getWindowType();

		return (metaTypes
				.contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"))
				&& (windowType == WindowType.SLIDING_TIME_WINDOW);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TransformedPO transform(WindowAO windowAO,
			TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {
		SlidingTimeWindowTIPO windowPO = new SlidingTimeWindowTIPO(windowAO);
		windowPO.setOutputSchema(windowAO.getOutputSchema());
		return new TransformedPO(windowPO);
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator(
				"SlidingTimeWindowTIPO");
		return to;
	}
}
