//package de.uniol.inf.is.odysseus.sparql.physicalops.object.predicate;
//
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.predicate.AbstractPredicate;
//import de.uniol.inf.is.odysseus.queryexecution.po.sparql.object.NodeList;
//
//public class NodeListEqualPredicate extends AbstractPredicate<NodeList>{
//
//	public static final NodeListEqualPredicate instance = new NodeListEqualPredicate();
//	
//	public boolean evaluate(NodeList elem){
//		throw new UnsupportedOperationException();
//	}
//	
//	public boolean evaluate(NodeList left, NodeList right){
//		return left.equals(right);
//	}
//	
//	public NodeListEqualPredicate clone(){
//		return instance;
//	}
//	
//	public static NodeListEqualPredicate getInstance(){
//		return instance;
//	}
//}
