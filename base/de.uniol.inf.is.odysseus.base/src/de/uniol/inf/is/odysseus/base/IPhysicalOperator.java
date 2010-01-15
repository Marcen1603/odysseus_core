package de.uniol.inf.is.odysseus.base;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder, Jonas Jacobi
 */
public interface IPhysicalOperator extends IOwnedOperator,
		IMonitoringDataProvider {

	boolean isSource();

	boolean isSink();

	boolean isPipe();

	public void open() throws OpenFailedException;

	/**
	 * stop the operator and free all its resources
	 */
	public void close();

	public void subscribe(POEventListener listener, POEventType type);

	public void unsubscribe(POEventListener listener, POEventType type);

	public void subscribeToAll(POEventListener listener);

	public void unSubscribeFromAll(POEventListener listener);
	
	/**
	 * Name for Operator (Visual Identification) 
	 */
	public String getName();
	public void setName(String name);
	public SDFAttributeList getOutputSchema();
	public void setOutputSchema(SDFAttributeList outputSchema);
}
