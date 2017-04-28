package de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
@LogicalOperator(name = "SECURITYSHIELD", minInputPorts = 1, maxInputPorts = 1, doc = "Filters Tuples and SPs based on the role of the query specifier", url = "", category = {
		LogicalOperatorCategory.BASE })
public class SecurityShieldAO extends AbstractLogicalOperator{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1339290034962641333L;

	public SecurityShieldAO(){
		super();
	}
	
	public SecurityShieldAO(SecurityShieldAO securityShieldAO){
		super(securityShieldAO);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new SecurityShieldAO(this);
	}

}
