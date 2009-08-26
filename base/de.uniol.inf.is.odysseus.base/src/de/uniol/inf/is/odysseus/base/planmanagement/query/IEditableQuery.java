package de.uniol.inf.is.odysseus.base.planmanagement.query;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.planmanagement.IOperatorControl;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.QueryBuildParameter;

public interface IEditableQuery extends IOperatorControl, IQuery {
	public void stop();
	
	public void start();

	public void setLogicalPlan(ILogicalOperator newLogicalAlgebra);

	public ILogicalOperator getLogicalPlan();

	public void initializePhysicalPlan(IPhysicalOperator physicalChilds) throws OpenFailedException;

	public IPhysicalOperator setRoot(IPhysicalOperator root) throws OpenFailedException;

	public IPhysicalOperator getRoot();
	
	public void setPhysicalChilds(ArrayList<IPhysicalOperator> physicalChilds);

	public ArrayList<IPhysicalOperator> getIntialPhysicalPlan();

	public void removeOwnerschip();

	public QueryBuildParameter getBuildParameter();
}
