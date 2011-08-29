package de.uniol.inf.is.odysseus.ac;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public interface IAdmissionControl {

	public boolean canStartQuery( IQuery query );
	public ICost getCost( IQuery query );
	public void updateEstimations();
	public ICost getActualCost();
	public ICost getMaximumCost();
		
	public Set<String> getRegisteredCostModels();
	public void selectCostModel( String name );
	public String getSelectedCostModel();
	
	public void addListener( IAdmissionListener listener );
	public void removeListener( IAdmissionListener listener );
	
	public List<IPossibleExecution> getPossibleExecutions();
}
