package de.uniol.inf.is.odysseus.objecttracking.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.ISolution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.Solution;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * This class is just a hack for the missing maple solutions.
 * It provides methods that return the missing solutions for
 * SPECIFIC prediction functions used during evaluation.
 * 
 * @author André Bolles
 *
 */
public class MapleHack {

	private static Logger logger = LoggerFactory.getLogger(MapleHack.class);
	
	/**
	 * Maple hat ein Problem. Wenn die Lösungen existieren:
	 * falls b<0 => t < c/b
	 * falls b>0 => t > c/b
	 * dann fehlt immer
	 * 
	 * falls b = 0 => irgendwas
	 * 
	 * Hier muss man dann noch die Lösungen
	 * 
	 * falls b = 0 && c >  0 dann irgendwas
	 * falls b = 0 && c <= 0 dann irgendwas hinzufügen
	 * 
	 * @param originalExpression Der ursprüngliche Ausdruck, der nach t aufgelöst werden sollte.
	 * @param denominator entspricht b in der obigen Beschreibung
	 * @param solution Ein der gefundenen Lösungen t <|>|<=|>= c/b Hieraus kann man den zähler entnehmen.
	 * @param attributeResolver Wird für die zu erzeugenden Expressions benötigt.
	 * 
	 * @return Eine Map mit den fehlenden Lösungen
	 */
	public static Map<IPredicate, ISolution> getMissingJoinSolutions(String originalExpression, String denominator, String solution, IAttributeResolver attributeResolver){
		Map<IPredicate, ISolution> missingSolutions = new HashMap<IPredicate, ISolution>();
		
		// 1. the full solution
		SDFExpression leftExpr = new SDFExpression(null, denominator + " == 0", attributeResolver);
		RelationalPredicate left = new RelationalPredicate(leftExpr);
		
		int indexOfSlash = solution.indexOf('/');
		
		String enumerator = solution.substring(0, indexOfSlash);
		
		// will be used for the empty solution
		// since there the compare operator must
		// inverted.
		String newCompareOperator = null;
		String oldCompareOperator = null;
		
		if(originalExpression.contains(">=")){
			oldCompareOperator = ">=";
			newCompareOperator = "<";
		}
		else if(originalExpression.contains("<=")){
			oldCompareOperator = "<=";
			newCompareOperator = ">";
		}
		else if(originalExpression.contains("<")){
			oldCompareOperator = "<";
			newCompareOperator = ">=";
		}
		else if(originalExpression.contains(">")){
			oldCompareOperator = ">";
			newCompareOperator = "<=";
		}
		else{
			throw new RuntimeException("No compare operator found in expression: " + originalExpression);
		}
		
		// only get the right side of the comparison
		// we assume, that the left side is the only one that contains
		// t
		String enumeratorPredicate = enumerator + " " + newCompareOperator + " 0 ";
		
		SDFExpression rightExpr = new SDFExpression(null, enumeratorPredicate, attributeResolver);
		RelationalPredicate right = new RelationalPredicate(rightExpr);
		AndPredicate andPred = new AndPredicate(left, right);
		
		ISolution full = new Solution(new SDFExpression(null, "(t)", attributeResolver), null, null);
		missingSolutions.put(andPred, full);
		
		logger.debug("Original Expression: " + originalExpression);
		logger.debug("Missing Solution: If " + andPred.toString() + " then " + full.toString() );
		
		// 2. the empty solution
		SDFExpression leftExprEmpty = new SDFExpression(null, denominator + " == 0", attributeResolver);
		RelationalPredicate leftEmpty = new RelationalPredicate(leftExpr);
		
		
		
		
		
		// only get the right side of the comparison
		// we assume, that the left side is the only one that contains
		// t		
		String enumeratorPredicateEmpty = enumerator + " " + oldCompareOperator + " 0 ";
		
		SDFExpression rightExprEmpty = new SDFExpression(null, enumeratorPredicateEmpty, attributeResolver);
		RelationalPredicate rightEmpty = new RelationalPredicate(rightExprEmpty);
		AndPredicate andPredEmpty = new AndPredicate(left, rightEmpty);
		
		ISolution empty = new Solution(null, null, null);
		missingSolutions.put(andPredEmpty, empty);
		
		logger.debug("Missing Solution: If " + andPredEmpty.toString() + " then " + empty.toString() );
		
		return missingSolutions;
	}
	
