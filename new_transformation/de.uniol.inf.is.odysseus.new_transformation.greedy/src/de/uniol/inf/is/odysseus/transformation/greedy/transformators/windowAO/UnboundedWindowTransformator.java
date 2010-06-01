package de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;

public class UnboundedWindowTransformator implements IPOTransformator<WindowAO> {

	@Override
	public boolean canExecute(WindowAO windowAO, TransformationConfiguration config) {
		return config.getMetaTypes().contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval")
				&& windowAO.getWindowType() == WindowType.UNBOUNDED && windowAO.getWindowOn() == null;
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
