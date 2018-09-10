package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.elementcloning;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.util.AbstractGraphNodeVisitor;

public class StandardCloningVisitor extends AbstractGraphNodeVisitor<IPhysicalOperator, Object>{

	@SuppressWarnings("rawtypes")
	@Override
	public void nodeAction(IPhysicalOperator node) {
			
		if (node instanceof AbstractSource){
			AbstractSource source = (AbstractSource) node;
			
			@SuppressWarnings("unchecked")
			Collection<AbstractPhysicalSubscription> subs = source.getSubscriptions();
			
			// Count number of consumers on same port
			Map<Integer,Integer> connectionsOnPort = new HashMap<>();
			for(AbstractPhysicalSubscription s:subs){
				Integer currentConn = connectionsOnPort.get(s.getSourceOutPort());
				if (currentConn == null){
					connectionsOnPort.put(s.getSourceOutPort(), 1);
				}else{
					connectionsOnPort.put(s.getSourceOutPort(), currentConn+1);
				}
			}
			
			// Update cloning information
			for (AbstractPhysicalSubscription s:subs){
				boolean nc = false;
				if (node instanceof AbstractPipe){
					AbstractPipe pipe = (AbstractPipe) node;
					if (pipe.getOutputMode() == OutputMode.MODIFIED_INPUT){
						nc = true;
					}
				} 
				// Case where not AbstractPipe or where number of Consumers per
				// port is higher than 1
				if (nc == false){
					nc = connectionsOnPort.get(s.getSourceOutPort()) > 1;
				}
				s.setNeedsClone(nc);			
			}

		}
		
		super.nodeAction(node);
	}
	
}
