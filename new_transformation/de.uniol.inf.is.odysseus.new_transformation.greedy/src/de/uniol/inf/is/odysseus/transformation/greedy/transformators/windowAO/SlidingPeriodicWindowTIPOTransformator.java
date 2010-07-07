package de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO;

import java.util.Set;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingPeriodicWindowTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataUpdatePO;

public class SlidingPeriodicWindowTIPOTransformator implements
		IPOTransformator<WindowAO> {
	@Override
	public boolean canExecute(WindowAO windowAO,
			TransformationConfiguration config) {
		Set<String> metaTypes = config.getMetaTypes();
		WindowType windowType = windowAO.getWindowType();

		return (metaTypes
				.contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"))
				&& (windowType == WindowType.PERIODIC_TIME_WINDOW || windowType == WindowType.PERIODIC_TUPLE_WINDOW);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TransformedPO transform(WindowAO windowAO,
			TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {
		SlidingPeriodicWindowTIPO windowPO = new SlidingPeriodicWindowTIPO(
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
				"SlidingPeriodicWindowTIPO");
		return to;
	}
}
