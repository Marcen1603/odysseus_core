/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
//package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.object.predicate;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.hp.hpl.jenaUpdated.sparql.core.Var;
//import com.hp.hpl.jenaUpdated.sparql.engine.binding.Binding;
//import com.hp.hpl.jenaUpdated.sparql.engine.binding.BindingMap;
//import com.hp.hpl.jenaUpdated.sparql.expr.Expr;
//
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.IClone;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.predicate.AbstractPredicate;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.util.SPARQL_Util;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFAttribute;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFSchema;
//
//public class SPARQLFilterPredicate<T extends IClone> extends AbstractPredicate<NodeList<T>>{
//
//	private SDFSchema inputElems;
//	private List<Expr> exprs;
//	
//	public SPARQLFilterPredicate(Expr ex, SDFSchema input){
//		this.inputElems = input;
//		this.exprs = new ArrayList<Expr>();
//		this.exprs.add(ex);
//	}
//	
//	public SPARQLFilterPredicate(List<Expr> exprs, SDFSchema input){
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
