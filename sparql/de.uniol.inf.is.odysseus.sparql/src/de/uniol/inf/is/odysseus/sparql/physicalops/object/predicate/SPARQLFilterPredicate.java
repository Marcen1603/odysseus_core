//package de.uniol.inf.is.odysseus.sparql.physicalops.object.predicate;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.hp.hpl.jenaUpdated.sparql.core.Var;
//import com.hp.hpl.jenaUpdated.sparql.engine.binding.Binding;
//import com.hp.hpl.jenaUpdated.sparql.engine.binding.BindingMap;
//import com.hp.hpl.jenaUpdated.sparql.expr.Expr;
//
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.IClone;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.predicate.AbstractPredicate;
//import de.uniol.inf.is.odysseus.queryexecution.po.sparql.object.NodeList;
//import de.uniol.inf.is.odysseus.queryexecution.po.sparql.util.SPARQL_Util;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
//
//public class SPARQLFilterPredicate<T extends IClone> extends AbstractPredicate<NodeList<T>>{
//
//	private SDFAttributeList inputElems;
//	private List<Expr> exprs;
//	
//	public SPARQLFilterPredicate(Expr ex, SDFAttributeList input){
//		this.inputElems = input;
//		this.exprs = new ArrayList<Expr>();
//		this.exprs.add(ex);
//	}
//	
//	public SPARQLFilterPredicate(List<Expr> exprs, SDFAttributeList input){
//		this.exprs = exprs;
//		this.inputElems = input;
//	}
//	
//	public boolean evaluate(NodeList<T> elem){
//		Binding binding = new BindingMap();
//		int i=0;
//		for(SDFAttribute a : this.inputElems){
//			// only add if the value of the attribute is
//			// not the special value, after a union operation
//			// use == because the union operator only uses one
//			// instance of this special node. So you can
//			// distinguish between a node that has the special value,
//			// because this special value is used in an application
//			// and a node that indicates an extra attribute;
//			// also do not add the unbound attributes
//			if(!(elem.get(i) == SPARQL_Util.EXTRA_ATTRIBUTE_NODE) && 
//					!(elem.get(i) == SPARQL_Util.UNBOUND_ATTRIBUTE_NODE)){
//				binding.add(Var.alloc(a.getQualName()), elem.get(i++));
//			}
//			else{
//				i++;
//			}
//		}
//		
//		for(Expr ex : this.exprs){
//			if(!ex.isSatisfied(binding, null)){
//				return false;
//			}
//		}
//		return true;
//	}
//	
//	@Deprecated
//	public boolean evaluate(NodeList<T> left, NodeList<T> right){
//		throw new UnsupportedOperationException();
//	}
//	
//	public SPARQLFilterPredicate<T> clone(){
//		return new SPARQLFilterPredicate<T>(this.exprs, this.inputElems);
//	}
//}
