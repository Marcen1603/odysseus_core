package de.uniol.inf.is.odysseus.query.transformation.operator.java;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;


public interface ITransformationOperator {
	
	public String getName();
	
	public String getProgramLanguage();
	
	public String getCode(ILogicalOperator operator);
	

}
