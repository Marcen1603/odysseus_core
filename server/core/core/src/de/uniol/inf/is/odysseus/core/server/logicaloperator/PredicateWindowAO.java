package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "PREDICATEWINDOW", category = { LogicalOperatorCategory.BASE }, doc = "This is an predicated based window, set start and end condition with predicates.", url = "http://odysseus.offis.uni-oldenburg.de:8090/display/ODYSSEUS/PredicateWindow", hidden = true)
public class PredicateWindowAO extends AbstractWindowAO {

	private static final long serialVersionUID = 8834015972527486443L;

	public PredicateWindowAO(AbstractWindowAO windowPO) {
		super(windowPO);
	}

	public PredicateWindowAO() {
		super(WindowType.PREDICATE);
	}
	
	@Override
	@Parameter(type = PredicateParameter.class, name = "START", optional = true)
	public void setStartCondition(IPredicate<?> startCondition) {
		super.setStartCondition(startCondition);
	}

	@Override
	@Parameter(type = PredicateParameter.class, name = "END", optional = true)
	public void setEndCondition(IPredicate<?> endCondition) {
		super.setEndCondition(endCondition);
	}
	
	@Override
	@Parameter(type = BooleanParameter.class, name = "SameStartTime", optional = true)
	public void setSameStarttime(boolean sameStarttime) {
		super.setSameStarttime(sameStarttime);
	}
	
	@Override
	public PredicateWindowAO clone() {
		return new PredicateWindowAO(this);
	}

	
}
