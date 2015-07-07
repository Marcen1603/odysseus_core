package de.uniol.inf.is.odysseus.query.transformation.operator;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;



public interface IOperator {
	
	public String getName();
	
	public String getTargetPlatform();
	
	public String getCode(ILogicalOperator operator);
	
	public Set<String> getNeededImports();
	
	

}
