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
package de.uniol.inf.is.odysseus.sparql.parser.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sparql.datamodel.INode;
import de.uniol.inf.is.odysseus.sparql.datamodel.Triple;

@SuppressWarnings({"rawtypes"})
public class SparqlParserHelper {

	// ===============================================================================================
	// PUBLIC METHODS THAT CAN BE ACCESSED FROM SPARQL GRAMMAR
	
	public static JoinAO createJoin(ILogicalOperator left, ILogicalOperator right){
		JoinAO join = new JoinAO();
		
		SDFSchema leftSchema = left.getOutputSchema();
		SDFSchema rightSchema = right.getOutputSchema();
		  
		SDFSchema commonVars = SparqlParserHelper.getCommonVariables(leftSchema, rightSchema);
		
		IPredicate joinPred = SparqlParserHelper.createJoinPredicate(commonVars, leftSchema, rightSchema);
		
		join.setPredicate(joinPred);
		join.subscribeToSource(left, 0, 0, leftSchema);
		join.subscribeToSource(right, 1, 0, rightSchema);
		
		return join;
	}
	
	public static ILogicalOperator triplesSameSubject(INode subject, PropertiesAndObjects pao){
		ILogicalOperator op = null;
		List<Triple> triples = new ArrayList<Triple>();
		
		// generating all triples
		Set<INode> predicates = pao.getProperties();
		for(INode predicate: predicates){
			List<INode> objects = pao.getObjects(predicate);
			for(INode object: objects){
				Triple t = new Triple(subject, predicate, object);
				triples.add(t);
			}
		}
		
		if(triples.size() > 1){
			
		}
		else{
//			TriplePatternMatching tpm = new TriplePatternMatching();
		}
		
		return op;
	}
	
	// ===============================================================================================
	// PRIVATE METHODS THAT ARE ONLY ACCESSED WITHIN THIS CLASS AS HELPER METHODS
	
	/**
	 * Creates a join predicate of the form
	 * a.y = b.y AND a.x = b.x from two triple patterns
	 * like 
	 * {?y :pred  ?x
	 *  ?y :pred2 ?x}
	 */
	private static IPredicate createJoinPredicate(SDFSchema commonVars, SDFSchema leftSchema, SDFSchema rightSchema){
		ArrayList<SDFExpression> exprs = new ArrayList<SDFExpression>();
		
//		SDFSchema outputSchema = SDFSchema.union(leftSchema, rightSchema);
//		IAttributeResolver attrRes = new DirectAttributeResolver(outputSchema);
//		
//		
//		String exprStr = "";
//		for(int i = 0; i<commonVars.size(); i += 2){
//			SDFAttribute curLeftAttr = commonVars.get(i); // even indices contain the attributes of the left schema
//			SDFAttribute curRightAttr = commonVars.get(i+1); // odd (even + 1) indices contain the attributes of the right schema
//			exprStr += curLeftAttr.getURI() + " = " + curRightAttr.getURI();			
//			SDFExpression expr = new SDFExpression(null, exprStr, attrRes);
//		}
		
		IPredicate retval = null;
		
		
		// more than one common variable
		// so build a tree of and predicates
		if(exprs.size() > 1){
			RelationalPredicate firstRelational = new RelationalPredicate(exprs.get(0));
			firstRelational.init(leftSchema, rightSchema);
			
			IPredicate left = firstRelational;
			
			for(int i = 1; i<exprs.size(); i++){
				RelationalPredicate right = new RelationalPredicate(exprs.get(i));
				right.init(leftSchema, rightSchema);
				IPredicate tempAnd = ComplexPredicateHelper.createAndPredicate(left, right);
				left = tempAnd;
			}
			
			retval = left;
		}
		// only one common variable, so
		// return corresponding relational predicate
		else if(exprs.size() == 1){
			RelationalPredicate firstRelational = new RelationalPredicate(exprs.get(0));
			firstRelational.init(leftSchema, rightSchema);
			retval = firstRelational;
		}
		
		return retval;
	}
	
	/**
	 * Returns a list of attributes, that occur in both schemas.
	 * The even indices contain the attributes of the left schema.
	 * The odd (even +1 ) indices contain the attributes of the right schema.
	 * 
	 * @param leftSchema
	 * @param rightSchema
	 * @return
	 */
	public static SDFSchema getCommonVariables(SDFSchema leftSchema, SDFSchema rightSchema){
		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		for(SDFAttribute leftAttr: leftSchema){
			for(SDFAttribute rightAttr: rightSchema){
				if(leftAttr.getAttributeName().equals(rightAttr.getAttributeName())){
					attrs.add(leftAttr);
					attrs.add(rightAttr);
				}
			}
		}
		return SDFSchemaFactory.createNewTupleSchema("", attrs);
	}
}
