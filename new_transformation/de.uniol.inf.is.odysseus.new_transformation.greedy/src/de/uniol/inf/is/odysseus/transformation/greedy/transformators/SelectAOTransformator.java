package de.uniol.inf.is.odysseus.transformation.greedy.transformators;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.SelectPO;

public class SelectAOTransformator implements IPOTransformator<SelectAO> {
	@Override
	public boolean canExecute(SelectAO logicalOperator, TransformationConfiguration config) {
		return true;
	}

	@Override
	public TransformedPO transform(SelectAO selectAO, TransformationConfiguration config,
			ITransformation transformation) {
		SelectPO<?> selectPO = new SelectPO(selectAO.getPredicate());
		selectPO.setOutputSchema(selectAO.getOutputSchema());
		return new TransformedPO(selectPO);
	}

	@Override
	public int getPriority() {
		return 0;
	}
}
