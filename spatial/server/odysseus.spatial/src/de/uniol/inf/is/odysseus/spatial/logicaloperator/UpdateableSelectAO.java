package de.uniol.inf.is.odysseus.spatial.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.logicaloperator.IParallelizableOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

@LogicalOperator(name = "UpdateableSelect", maxInputPorts = 1, minInputPorts = 1, doc = "A select which predicate can be updated with punctuations.", category = {
		LogicalOperatorCategory.PROCESSING })
public class UpdateableSelectAO extends UnaryLogicalOp implements IHasPredicate, IParallelizableOperator {

	private static final long serialVersionUID = -2310938474043068459L;

	private IPredicate<?> predicate;

	public UpdateableSelectAO() {
	}

	public UpdateableSelectAO(UpdateableSelectAO ao) {
		this.predicate = ao.predicate.clone();
	}

	public UpdateableSelectAO(IPredicate<?> predicate) {
		setPredicate(predicate);
	}

	@SuppressWarnings("rawtypes")
	@Parameter(type = PredicateParameter.class)
	public void setPredicate(IPredicate predicate) {
		this.predicate = predicate;

	}

	@Override
	public IPredicate<?> getPredicate() {
		return predicate;
	}

	@Override
	public UpdateableSelectAO clone() {
		return new UpdateableSelectAO(this);
	}

	@Override
	public String toString() {
		return super.toString() + getPredicate();
	}

	@Override
	public OperatorStateType getStateType() {
		// FIXME check if predicate is stateful
		return IOperatorState.OperatorStateType.STATELESS;
	}

	@Override
	public boolean isParallelizable() {
		return true;
	}

}