	/**
	 * This method is used for evaluation. Since we cannot solve quadratic functions automatically,
	 * we have to simulate the situation with these functions. Therefore, this method returns
	 * additions predicates, that always evaluate to false.
	 * 
	 * @param noOfPredicates
	 * @param attributes
	 * @param attrRes
	 * @return
	 */
	public static Map<IPredicate, ISolution> getAdditionalFalsePredrdicates(int noOfPredicates, List<SDFAttribute> attributes, IAttributeResolver attrRes){
		Map<IPredicate, ISolution> falseSolutions = new HashMap<IPredicate, ISolution>();
		int counter = 0;
		for(int i = 0; i<attributes.size(); i++){
			for(int u = 0; u<attributes.size(); u++){
				
				if(counter >= noOfPredicates){
					logger.debug(counter + " additional false predicates added.");
					return falseSolutions;
				}
				counter++;
				
				SDFExpression leftExpr = new SDFExpression(null, attributes.get(i).toPointString() + "<" + attributes.get(u).toPointString(), attrRes );
				SDFExpression rightExpr = new SDFExpression(null, attributes.get(i).toPointString() + ">" + attributes.get(u).toPointString(), attrRes);
				IPredicate left = new RelationalPredicate(leftExpr);
				IPredicate right = new RelationalPredicate(rightExpr);
				AndPredicate andPred = new AndPredicate(left, right);
				
				ISolution empty = new Solution(null, null, null);
				falseSolutions.put(andPred, empty);
				logger.debug("Additional false predicate: " + andPred);
			}
		}
		
		logger.debug(counter + " additional false predicates added.");
		
		return falseSolutions;
	}
	
	
	public static Map<IPredicate, ISolution> getMissingJoinSolutions1(IAttributeResolver attributeResolver){
		Map<IPredicate, ISolution> missingSolutions = new HashMap<IPredicate, ISolution>();
		
		
		// 1. the full solution
		
		SDFExpression leftExpr = new SDFExpression(null, "a.speed - b.speed == 0", attributeResolver);
		RelationalPredicate left = new RelationalPredicate(leftExpr);
		
		SDFExpression rightExpr = new SDFExpression(null, "a.pos - b.pos + b.speed * b.timestamp - a.speed * a.timestamp > -0.1", attributeResolver);
		RelationalPredicate right = new RelationalPredicate(rightExpr);
		AndPredicate andPred = new AndPredicate(left, right);
		
		ISolution solution = new Solution(new SDFExpression(null, "(t)", attributeResolver), null, null);
		missingSolutions.put(andPred, solution);
		
		// 2. the empty solution
		
		SDFExpression leftExprEmpty = new SDFExpression(null, "a.speed - b.speed == 0", attributeResolver);
		RelationalPredicate leftEmpty = new RelationalPredicate(leftExpr);
		
		SDFExpression rightExprEmpty = new SDFExpression(null, "a.pos - b.pos + b.speed * b.timestamp - a.speed * a.timestamp <= -0.1", attributeResolver);
		RelationalPredicate rightEmpty = new RelationalPredicate(rightExprEmpty);
		AndPredicate andPredEmpty = new AndPredicate(leftEmpty, rightEmpty);
		
		ISolution solutionEmpty = new Solution(null, null, null);
		missingSolutions.put(andPredEmpty, solutionEmpty);
		
		return missingSolutions;
	}
	
	public static Map<IPredicate, ISolution> getMissingJoinSolutions2(IAttributeResolver attributeResolver){
		Map<IPredicate, ISolution> missingSolutions = new HashMap<IPredicate, ISolution>();
		
		// 1. the full solution
		SDFExpression leftExpr = new SDFExpression(null, "a.speed - b.speed == 0", attributeResolver);
		RelationalPredicate left = new RelationalPredicate(leftExpr);
		
		SDFExpression rightExpr = new SDFExpression(null, "a.pos - b.pos + b.speed * b.timestamp - a.speed * a.timestamp < 0.1", attributeResolver);
		RelationalPredicate right = new RelationalPredicate(rightExpr);
		AndPredicate andPred = new AndPredicate(left, right);
		
		ISolution solution = new Solution(new SDFExpression(null, "(t)", attributeResolver), null, null);
		
		missingSolutions.put(andPred, solution);
		
		
		// 2. the empty solution
		SDFExpression leftExprEmpty = new SDFExpression(null, "a.speed - b.speed == 0", attributeResolver);
		RelationalPredicate leftEmtpy = new RelationalPredicate(leftExpr);
		
		SDFExpression rightExprEmpty = new SDFExpression(null, "a.pos - b.pos + b.speed * b.timestamp - a.speed * a.timestamp >= 0.1", attributeResolver);
		RelationalPredicate rightEmpty = new RelationalPredicate(rightExpr);
		AndPredicate andPredEmpty = new AndPredicate(left, right);
		
		ISolution solutionEmpty = new Solution(null, null, null);
		
		missingSolutions.put(andPredEmpty, solutionEmpty);
		
//		(.1000000000-1.*(a.pos)+a.speed*(a.timestamp)+b.pos-1.*(b.speed)*(b.timestamp))
//		(.1000000000-1.*(a.pos)+a.speed*(a.timestamp)+b.pos-1.*(b.speed)*(b.timestamp))
//		
//		(-1.*(a.pos)+a.speed*(a.timestamp)+b.pos-1.*(b.speed)*(b.timestamp)-.1000000000)
//		
		return missingSolutions;
	}
	
	public static Map<IPredicate, ISolution> getMissingSelectionSolutions(){
		return null;
	}
}
