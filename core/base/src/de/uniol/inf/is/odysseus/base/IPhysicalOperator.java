package de.uniol.inf.is.odysseus.base;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder, Jonas Jacobi
 */
public interface IPhysicalOperator extends IOwnedOperator,
		IMonitoringDataProvider, IClone {

	boolean isSource();

	boolean isSink();

	boolean isPipe();
	
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
	public void setOutputSchema(SDFAttributeList outputSchema);
	
	public IPhysicalOperator clone();
	
	public boolean isOpen();
}
