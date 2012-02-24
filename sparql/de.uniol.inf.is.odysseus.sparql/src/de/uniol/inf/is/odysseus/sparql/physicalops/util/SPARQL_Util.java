//package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.util;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.hp.hpl.jena.graph.Node;
//import com.hp.hpl.jena.graph.Triple;
//
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.ITimeInterval;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.MetaTriple;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFAttribute;
//
///**
// * This class provides some utility methods, that will be used 
// * in different classed for sparql stream processing.
// * 
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// *
// */
//public class SPARQL_Util<M extends ITimeInterval> {
//
//	/**
//	 * This value indicates, that the attribute that owns this
//	 * value is not in the sparql solution. The attribute has
//	 * only been added, to be able to tell the next operator after
//	 * a union operator, in which order the attributes in the sparql
//	 * solution occur.
//	 */
//	public static final String EXTRA_ATTRIBUTE = "extra";
//	
//	public static final Node EXTRA_ATTRIBUTE_NODE = Node.create(EXTRA_ATTRIBUTE);
//	
//	/**
//	 * This string indicates that a variable is unbound. This will
//	 * be used to be able to use the predicate of a natural join. This
//	 * predicates does not allow unbound variables, but unbound variables
//	 * are allowed in sparql joins, so add a node that says, variable is
//	 * unbound.
//	 */
//	public static final String UNBOUND_ATTRIBUTE = "unbound";
//	
//	public static final Node UNBOUND_ATTRIBUTE_NODE = Node.create(UNBOUND_ATTRIBUTE);
//	
//	/**
//	 * Checks whether s is possibly an update of s hat. Means that s and s_hat
//	 * have the same subject and predicate and s follows s_hat.
//	 * @param s
//	 * @param s_hat 
//	 * @return true, if s is possibly an update of s_hat.
//	 */
//	public static boolean isPossibleUpdate(MetaTriple<? extends ITimeInterval> s, MetaTriple<? extends ITimeInterval> s_hat){
//        // check update
//        if(s_hat.getMetadata().getStart().before(s.getMetadata().getStart())){
//            if(s.getSubject().sameValueAs(s_hat.getSubject()) &&
//                    s.getPredicate().sameValueAs(s_hat.getPredicate())){
//                return true;
//            }
//        }
//        return false;
//	}
//	
//	/**
//	 * 
//	 * @param pattern the pattern against triple will be evaluated
//	 * @param triple the triple to evaluate
//	 * @return true, if triple matches the pattern, false otherwise
//	 */
//	public static boolean matchesPattern(Triple pattern, Triple triple){
//		// a check for blank nodes has not to be done, because
//		// blank nodes are stored as variables by the parser.
//		if((pattern.getSubject().isVariable() || 
//				pattern.getSubject().sameValueAs(triple.getSubject())) &&
//				
//				(pattern.getPredicate().isVariable() || 
//						pattern.getPredicate().sameValueAs(triple.getPredicate())) &&
//						
//				(pattern.getObject().isVariable() || 
//						pattern.getObject().sameValueAs(triple.getObject()))){
//			return true;
//		}
//		return false;
//	}
//	
//	/**
//	 * 
//	 * @param a the first attribute to compare
//	 * @param b the second attribute to compare
//	 * @return true, if the first attribute has the same name as the second
//	 */
//	public static boolean refersSameVar(SDFAttribute a, SDFAttribute b) {
//		return a.getQualName().equals(b.getQualName());
//	}
//	
//	/**
//	 * This method splits elements of two lists, such that
//	 * the time intervals of the new elements are equal or do not
//	 * overlap.
//	 * 
//	 * This algorithm only works correct, if only timeintervals of different
//	 * lists overlap. Timeintervals in the same list must not overlap.
//	 */
//	public static List<List<NodeList<? extends ITimeInterval>>> splitElements(
//			List<NodeList<? extends ITimeInterval>> splittedNewElements,
//			List<NodeList<? extends ITimeInterval>> splittedOldElements){
//		
//		
//		for(int i = 0; i<splittedNewElements.size(); i++){
//			NodeList<? extends ITimeInterval> e_tilde = splittedNewElements.get(i);
//			inner:
//			for(int u = 0; u<splittedOldElements.size(); u++){
//				NodeList<? extends ITimeInterval> e_hat = splittedOldElements.get(u);
//				
//				boolean changedOuter = false;
//				/*
//				 * t_s,1 < t_s,2 AND t_s,2 < t_e,1 <= t_e,2
//				 */
//				if(e_tilde.getMetadata().getStart().before(e_hat.getMetadata().getStart()) && 
//						e_hat.getMetadata().getStart().before(e_tilde.getMetadata().getEnd()) &&
//						e_tilde.getMetadata().getEnd().beforeOrEquals(e_hat.getMetadata().getEnd())){
//					
//					NodeList<? extends ITimeInterval> newTilde1 = e_tilde.clone();
//					newTilde1.getMetadata().setStart(e_tilde.getMetadata().getStart());
//					newTilde1.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//					
//					NodeList<? extends ITimeInterval> newTilde2 = e_tilde.clone();
//					newTilde2.getMetadata().setStart(e_hat.getMetadata().getStart());
//					newTilde2.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//					
//					splittedNewElements.remove(e_tilde);
//					splittedNewElements.add(i, newTilde1);
//					splittedNewElements.add(i+1,newTilde2);
//					i--;
//
//					changedOuter = true;
//					
//					if(e_tilde.getMetadata().getEnd().before(e_hat.getMetadata().getEnd())){
//						NodeList<? extends ITimeInterval> newHat1 = e_hat.clone();
//						newHat1.getMetadata().setStart(e_hat.getMetadata().getStart());
//						newHat1.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//						
//						NodeList<? extends ITimeInterval> newHat2 = e_hat.clone();
//						newHat2.getMetadata().setStart(e_tilde.getMetadata().getEnd());
//						newHat2.getMetadata().setEnd(e_hat.getMetadata().getEnd());
//						
//						splittedOldElements.remove(e_hat);
//						splittedOldElements.add(u,newHat1);
//						splittedOldElements.add(u+1,newHat2);
//						u--;
//
//					}
//				}
//				
//				/*
//				 * t_s,1 < t_s,2 AND t_e,1 > t_e,2
//				 */
//				if(e_tilde.getMetadata().getStart().before(e_hat.getMetadata().getStart()) &&
//						e_tilde.getMetadata().getEnd().after(e_hat.getMetadata().getEnd())){
//					
//					NodeList<? extends ITimeInterval> newTilde1 = e_tilde.clone();
//					newTilde1.getMetadata().setStart(e_tilde.getMetadata().getStart());
//					newTilde1.getMetadata().setEnd(e_hat.getMetadata().getStart());
//					
//					NodeList<? extends ITimeInterval> newTilde2 = e_tilde.clone();
//					newTilde2.getMetadata().setStart(e_hat.getMetadata().getStart());
//					newTilde2.getMetadata().setEnd(e_hat.getMetadata().getEnd());
//					
//					NodeList<? extends ITimeInterval> newTilde3 = e_tilde.clone();
//					newTilde3.getMetadata().setStart(e_hat.getMetadata().getEnd());
//					newTilde3.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//					
//					splittedNewElements.remove(e_tilde);
//					splittedNewElements.add(i,newTilde1);
//					splittedNewElements.add(i+1,newTilde2);
//					splittedNewElements.add(i+2,newTilde3);
//					i--;
//
//					
//					changedOuter = true;
//					
//				}
//				
//				/*
//				 * t_s,1 > t_s,2 AND t_e,1 < t_e,2
//				 */
//				if(e_tilde.getMetadata().getStart().after(e_hat.getMetadata().getStart()) &&
//						e_tilde.getMetadata().getEnd().before(e_hat.getMetadata().getEnd())){
//					NodeList<? extends ITimeInterval> newHat1 = e_hat.clone();
//					newHat1.getMetadata().setStart(e_hat.getMetadata().getStart());
//					newHat1.getMetadata().setEnd(e_tilde.getMetadata().getStart());
//					
//					NodeList<? extends ITimeInterval> newHat2 = e_hat.clone();
//					newHat2.getMetadata().setStart(e_tilde.getMetadata().getStart());
//					newHat2.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//					
//					NodeList<? extends ITimeInterval> newHat3 = e_hat.clone();
//					newHat3.getMetadata().setStart(e_tilde.getMetadata().getEnd());
//					newHat3.getMetadata().setEnd(e_hat.getMetadata().getEnd());
//					
//					splittedOldElements.remove(e_hat);
//					splittedOldElements.add(u,newHat1);
//					splittedOldElements.add(u+1,newHat2);
//					splittedOldElements.add(u+2,newHat3);
//					u--;
//
//				}
//				
//				/*
//				 * t_e,2 > t_s,1 >= t_s,2 AND t_e,1 > t_e,2
//				 */
//				if(e_hat.getMetadata().getEnd().after(e_tilde.getMetadata().getStart()) &&
//						e_tilde.getMetadata().getStart().afterOrEquals(e_hat.getMetadata().getStart()) &&
//						e_tilde.getMetadata().getEnd().after(e_hat.getMetadata().getEnd())){
//					
//					NodeList<? extends ITimeInterval> newTilde1 = e_tilde.clone();
//					newTilde1.getMetadata().setStart(e_tilde.getMetadata().getStart());
//					newTilde1.getMetadata().setStart(e_hat.getMetadata().getEnd());
//					
//					NodeList<? extends ITimeInterval> newTilde2 = e_tilde.clone();
//					newTilde2.getMetadata().setStart(e_hat.getMetadata().getEnd());
//					newTilde2.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//					
//					splittedNewElements.remove(e_tilde);
//					splittedNewElements.add(i,newTilde1);
//					splittedNewElements.add(i+1,newTilde2);
//					i--;
//
//					
//					changedOuter = true;
//					
//					if(e_tilde.getMetadata().getStart().after(e_hat.getMetadata().getStart())){
//						NodeList<? extends ITimeInterval> newHat1 = e_hat.clone();
//						newHat1.getMetadata().setStart(e_hat.getMetadata().getStart());
//						newHat1.getMetadata().setEnd(e_tilde.getMetadata().getStart());
//						
//						NodeList<? extends ITimeInterval> newHat2 = e_hat.clone();
//						newHat2.getMetadata().setStart(e_tilde.getMetadata().getStart());
//						newHat2.getMetadata().setEnd(e_hat.getMetadata().getEnd());
//						
//						splittedOldElements.remove(e_hat);
//						splittedOldElements.add(u,newHat1);
//						splittedOldElements.add(u+1,newHat2);
//						u--;
//
//					}
//				}
//				
//				/*
//				 * t_s,1 = t_s,2 AND t_e,1 < t_e,2
//				 */
//				if(e_tilde.getMetadata().getStart().equals(e_hat.getMetadata().getStart()) &&
//						e_tilde.getMetadata().getEnd().before(e_hat.getMetadata().getEnd())){
//					
//					NodeList<? extends ITimeInterval> newHat1 = e_hat.clone();
//					newHat1.getMetadata().setStart(e_hat.getMetadata().getStart());
//					newHat1.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//					
//					NodeList<? extends ITimeInterval> newHat2 = e_hat.clone();
//					newHat2.getMetadata().setStart(e_tilde.getMetadata().getEnd());
//					newHat2.getMetadata().setEnd(e_hat.getMetadata().getEnd());
//					
//					splittedOldElements.remove(e_hat);
//					splittedOldElements.add(u,newHat1);
//					splittedOldElements.add(u+1,newHat2);
//					u--;
//
//					
//				}
//				
//				if(e_tilde.getMetadata().getEnd().equals(e_hat.getMetadata().getEnd()) &&
//						e_tilde.getMetadata().getStart().after(e_hat.getMetadata().getStart())){
//					NodeList<? extends ITimeInterval> newHat1 = e_hat.clone();
//					newHat1.getMetadata().setStart(e_hat.getMetadata().getStart());
//					newHat1.getMetadata().setEnd(e_tilde.getMetadata().getStart());
//					
//					NodeList<? extends ITimeInterval> newHat2 = e_hat.clone();
//					newHat2.getMetadata().setStart(e_tilde.getMetadata().getStart());
//					newHat2.getMetadata().setEnd(e_hat.getMetadata().getEnd());
//					
//					splittedOldElements.remove(e_hat);
//					splittedOldElements.add(u,newHat1);
//					splittedOldElements.add(u+1,newHat2);
//					u--;
//
//				}
//				
//				if(changedOuter){
//					break inner;
//				}
//			}
//		}
//		
//		List<List<NodeList<? extends ITimeInterval>>> retval = new ArrayList<List<NodeList<? extends ITimeInterval>>>();
//		
//		retval.add(splittedOldElements);
//		retval.add(splittedNewElements);
//
//		return retval;
//	}
//	
//	/**
//	 * This method splits elements of two lists, such that
//	 * the time intervals of the new elements are equal or do not
//	 * overlap.
//	 * 
//	 * This algorithm only works correct, if only timeintervals of different
//	 * lists overlap. Timeintervals in the same list must not overlap.
//	 */
//	public  List<List<NodeList<M>>> splitElements2(
//			List<NodeList<M>> splittedNewElements,
//			List<NodeList<M>> splittedOldElements){
//		
//		
//		for(int i = 0; i<splittedNewElements.size(); i++){
//			NodeList<M> e_tilde = splittedNewElements.get(i);
//			inner:
//			for(int u = 0; u<splittedOldElements.size(); u++){
//				NodeList<M> e_hat = splittedOldElements.get(u);
//				
//				boolean changedOuter = false;
//				/*
//				 * t_s,1 < t_s,2 AND t_s,2 < t_e,1 <= t_e,2
//				 */
//				if(e_tilde.getMetadata().getStart().before(e_hat.getMetadata().getStart()) && 
//						e_hat.getMetadata().getStart().before(e_tilde.getMetadata().getEnd()) &&
//						e_tilde.getMetadata().getEnd().beforeOrEquals(e_hat.getMetadata().getEnd())){
//					
//					NodeList<M> newTilde1 = e_tilde.clone();
//					newTilde1.getMetadata().setStart(e_tilde.getMetadata().getStart());
//					newTilde1.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//					
//					NodeList<M> newTilde2 = e_tilde.clone();
//					newTilde2.getMetadata().setStart(e_hat.getMetadata().getStart());
//					newTilde2.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//					
//					splittedNewElements.remove(e_tilde);
//					splittedNewElements.add(i, newTilde1);
//					splittedNewElements.add(i+1,newTilde2);
//					i--;
//
//					changedOuter = true;
//					
//					if(e_tilde.getMetadata().getEnd().before(e_hat.getMetadata().getEnd())){
//						NodeList<M> newHat1 = e_hat.clone();
//						newHat1.getMetadata().setStart(e_hat.getMetadata().getStart());
//						newHat1.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//						
//						NodeList<M> newHat2 = e_hat.clone();
//						newHat2.getMetadata().setStart(e_tilde.getMetadata().getEnd());
//						newHat2.getMetadata().setEnd(e_hat.getMetadata().getEnd());
//						
//						splittedOldElements.remove(e_hat);
//						splittedOldElements.add(u,newHat1);
//						splittedOldElements.add(u+1,newHat2);
//						u--;
//
//					}
//				}
//				
//				/*
//				 * t_s,1 < t_s,2 AND t_e,1 > t_e,2
//				 */
//				if(e_tilde.getMetadata().getStart().before(e_hat.getMetadata().getStart()) &&
//						e_tilde.getMetadata().getEnd().after(e_hat.getMetadata().getEnd())){
//					
//					NodeList<M> newTilde1 = e_tilde.clone();
//					newTilde1.getMetadata().setStart(e_tilde.getMetadata().getStart());
//					newTilde1.getMetadata().setEnd(e_hat.getMetadata().getStart());
//					
//					NodeList<M> newTilde2 = e_tilde.clone();
//					newTilde2.getMetadata().setStart(e_hat.getMetadata().getStart());
//					newTilde2.getMetadata().setEnd(e_hat.getMetadata().getEnd());
//					
//					NodeList<M> newTilde3 = e_tilde.clone();
//					newTilde3.getMetadata().setStart(e_hat.getMetadata().getEnd());
//					newTilde3.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//					
//					splittedNewElements.remove(e_tilde);
//					splittedNewElements.add(i,newTilde1);
//					splittedNewElements.add(i+1,newTilde2);
//					splittedNewElements.add(i+2,newTilde3);
//					i--;
//
//					
//					changedOuter = true;
//					
//				}
//				
//				/*
//				 * t_s,1 > t_s,2 AND t_e,1 < t_e,2
//				 */
//				if(e_tilde.getMetadata().getStart().after(e_hat.getMetadata().getStart()) &&
//						e_tilde.getMetadata().getEnd().before(e_hat.getMetadata().getEnd())){
//					NodeList<M> newHat1 = e_hat.clone();
//					newHat1.getMetadata().setStart(e_hat.getMetadata().getStart());
//					newHat1.getMetadata().setEnd(e_tilde.getMetadata().getStart());
//					
//					NodeList<M> newHat2 = e_hat.clone();
//					newHat2.getMetadata().setStart(e_tilde.getMetadata().getStart());
//					newHat2.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//					
//					NodeList<M> newHat3 = e_hat.clone();
//					newHat3.getMetadata().setStart(e_tilde.getMetadata().getEnd());
//					newHat3.getMetadata().setEnd(e_hat.getMetadata().getEnd());
//					
//					splittedOldElements.remove(e_hat);
//					splittedOldElements.add(u,newHat1);
//					splittedOldElements.add(u+1,newHat2);
//					splittedOldElements.add(u+2,newHat3);
//					u--;
//
//				}
//				
//				/*
//				 * t_e,2 > t_s,1 >= t_s,2 AND t_e,1 > t_e,2
//				 */
//				if(e_hat.getMetadata().getEnd().after(e_tilde.getMetadata().getStart()) &&
//						e_tilde.getMetadata().getStart().afterOrEquals(e_hat.getMetadata().getStart()) &&
//						e_tilde.getMetadata().getEnd().after(e_hat.getMetadata().getEnd())){
//					
//					NodeList<M> newTilde1 = e_tilde.clone();
//					newTilde1.getMetadata().setStart(e_tilde.getMetadata().getStart());
//					newTilde1.getMetadata().setStart(e_hat.getMetadata().getEnd());
//					
//					NodeList<M> newTilde2 = e_tilde.clone();
//					newTilde2.getMetadata().setStart(e_hat.getMetadata().getEnd());
//					newTilde2.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//					
//					splittedNewElements.remove(e_tilde);
//					splittedNewElements.add(i,newTilde1);
//					splittedNewElements.add(i+1,newTilde2);
//					i--;
//
//					
//					changedOuter = true;
//					
//					if(e_tilde.getMetadata().getStart().after(e_hat.getMetadata().getStart())){
//						NodeList<M> newHat1 = e_hat.clone();
//						newHat1.getMetadata().setStart(e_hat.getMetadata().getStart());
//						newHat1.getMetadata().setEnd(e_tilde.getMetadata().getStart());
//						
//						NodeList<M> newHat2 = e_hat.clone();
//						newHat2.getMetadata().setStart(e_tilde.getMetadata().getStart());
//						newHat2.getMetadata().setEnd(e_hat.getMetadata().getEnd());
//						
//						splittedOldElements.remove(e_hat);
//						splittedOldElements.add(u,newHat1);
//						splittedOldElements.add(u+1,newHat2);
//						u--;
//
//					}
//				}
//				
//				/*
//				 * t_s,1 = t_s,2 AND t_e,1 < t_e,2
//				 */
//				if(e_tilde.getMetadata().getStart().equals(e_hat.getMetadata().getStart()) &&
//						e_tilde.getMetadata().getEnd().before(e_hat.getMetadata().getEnd())){
//					
//					NodeList<M> newHat1 = e_hat.clone();
//					newHat1.getMetadata().setStart(e_hat.getMetadata().getStart());
//					newHat1.getMetadata().setEnd(e_tilde.getMetadata().getEnd());
//					
//					NodeList<M> newHat2 = e_hat.clone();
//					newHat2.getMetadata().setStart(e_tilde.getMetadata().getEnd());
//					newHat2.getMetadata().setEnd(e_hat.getMetadata().getEnd());
//					
//					splittedOldElements.remove(e_hat);
//					splittedOldElements.add(u,newHat1);
//					splittedOldElements.add(u+1,newHat2);
//					u--;
//
//					
//				}
//				
//				if(e_tilde.getMetadata().getEnd().equals(e_hat.getMetadata().getEnd()) &&
//						e_tilde.getMetadata().getStart().after(e_hat.getMetadata().getStart())){
//					NodeList<M> newHat1 = e_hat.clone();
//					newHat1.getMetadata().setStart(e_hat.getMetadata().getStart());
//					newHat1.getMetadata().setEnd(e_tilde.getMetadata().getStart());
//					
//					NodeList<M> newHat2 = e_hat.clone();
//					newHat2.getMetadata().setStart(e_tilde.getMetadata().getStart());
//					newHat2.getMetadata().setEnd(e_hat.getMetadata().getEnd());
//					
//					splittedOldElements.remove(e_hat);
//					splittedOldElements.add(u,newHat1);
//					splittedOldElements.add(u+1,newHat2);
//					u--;
//
//				}
//				
//				if(changedOuter){
//					break inner;
//				}
//			}
//		}
//		
//		List<List<NodeList<M>>> retval = new ArrayList<List<NodeList<M>>>();
//		
//		retval.add(splittedOldElements);
//		retval.add(splittedNewElements);
//
//		return retval;
//	}
//	
//	/**
//	 * for testing
//	 * @param args
//	 */
//	public static void main(String[] args){
////		TimeInterval i1 = new TimeInterval(new PointInTime(0,0), new PointInTime(5,0));
////		TimeInterval i2 = new TimeInterval(new PointInTime(5,0), new PointInTime(10,0));
////		TimeInterval i3 = new TimeInterval(new PointInTime(3,0), new PointInTime(7,0));
////		TimeInterval i4 = new TimeInterval(new PointInTime(6,0), new PointInTime(9,0));
////		TimeInterval i5 = new TimeInterval(new PointInTime(11,0), new PointInTime(15,0));
////		
////		TimestampedExchangeElement<NodeList> t1 = new TimestampedExchangeElement<NodeList>(null, i1);
////		t1.no = 1;
////		TimestampedExchangeElement<NodeList> t2 = new TimestampedExchangeElement<NodeList>(null, i2);
////		t2.no = 2;
////		TimestampedExchangeElement<NodeList> t3 = new TimestampedExchangeElement<NodeList>(null, i3);
////		t3.no = 3;
////		TimestampedExchangeElement<NodeList> t4 = new TimestampedExchangeElement<NodeList>(null, i4);
////		t4.no = 4;
////		TimestampedExchangeElement<NodeList> t5 = new TimestampedExchangeElement<NodeList>(null, i5);
////		t5.no = 5;
////		
////		List<TimestampedExchangeElement<NodeList>> splittedNewElements = new ArrayList<TimestampedExchangeElement<NodeList>>();
////		List<TimestampedExchangeElement<NodeList>> splittedOldElements = new ArrayList<TimestampedExchangeElement<NodeList>>();
////				
////		splittedOldElements.add(t2);
////		splittedOldElements.add(t3);
////		splittedOldElements.add(t4);
////		splittedOldElements.add(t4);
////		splittedOldElements.add(t5);
////		
////		
////		splittedNewElements.add(t1);
////		splittedNewElements.add(t2);
////		splittedNewElements.add(t2);
//////		splittedOldElements.add(t4);
////		
////		
////		List<List<TimestampedExchangeElement<NodeList>>> result = splitElements((List<TimestampedExchangeElement<NodeList>>)splittedNewElements, (List<TimestampedExchangeElement<NodeList>>)splittedOldElements);
////		
////		List<TimestampedExchangeElement<NodeList>> resOld = result.get(0);
////		List<TimestampedExchangeElement<NodeList>> resNew = result.get(1);
////		
////		for(TimestampedExchangeElement<NodeList> n : resOld){
////			System.out.println("old Elem: " + n.no + ": " + n.getValidity());
////		}
////		
////		for(TimestampedExchangeElement<NodeList> n : resNew){
////			System.out.println("new Elem: " + n.no + ": " + n.getValidity());
////		}
//		
//	}
//}
