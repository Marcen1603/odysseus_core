package de.uniol.inf.is.odysseus.base;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder, Jonas Jacobi
 */
public interface IPhysicalOperator extends IOwnedOperator,
		IMonitoringDataProvider, IClone {

	boolean isSource();

	boolean isSink();

	boolean isPipe();

//	/**
//	 * Initialize operator and propagates this down the tree
//	 * @param o father who called open
//	 * @throws OpenFailedException
//	 */
//	public void open(IPhysicalOperator o) throws OpenFailedException;
//
//	/**
//	 * stop the operator and free all its resources
//	 */
//	public void close(IPhysicalOperator o);

	public void subscribe(IPOEventListener listener, POEventType type);

	public void unsubscribe(IPOEventListener listener, POEventType type);

	public void subscribeToAll(IPOEventListener listener);

	public void unSubscribeFromAll(IPOEventListener listener);
	
	/**
	 * Name for Operator (Visual Identification) 
	 */
	public String getName();
	public void setName(String name);
	public SDFAttributeList getOutputSchema();
	public void setOutputSchema(SDFAttributeList outputSchema);
	
	public IPhysicalOperator clone();
}
