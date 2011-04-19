//package de.uniol.inf.is.odysseus.sparql.physicalops.interval.sort;
//
//import java.util.Collections;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.ITimeInterval;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.predicate.IPredicate;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.predicate.intervalbased.OverlapsPredicate;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.predicate.intervalbased.TotallyBeforePredicate;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.sweeparea.ISweepArea.Order;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.sweeparea.intervalbased.DefaultTISweepArea;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.AbstractPipe;
//import de.uniol.inf.is.odysseus.queryexecution.po.sparql.object.NodeList;
//import de.uniol.inf.is.odysseus.queryexecution.po.sparql.object.SparqlTimestampedSolutionComparator;
//import de.uniol.inf.is.odysseus.queryexecution.po.sparql.util.SPARQL_Util;
//import de.uniol.inf.is.odysseus.querytranslation.logicalops.AbstractLogicalOperator;
//import de.uniol.inf.is.odysseus.querytranslation.logicalops.SortAO;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
//
///**
// * This is an operator for OrderBy in an sparql query.
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// *
// */
//public class SortPO<M extends ITimeInterval> extends AbstractPipe<NodeList<M>,
//											NodeList<M>>{
//	private DefaultTISweepArea<NodeList<M>> sa;
//	
//	private LinkedList<NodeList<M>> nextElems;
//	
//	/**
//	 * this is for the ordering
//	 */
//	private SparqlTimestampedSolutionComparator ssc;
//	
//	/**
//	 * the logical counterpart for this operator
//	 */
//	private AbstractLogicalOperator ao;
//	
//	/**
//	 * ascending or descending ordering
//	 */
//	//private boolean ascending;
//	
//	@SuppressWarnings("unchecked")
//	public SortPO(AbstractLogicalOperator logical){
//		this.ao = logical;
//		this.sa = new DefaultTISweepArea<NodeList<M>>();
//		this.sa.setRemovePredicate(TotallyBeforePredicate.getInstance());
//		
//		// we only need an overlaps predicate
//		IPredicate p_query = OverlapsPredicate.getInstance();
//		this.sa.setQueryPredicate(p_query);
//		
//		// create the field of attribute indices
//		SDFAttributeList attToSort = ((SortAO)this.ao).getSortAttrib();
//		boolean[] direction = ((SortAO)this.ao).getAscending();
//		
//		int[] indices = new int[attToSort.size()];
//		//boolean[] ascending = new boolean[attToSort.size()];
//		
//		for(int i = 0; i<attToSort.size(); i++){
//			for(int u = 0; u<this.ao.getOutputSchema().size(); u++){
//				SDFAttribute a = attToSort.get(i);
//				SDFAttribute b = this.ao.getOutputSchema().get(u);
//				if(SPARQL_Util.refersSameVar(a, b)){
//					indices[i] = u;
//				}
//			}
//		}
//		
//		this.ssc = new SparqlTimestampedSolutionComparator(indices, direction);
//
//		
//		this.nextElems = new LinkedList<NodeList<M>>();
//	}
//	
//	public SortPO(SortPO original){
//		this.sa = original.sa;
//		this.ssc = original.ssc;
//		this.nextElems = original.nextElems;
//		this.ao = original.ao;
//	}
//	
//	@Override
//	public SortPO clone(){
//		return new SortPO(this);
//	}
//	
//	public synchronized void process_next(NodeList<M> next, int port, boolean isReadOnly) {
//		
//			if(isReadOnly){
//				next = (NodeList<M>)next.clone();
//			}
//			doIntervalBased(next);
//		
//		while(!this.nextElems.isEmpty()){
//			this.transfer(this.nextElems.removeFirst());
//		}
//	}
//	
//	private void  doIntervalBased(NodeList<M> next){
//		Iterator<NodeList<M>> iter = this.sa.extractElements(next, Order.RightLeft);
//		// create a list and sort this list
//		LinkedList<NodeList<M>> sortList =
//			new LinkedList<NodeList<M>>();
//		while(iter.hasNext()){
//			sortList.addLast(iter.next());
//		}
//		
//		Collections.sort(sortList, this.ssc);
//		
//		
//		this.nextElems = sortList;
//		
//		// add the new element to the sweep area
//		// for this the timeintervals have to be splitted
//		List<NodeList<M>> splittedNewElements = new LinkedList<NodeList<M>>();
//		splittedNewElements.add(next);
//		
//		List<NodeList<M>> splittedOldElements = new LinkedList<NodeList<M>>();
//		Iterator<NodeList<M>> qualifies = this.sa.query(next, Order.LeftRight);
//		while(qualifies.hasNext()){
//			splittedOldElements.add(qualifies.next());
//		}
//		
//		// remove the old elements from the sweep area
//		this.sa.removeAll(splittedOldElements);
//		
//		// split the elements
//		List<List<NodeList<M>>> result = 
//			new SPARQL_Util<M>().splitElements2(splittedNewElements, splittedOldElements);
//		
//		this.sa.insertAll(result.get(0));
//		this.sa.insertAll(result.get(1));
//	}
//	
//	
//	public void process_close(){
//	}
//}
