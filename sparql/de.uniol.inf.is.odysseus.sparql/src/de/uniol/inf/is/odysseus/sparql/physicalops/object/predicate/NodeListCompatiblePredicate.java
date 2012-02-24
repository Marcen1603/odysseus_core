//package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.object.predicate;
//
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.IClone;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.predicate.AbstractPredicate;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.util.SPARQL_Util;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFAttribute;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFSchema;
//
//public class NodeListCompatiblePredicate<T extends IClone> extends AbstractPredicate<NodeList<T>>{
//
//	private SDFSchema leftSchema;
//	private SDFSchema rightSchema;
//	
//	public NodeListCompatiblePredicate(SDFSchema leftSchema, SDFSchema rightSchema){
//		this.leftSchema = leftSchema;
//		this.rightSchema = rightSchema;
//	}
//	
//	public NodeListCompatiblePredicate(NodeListCompatiblePredicate<T> old){
//		this.leftSchema = old.leftSchema;
//		this.rightSchema = old.rightSchema;
//	}
//	
//	public NodeListCompatiblePredicate<T> clone(){
//		return new NodeListCompatiblePredicate<T>(this);
//	}
//	
//	public boolean evaluate(NodeList<T> elem){
//		throw new UnsupportedOperationException();
//	}
//	
//	public boolean evaluate(NodeList<T> left, NodeList<T> right){
//		for(int i = 0; i<this.leftSchema.size(); i++){
//			for(int u = 0; u<this.rightSchema.size(); u++){
//				SDFAttribute la = this.leftSchema.get(i);
//				SDFAttribute ra = this.rightSchema.get(u);
//				if(SPARQL_Util.refersSameVar(la, ra) &&
//						!(left.get(i).equals(right.get(u)) || 
//								left.get(i).equals(SPARQL_Util.EXTRA_ATTRIBUTE_NODE) ||
//								right.get(u).equals(SPARQL_Util.EXTRA_ATTRIBUTE_NODE))){
//					return false;
//				}
//					
//						
//			}
//		}
//		return true;
//	}
//}
