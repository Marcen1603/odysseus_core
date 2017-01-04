package de.uniol.inf.is.odysseus.admission.status;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public interface IAdmissionMonitor {
	
	public void addQuery(IPhysicalQuery query);
	
	public void removeQuery(IPhysicalQuery query);
	
	public void updateMeasurements();
	
	public ArrayList<IPhysicalQuery> getQuerysWithIncreasingTendency();
}
