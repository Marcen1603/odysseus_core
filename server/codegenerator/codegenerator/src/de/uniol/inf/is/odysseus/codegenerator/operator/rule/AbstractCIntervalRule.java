package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

/**
 * abstract rule for the inputSchema -> ITimeInterval
 * 
 * @author MarcPreuschaft
 *
 * @param <T>
 */
public abstract class AbstractCIntervalRule<T extends ILogicalOperator>  extends AbstractCOperatorRule<T>{

	public AbstractCIntervalRule(String name) {
		super(name);
	}
	
	@Override
	public boolean isExecutable(T logicalOperator,
			TransformationConfiguration transformationConfiguration) {

			// ALL INPUT SCHEMAS need ITimeInterval			
			for (int i=0; i<logicalOperator.getNumberOfInputs();i++){
				SDFSchema s = logicalOperator.getInputSchema(i);
				if (!s.hasMetatype(ITimeInterval.class)){
					return false;
				}
			}
			return true;
			
	}

}
