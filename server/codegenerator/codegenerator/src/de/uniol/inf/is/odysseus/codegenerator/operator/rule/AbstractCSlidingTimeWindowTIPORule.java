package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

/**
 * abstract rule for the TimeWindowAO (SlidingTimeWindow)
 * 
 * @author MarcPreuschaft
 *
 * @param <T>
 */
public abstract class AbstractCSlidingTimeWindowTIPORule<T extends TimeWindowAO> extends AbstractCOperatorRule<TimeWindowAO> {

	public AbstractCSlidingTimeWindowTIPORule(String name) {
		super(name);
	}


	@Override
	public boolean isExecutable(TimeWindowAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		if (logicalOperator.getInputSchema().hasMetatype(ITimeInterval.class)){
			switch (logicalOperator.getWindowType()) {
			case TIME:
				if (logicalOperator.getWindowSlide() == null
						&& logicalOperator.getWindowAdvance() == null) {
					return true;
				}
				return false;
			default:
				return false;
			}

		}
		return false;

	}



}
