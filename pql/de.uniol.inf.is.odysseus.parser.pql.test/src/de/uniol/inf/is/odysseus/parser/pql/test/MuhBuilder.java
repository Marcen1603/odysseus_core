package de.uniol.inf.is.odysseus.parser.pql.test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.parser.pql.IOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.pql.IParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class MuhBuilder implements IOperatorBuilder {

	@Override
	public ILogicalOperator createOperator(Map<String, Object> parameters,
			List<ILogicalOperator> inputOps) {
		MuhOperator op = new MuhOperator();
		int i = 0;
		for(ILogicalOperator curOp : inputOps) {
			op.subscribeToSource(curOp, i++,0, new SDFAttributeList());	
		}
		return op;
	}

	@Override
	public Set<IParameter<?>> getParameters() {
		return Collections.emptySet();
	}
	
	

}
