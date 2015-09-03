package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.elementcloning.testalgorithm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.elementcloning.testalgorithm.TestOperatorData.State;
import de.uniol.inf.is.odysseus.core.util.AbstractExtendedGraphNodeVisitor;

public class TestCloningVisitor extends AbstractExtendedGraphNodeVisitor<IPhysicalOperator, Object>
{
	private final TestElementCloningUpdater updater;
	private IPhysicalOperator lastSink;
	
	public TestCloningVisitor(TestElementCloningUpdater updater) 
	{
		this.updater = updater;
	}
	
	public void beforeFromSinkToSourceAction(IPhysicalOperator sink, IPhysicalOperator source)
	{
		lastSink = sink;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public NodeActionResult nodeAction(IPhysicalOperator node) 
	{
		if (!(node instanceof AbstractSource))
			return super.nodeAction(node);

		System.out.println("visit " + node.getName());
				
		TestOperatorData operatorData = updater.operatorDataMap.get(node);
				
		AbstractSource source = (AbstractSource) node;
		AbstractPipe pipe = null;

		@SuppressWarnings("unchecked")
		Collection<AbstractPhysicalSubscription> subscriptions = source.getSubscriptions();
		OutputMode outputMode = null;
		
		// Get out port for node->sink
		int outPort;		
		if (lastSink != null)
		{
			outPort = -1;
			for (AbstractPhysicalSubscription s : subscriptions)
			{
				if (s.getTarget() == lastSink)
				{
					outPort = s.getSourceOutPort();
					break;
				}
			}
			if (outPort == -1)
				throw new IllegalStateException(lastSink.getName() + " is no sink for " + node.getName());
			
			// Get output mode for port
			outputMode = OutputMode.NEW_ELEMENT;		
			if (node instanceof AbstractPipe)
			{
				pipe = (AbstractPipe) node;
				outputMode = pipe.getOutputMode(outPort);
			}			
		}
		else
			outPort = 0;
		
		State newState = State.UNINITIALIZED;
		State currentState = operatorData.states.get(outPort);
		if (currentState == null)
		{
			currentState = State.UNINITIALIZED;
			operatorData.states.put(outPort, currentState);
		}		
			
		// Count number of consumers on port
		int connectionsOnPort = 0;
		for(AbstractPhysicalSubscription s:subscriptions)
		{
			if (s.getSourceOutPort() == outPort)
				connectionsOnPort++;
		}
		
		
			
		// Update cloning information
/*		for (AbstractPhysicalSubscription s:subs){
			boolean nc = false;
			if (pipe != null)
			{
				if (pipe.getOutputMode() == OutputMode.MODIFIED_INPUT)
					nc = true;
			} 
			// Case where not AbstractPipe or where number of Consumers per
			// port is higher than 1
			if (nc == false){
				nc = connectionsOnPort.get(s.getSourceOutPort()) > 1;
			}
			s.setNeedsClone(nc);			
		}*/
		
		return super.nodeAction(node);
	}
	
}
