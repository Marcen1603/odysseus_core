package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

/**
 * abstract rule for the TimestampAO
 * 
 * @author MarcPreuschaft
 *
 * @param <T>
 */
public abstract class AbstractCApplicationTimestampPosGreaterEqualZeroRule<T extends TimestampAO> extends AbstractCOperatorRule<TimestampAO> {

	public AbstractCApplicationTimestampPosGreaterEqualZeroRule(String name) {
		super(name);
	}



	@Override
	public boolean isExecutable(TimestampAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

			if(!logicalOperator.isUsingSystemTime()){
			
				SDFSchema schema = logicalOperator.getInputSchema();
				int pos = schema.indexOf(logicalOperator.getStartTimestamp());
	
				if (Tuple.class.isAssignableFrom(logicalOperator.getInputSchema().getType())) {
					if (pos >= 0) {
							return true;
					}
				}
			}
		
			return false;
	}
}
