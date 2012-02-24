package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.interval.object.predicates;

import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.TimeInterval;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.predicate.NodeListEqualPredicate;

public class NodeListEqualTIPredicate extends AbstractPredicate<NodeList<ITimeInterval>>{

	private static final NodeListEqualTIPredicate instance = new NodeListEqualTIPredicate();
	
	public boolean evaluate(NodeList<ITimeInterval> elem){
		throw new UnsupportedOperationException();
	}
	
	public boolean evaluate(NodeList<ITimeInterval> left, NodeList<ITimeInterval> right){
		return TimeInterval.overlaps(left.getMetadata(), right.getMetadata()) ? NodeListEqualPredicate.getInstance().evaluate(left, right) : false;
	}
	
	public NodeListEqualTIPredicate clone(){
		return instance;
	}
	
	public static NodeListEqualTIPredicate getInstance(){
		return instance;
	}
}
