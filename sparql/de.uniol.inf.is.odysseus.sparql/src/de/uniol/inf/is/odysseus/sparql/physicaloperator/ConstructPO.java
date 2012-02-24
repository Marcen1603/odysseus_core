//package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.interval.convert;
//
//import java.util.LinkedList;
//import java.util.List;
//import com.hp.hpl.jena.graph.Node;
//import com.hp.hpl.jena.graph.Triple;
//import com.hp.hpl.jenaUpdated.sparql.core.Var;
//
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.ITimeInterval;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.operators.AbstractPipe;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.MetaTriple;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.util.SPARQL_Util;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.AbstractLogicalOperator;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.Construct;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFAttribute;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFSchema;
//
///**
// * This is the sparql construct operator for stream processing. It construct triples
// * with a time interval from a list of triple patterns.
// * @author lg
// *
// */
//public class ConstructPO extends AbstractPipe<NodeList<? extends ITimeInterval>,
//													MetaTriple<? extends ITimeInterval>>{
//	/**
//	 * The list of triple patterns from which to create
//	 * rdf statements. Used to be able to use them directly
//	 * and to have to read them from the logical operator.
//	 */
//	private List<Triple> patterns;
//	
//	/**
//	 * 
//	 * @param logical
//	 */
//	private LinkedList<MetaTriple<ITimeInterval>> nextElems;
//	
//	private AbstractLogicalOperator logical;
//	
//	public ConstructPO(AbstractLogicalOperator logical){
//		this.logical = logical;
//		this.patterns = ((Construct)logical).getTriples();
//		this.nextElems = new LinkedList<MetaTriple<ITimeInterval>>();
//	}
//	
//	public ConstructPO(ConstructPO original){
//		this.logical = original.logical;
//		this.patterns = original.patterns;
//		this.nextElems = new LinkedList<MetaTriple<ITimeInterval>>(original.nextElems);
//	}
//	
//	public ConstructPO clone(){
//		return new ConstructPO(this);
//	}
//	
//	public synchronized void process_next(NodeList<? extends ITimeInterval> object, int port, boolean isReadOnly) {
//		this.doIntervalBased(object);
//		while(this.nextElems.isEmpty()){
//			this.transfer(this.nextElems.removeFirst());
//		}
//		
//	}
//
//	private void doIntervalBased(NodeList<? extends ITimeInterval> next){
//		for(int u=0; u<this.patterns.size(); u++){
//			Triple t = this.patterns.get(u);
//			//Triple newTriple = null;
//			Node s = null;
//			Node p = null;
//			Node o = null;
//			
//			SDFSchema inElements = this.logical.getInputSchema(0);
//			// remember: blank nodes are stored as variables
//			if(t.getSubject().isVariable()){
//				SDFAttribute subject = new SDFAttribute(this.hashCode() + "#" + ((Var)t.getSubject()).getName());
//				for(int i = 0; i<inElements.size(); i++){
//					if(SPARQL_Util.refersSameVar(subject, inElements.get(i))){
//						if(! (next.get(i) == SPARQL_Util.EXTRA_ATTRIBUTE_NODE || 
//								next.get(i) == SPARQL_Util.UNBOUND_ATTRIBUTE_NODE)){
//							s = next.get(i);
//						}
//					}
//				}
//			}
//			else if(t.getSubject().isURI()){
//				s = t.getSubject();
//			}
//			
//			if(t.getPredicate().isVariable()){
//				SDFAttribute predicate = new SDFAttribute(this.hashCode() + "#" + ((Var)t.getPredicate()).getName());
//				for(int i = 0; i<inElements.size(); i++){
//					if(SPARQL_Util.refersSameVar(predicate, inElements.get(i))){
//						if(! (next.get(i) == SPARQL_Util.EXTRA_ATTRIBUTE_NODE || 
//								next.get(i) == SPARQL_Util.UNBOUND_ATTRIBUTE_NODE)){
//							p = next.get(i);
//						}
//					}
//				}
//			}
//			else if(t.getPredicate().isURI()){
//				p = t.getPredicate();
//			}
//			
//			if(t.getObject().isVariable()){
//				SDFAttribute object = new SDFAttribute(this.hashCode() + "#" + ((Var)t.getObject()).getName());
//				for(int i = 0; i<inElements.size(); i++){
//					if(SPARQL_Util.refersSameVar(object, inElements.get(i))){
//						if(! (next.get(i) == SPARQL_Util.EXTRA_ATTRIBUTE_NODE || 
//								next.get(i) == SPARQL_Util.UNBOUND_ATTRIBUTE_NODE)){
//							o = next.get(i);
//						}
//					}
//				}
//			}
//			else if(t.getObject().isURI() || t.getObject().isLiteral()){
//				o = t.getObject();
//			}
//			
//			if(s != null && p != null && o != null){
//				MetaTriple<ITimeInterval> retval = new MetaTriple<ITimeInterval>(s, p, o);
//				retval.setMetadata((ITimeInterval)next.getMetadata().clone());
//				this.nextElems.addLast(retval);
//			}
//			else{
//				// there is an unbound variable, so do not put it into the output
//				continue;
//			}
//		}
//	}
//	
//	public void process_close(){
//	}
//}
