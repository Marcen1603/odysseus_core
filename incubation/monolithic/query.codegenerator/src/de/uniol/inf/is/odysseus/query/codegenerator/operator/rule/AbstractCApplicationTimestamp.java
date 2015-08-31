package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public abstract class AbstractCApplicationTimestamp<T extends TimestampAO> extends AbstractRule<TimestampAO> {

	public AbstractCApplicationTimestamp(String name) {
		super(name);
	}

	public boolean isExecutable(TimestampAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		if (logicalOperator instanceof TimestampAO) {
			TimestampAO timestampAO = (TimestampAO) logicalOperator;
			
			if(!timestampAO.isUsingSystemTime()){
			
				SDFSchema schema = timestampAO.getInputSchema();
				int pos = schema.indexOf(timestampAO.getStartTimestamp());
	
				if (Tuple.class.isAssignableFrom(timestampAO.getInputSchema().getType())) {
					if (pos >= 0) {
							return false;
				
					}
					return true;
				}
			}
	
			return false;

		}
		return false;	
	}



}
