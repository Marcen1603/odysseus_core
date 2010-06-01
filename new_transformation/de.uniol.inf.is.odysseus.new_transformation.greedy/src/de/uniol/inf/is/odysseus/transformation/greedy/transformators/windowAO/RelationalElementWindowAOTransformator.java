package de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalSlidingElementWindowTIPO;

public class RelationalElementWindowAOTransformator implements IPOTransformator<WindowAO> {

	@Override
	public boolean canExecute(WindowAO windowAO, TransformationConfiguration config) {
		boolean hasMetaType = config.getMetaTypes().contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval");
		boolean isCorrectWindowAO = (windowAO.getWindowType() == WindowType.SLIDING_TUPLE_WINDOW || windowAO
				.getWindowType() == WindowType.JUMPING_TUPLE_WINDOW)
				&& windowAO.isPartitioned();
		return hasMetaType && isCorrectWindowAO;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public TransformedPO transform(WindowAO windowAO, TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {

		RelationalSlidingElementWindowTIPO windowPO = new RelationalSlidingElementWindowTIPO(windowAO);
		windowPO.setOutputSchema(windowAO.getOutputSchema());

		return new TransformedPO(windowPO);
	}

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator("RelationalSlidingElementWindowTIPO");
		return to;
	}
}
