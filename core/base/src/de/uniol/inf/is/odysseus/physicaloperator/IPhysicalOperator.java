package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.event.IEventHandler;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder, Jonas Jacobi
 */
public interface IPhysicalOperator extends IOwnedOperator,
		IMonitoringDataProvider, IEventHandler, IClone {

	boolean isSource();

	boolean isSink();

	boolean isPipe();
	
	boolean isSemanticallyEqual(IPhysicalOperator ipo);
	
	/**
	 * Name for Operator (Visual Identification) 
	 */
	public String getName();
	public void setName(String name);
	
	/**
	 * Schemarelated methods
	 * @return
	 */
	public SDFAttributeList getOutputSchema();
	public SDFAttributeList getOutputSchema(int port);
	public void setOutputSchema(SDFAttributeList outputSchema);
	public void setOutputSchema(SDFAttributeList outputSchema, int port);
	
	@Override
	public IPhysicalOperator clone();
	
	public boolean isOpen();
}
