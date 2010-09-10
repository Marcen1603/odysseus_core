package de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO;

import java.util.Set;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingAdvanceTimeWindowTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;

public class SlidingAdvanceTimeWindowTIPOTransformator implements
		IPOTransformator<WindowAO> {
	@Override
	public boolean canExecute(WindowAO windowAO,
			TransformationConfiguration config) {
		Set<String> metaTypes = config.getMetaTypes();
		WindowType windowType = windowAO.getWindowType();

		return (metaTypes
				.contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval") && (windowType == WindowType.JUMPING_TIME_WINDOW || windowType == WindowType.FIXED_TIME_WINDOW));
	}

	@SuppressWarnings("unchecked")
	@Override
	public TransformedPO transform(WindowAO windowAO,
			TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {
		SlidingAdvanceTimeWindowTIPO windowPO = new SlidingAdvanceTimeWindowTIPO(
				windowAO);
		windowPO.setOutputSchema(windowAO.getOutputSchema());

		TransformedPO po = new TransformedPO(windowPO);
		return po;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator(
				"SlidingAdvanceTimeWindowTIPO");
		return to;
	}
}
