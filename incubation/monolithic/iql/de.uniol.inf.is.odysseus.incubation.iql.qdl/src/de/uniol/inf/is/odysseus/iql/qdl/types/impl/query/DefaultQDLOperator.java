package de.uniol.inf.is.odysseus.iql.qdl.types.impl.query;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;

public class DefaultQDLOperator<T extends ILogicalOperator> extends AbstractQDLOperator<T> {

	
	public DefaultQDLOperator(T operator) {
		super(operator);
	}
	
	public DefaultQDLOperator(T operator, IQDLOperator<?> source) {
		super(operator, source);
	}
	
	public DefaultQDLOperator(T operator, IQDLOperator<?> source1, IQDLOperator<?> source2) {
		super(operator,source1,source2);
	}

}
