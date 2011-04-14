package de.uniol.inf.is.odysseus.sparql.parser.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.sparql.logicaloperator.TriplePatternMatching;

public class SparqlParserHelper {

	// ===============================================================================================
	// PUBLIC METHODS THAT CAN BE ACCESSED FROM SPARQL GRAMMAR
	
	public static JoinAO createJoin(ILogicalOperator left, ILogicalOperator right){
		JoinAO join = new JoinAO();
		
		SDFAttributeList leftSchema = left.getOutputSchema();
		SDFAttributeList rightSchema = right.getOutputSchema();
		  
		SDFAttributeList commonVars = SparqlParserHelper.getCommonVariables(leftSchema, rightSchema);
		
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
	private static IPredicate createJoinPredicate(SDFAttributeList commonVars, SDFAttributeList leftSchema, SDFAttributeList rightSchema){
		ArrayList<SDFExpression> exprs = new ArrayList<SDFExpression>();
		
		SDFAttributeList outputSchema = SDFAttributeList.union(leftSchema, rightSchema);
		IAttributeResolver attrRes = new DirectAttributeResolver(outputSchema);
		
		
		String exprStr = "";
		for(int i = 0; i<commonVars.getAttributeCount(); i += 2){
			SDFAttribute curLeftAttr = commonVars.get(i); // even indices contain the attributes of the left schema
			SDFAttribute curRightAttr = commonVars.get(i+1); // odd (even + 1) indices contain the attributes of the right schema
			exprStr += curLeftAttr.getURI() + " = " + curRightAttr.getURI();
			SDFExpression expr = new SDFExpression(null, exprStr, attrRes);
		}
		
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
				AndPredicate tempAnd = new AndPredicate(left, right);
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
	private static SDFAttributeList getCommonVariables(SDFAttributeList leftSchema, SDFAttributeList rightSchema){
		SDFAttributeList commonSchema = new SDFAttributeList();
		for(SDFAttribute leftAttr: leftSchema){
			for(SDFAttribute rightAttr: rightSchema){
				if(leftAttr.getAttributeName().equals(rightAttr.getAttributeName())){
					commonSchema.add(leftAttr);
					commonSchema.add(rightAttr);
				}
			}
		}
		return commonSchema;
	}
}
