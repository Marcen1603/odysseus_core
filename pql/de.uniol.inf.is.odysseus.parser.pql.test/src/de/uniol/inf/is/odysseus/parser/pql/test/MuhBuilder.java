package de.uniol.inf.is.odysseus.parser.pql.test;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.parser.pql.IOperatorBuilder;

public class MuhBuilder implements IOperatorBuilder {

	@Override
	public ILogicalOperator createOperator(Map<String, Object> parameters,
			List<ILogicalOperator> inputOps) {
		MuhOperator op = new MuhOperator();
		int i = 0;
		for(ILogicalOperator curOp : inputOps) {
			op.subscribeTo(curOp, i++,0);	
		}
		return op;
	}

}
