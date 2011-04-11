//package de.uniol.inf.is.odysseus.sparql.physicalops.object;
//
//import java.util.Comparator;
//
//import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
//import com.hp.hpl.jena.graph.Node;
//
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.IClone;
//import de.uniol.inf.is.odysseus.queryexecution.po.sparql.util.SPARQL_Util;
//
///**
// * This is comparator to be able to sort a List<List<Node>> in the way
// * defined in the sparql recommendation.
// * 
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// *
// */
////should be T extends IClone & Comparable<T> but suns compiler (1.6) is buggy and doesn't accept it
//public class SparqlTimestampedSolutionComparator<T extends IClone> implements 
//						Comparator<NodeList<T>>{
//
//	/**
//	 * These are the indices of the attributes to sort.
//	 * An index of 1 means the nodes at position 1 will
//	 * compared.
//	 */
//	private int[] inds;
//	
//	/**
//	 * the direction for the attribute at the specified index
//	 */
//	private boolean[] ascending;
//	
//	public SparqlTimestampedSolutionComparator(int[] attributeIndices, boolean[] direction){
//		this.inds = attributeIndices;
//		this.ascending = direction;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public int compare(NodeList<T> t1, NodeList<T> t2){
//		int retval = 0;
//		// if the intervals are not equal, the ealier one
//		// is less than the later one
//		if(((Comparable<T>)t1.getMetadata()).compareTo(t2.getMetadata()) != 0){
//			return ((Comparable<T>)t1.getMetadata()).compareTo(t2.getMetadata());
//		}
//		
//		NodeList l1 = t1;
//		NodeList l2 = t2;
//		// the timeintervals are equal so compare
//		// list of nodes
//		for(int i = 0; i<this.inds.length; i++){
//			Node n1 = l1.get(this.inds[i]);
//			Node n2 = l2.get(this.inds[i]);
//			boolean asc = this.ascending[i];
//			
//			if(n1 == SPARQL_Util.UNBOUND_ATTRIBUTE_NODE &&
//					n2 != SPARQL_Util.UNBOUND_ATTRIBUTE_NODE){
//				if(asc){
//					return -1;
//				}
//				else{
//					return 1;
//				}
//			}
//			else if(n1.isBlank() && n2 == SPARQL_Util.UNBOUND_ATTRIBUTE_NODE ){
//				if(asc){
//					return 1;
//				}
//				else{
//					return -1;
//				}
//			}
//			else if(n1.isBlank() && n2 != SPARQL_Util.UNBOUND_ATTRIBUTE_NODE
//					&& !n2.isBlank()){
//				if(asc){
//					return -1;
//				}
//				else{
//					return 1;
//				}
//			}
//			else if(n1.isURI() && (n2 == SPARQL_Util.UNBOUND_ATTRIBUTE_NODE || 
//									n2.isBlank())){
//				if(asc){
//					return 1;
//				}
//				else{
//					return -1;
//				}
//				
//			}
//			else if(n1.isURI() && n2 != SPARQL_Util.UNBOUND_ATTRIBUTE_NODE && 
//					!n2.isBlank() && 
//					!n2.isURI()){
//				if(asc){
//					return -1;
//				}
//				else{
//					return 1;
//				}
//			}
//			else if(n1.isURI() && n2.isURI()){
//				int compVal = n1.getURI().compareTo(n2.getURI());
//				if(compVal != 0){
//					if(asc){
//						return compVal;
//					}
//					else{
//						return compVal * -1;
//					}
//				}
//			}
//			else if(n1.isLiteral() && (n2 == SPARQL_Util.UNBOUND_ATTRIBUTE_NODE || 
//										n2.isBlank() || 
//										n2.isURI())){
//				if(asc){
//					return 1;
//				}
//				else{
//					return -1;
//				}
//			}
//			else if(n1.isLiteral() && n2.isLiteral()){
//				if(n1.getLiteralDatatype() != null &&
//						n2.getLiteralDatatype() != null){
//					if(n1.getLiteralDatatype().equals(XSDDatatype.XSDstring) &&
//						!n2.getLiteralDatatype().equals(XSDDatatype.XSDstring)){
//						if(asc){
//							return 1;
//						}
//						else{
//							return -1;
//						}
//					}
//					else if(!n1.getLiteralDatatype().equals(XSDDatatype.XSDstring) &&
//						n2.getLiteralDatatype().equals(XSDDatatype.XSDstring)){
//						if(asc){
//							return -1;
//						}
//						else{
//							return 1;
//						}
//					}
//					else{
//						int compVal = n1.getLiteralLexicalForm().compareTo(n2.getLiteralLexicalForm());
//						if(asc){
//							return compVal;
//						}
//						else{
//							return compVal * -1;
//						}
//					}
//				}
//				else{
//					int compVal =  n1.getLiteralLexicalForm().compareTo(n2.getLiteralLexicalForm());
//					if(asc){
//						return compVal;
//					}
//					else{
//						return compVal * -1;
//					}
//				}
//			}
//			// SPARQL_Util.EXTRA_ATTRIBUTE_NODEs are literals
//			// they are equal to each other and are set to
//			// the end of the list.
//		}
//		
//		return retval;
//	}
//}
