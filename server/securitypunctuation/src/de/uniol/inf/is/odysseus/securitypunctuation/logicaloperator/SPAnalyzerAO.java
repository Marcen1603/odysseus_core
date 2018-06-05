package de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
@LogicalOperator(name = "AnalyzeSP", minInputPorts = 1, maxInputPorts = 1, doc = "Merges similar SPs to one SP", url = "-", category = {
		LogicalOperatorCategory.BASE })
public class SPAnalyzerAO extends AbstractLogicalOperator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1172430186451664486L;

	
	public SPAnalyzerAO(){
		super();
	}
	
	public SPAnalyzerAO(SPAnalyzerAO spAnalyzerAO){
		super(spAnalyzerAO);
	}
	
	
	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return new SPAnalyzerAO(this);
	}

}
