package de.uniol.inf.is.odysseus.iql.qdl.types.impl.query;

import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;

public class DefaultQDLOperator extends AbstractQDLOperator {

	
	public DefaultQDLOperator(String operator) {
		super(operator);
	}
	
	public DefaultQDLOperator(String operator, IQDLOperator source) {
		super(operator, source);
	}
	
	public DefaultQDLOperator(String operator, IQDLOperator source1, IQDLOperator source2) {
		super(operator,source1,source2);
	}

}
