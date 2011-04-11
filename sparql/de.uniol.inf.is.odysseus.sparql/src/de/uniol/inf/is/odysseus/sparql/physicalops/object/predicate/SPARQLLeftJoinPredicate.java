//package de.uniol.inf.is.odysseus.sparql.physicalops.object.predicate;
//
//import de.uniol.inf.is.odysseus.queryexecution.po.base.join.object.merge.LeftMergeFunction;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.IClone;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.predicate.AndPredicate;
//import de.uniol.inf.is.odysseus.queryexecution.po.sparql.interval.join.object.NodeListLeftMergeFunction;
//import de.uniol.inf.is.odysseus.queryexecution.po.sparql.object.NodeList;
//
//public class SPARQLLeftJoinPredicate<M extends IClone> extends AndPredicate<NodeList<M>> {
//	
//	LeftMergeFunction<NodeList<M>> dataMerge;
//	
//	public SPARQLLeftJoinPredicate(NodeListCompatiblePredicate<M> compatible, SPARQLFilterPredicate<M> filter, NodeListLeftMergeFunction<M> dataMerge){
//		super(compatible, filter);
//		this.dataMerge = dataMerge;
//	}
//	
//	public boolean evaluate(NodeList<M> left, NodeList<M> right) {
//		return getLeft().evaluate(left, right) && 
//				this.getRight().evaluate(this.dataMerge.merge(left, right));
//	}
//}
