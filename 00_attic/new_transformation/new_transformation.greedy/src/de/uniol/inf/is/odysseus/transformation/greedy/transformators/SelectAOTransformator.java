package de.uniol.inf.is.odysseus.transformation.greedy.transformators;

import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;

public class SelectAOTransformator implements IPOTransformator<SelectAO> {
	@Override
	public boolean canExecute(SelectAO logicalOperator,
			TransformationConfiguration config) {
		return true;
	}

	@Override
	public TransformedPO transform(SelectAO selectAO,
			TransformationConfiguration config, ITransformation transformation) {
		SelectPO<?> selectPO = null;
		selectPO = new SelectPO(selectAO.getPredicate());
		selectPO.setOutputSchema(selectAO.getOutputSchema());
		return new TransformedPO(selectPO);
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator(
				"SelectPO");
		return to;
	}
}
