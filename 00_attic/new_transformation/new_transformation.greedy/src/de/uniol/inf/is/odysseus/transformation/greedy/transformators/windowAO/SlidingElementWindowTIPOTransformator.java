package de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO;

import java.util.Set;

import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingElementWindowTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;

public class SlidingElementWindowTIPOTransformator implements
		IPOTransformator<WindowAO> {
	@Override
	public boolean canExecute(WindowAO windowAO,
			TransformationConfiguration config) {
		Set<String> metaTypes = config.getMetaTypes();
		WindowType windowType = windowAO.getWindowType();

		return (metaTypes
				.contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"))
				&& (windowType == WindowType.SLIDING_TUPLE_WINDOW || windowType == WindowType.JUMPING_TUPLE_WINDOW)
				&& !windowAO.isPartitioned();
	}

	@SuppressWarnings("unchecked")
	@Override
	public TransformedPO transform(WindowAO windowAO,
			TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {
		SlidingElementWindowTIPO windowPO = new SlidingElementWindowTIPO(
				windowAO);
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
				"SlidingElementWindowTIPO");
		return to;
	}
}
