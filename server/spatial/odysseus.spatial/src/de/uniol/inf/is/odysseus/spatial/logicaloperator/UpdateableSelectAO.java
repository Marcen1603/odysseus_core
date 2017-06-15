package de.uniol.inf.is.odysseus.spatial.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name = "UpdateableSelect", maxInputPorts = 1, minInputPorts = 1, doc = "A select which predicate can be updated with punctuations.", category = {
		LogicalOperatorCategory.PROCESSING })
public class UpdateableSelectAO extends SelectAO {

	private static final long serialVersionUID = -2310938474043068459L;
	
	

}
