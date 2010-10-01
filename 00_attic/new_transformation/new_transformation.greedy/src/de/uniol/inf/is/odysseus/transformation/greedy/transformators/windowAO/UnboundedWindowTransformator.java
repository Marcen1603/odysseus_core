package de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO;

import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;

public class UnboundedWindowTransformator implements IPOTransformator<WindowAO> {

	@Override
	public boolean canExecute(WindowAO windowAO, TransformationConfiguration config) {
		return config.getMetaTypes().contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval")
				&& windowAO.getWindowType() == WindowType.UNBOUNDED;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public TransformedPO transform(WindowAO logicalOperator, TransformationConfiguration config,
			ITransformation transformation) throws TransformationException {
		// dieser Transformator gibt null zurueck, da ein unbounded window aus
		// dem Plan geloescht werden soll
		return null;
	}

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator("UnboundedWindow");
		return to;
	}

}
