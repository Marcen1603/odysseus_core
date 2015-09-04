package de.uniol.inf.is.odysseus.iql.qdl.types.impl.query;

import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;

public class DefaultQDLOperator extends AbstractQDLOperator {

	
	public DefaultQDLOperator(String name) {
		super(name);
	}
	
	public DefaultQDLOperator(String name, IQDLOperator source) {
		super(name, source);
	}
	
	public DefaultQDLOperator(String name, IQDLOperator source1, IQDLOperator source2) {
		super(name,source1,source2);
	}



}
