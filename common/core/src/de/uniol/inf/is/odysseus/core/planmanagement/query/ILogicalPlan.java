package de.uniol.inf.is.odysseus.core.planmanagement.query;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface ILogicalPlan extends Serializable{
	ILogicalOperator getRoot();
	
	List<ILogicalOperator> getOperators();
	List<ILogicalOperator> getSources();
	List<ILogicalOperator> getSinks();
	
	Set<ILogicalOperator> findOpsFromType(Class<? extends ILogicalOperator> toFind);
	Set<ILogicalOperator> findOpsFromType(Set<Class<? extends ILogicalOperator>> toFind, boolean checkAssignabale);

	SDFSchema getOutputSchema();

	ILogicalPlan copyPlan();

	void removeOwner();
	void removePhysicalSubscriptions();

	void setOwner(IOperatorOwner owner);

	String getPlanAsString(boolean detailed);


}
