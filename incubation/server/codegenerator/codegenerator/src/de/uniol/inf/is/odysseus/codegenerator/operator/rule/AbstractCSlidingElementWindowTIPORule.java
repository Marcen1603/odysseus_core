package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public abstract class AbstractCSlidingElementWindowTIPORule<T extends ElementWindowAO> extends
AbstractCOperatorRule<ElementWindowAO>{
	
	public AbstractCSlidingElementWindowTIPORule(String name) {
		super(name);
	}


	@Override
	public boolean isExecutable(ElementWindowAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		if (logicalOperator.getInputSchema().hasMetatype(ITimeInterval.class)){
			
				switch (logicalOperator.getWindowType()) {
				case TUPLE:
					return true;
				default:
					return false;
				}

		}
		return false;
	
	}
	
}
