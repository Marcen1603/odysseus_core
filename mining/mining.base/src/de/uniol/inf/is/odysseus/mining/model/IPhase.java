package de.uniol.inf.is.odysseus.mining.model;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;

public interface IPhase {

	public Collection<? extends ILogicalOperator> buildLogicalPlan();

}
