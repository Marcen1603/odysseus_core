package de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.base.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.ISolution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.Solution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTCompareOperator;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTComplexCondition;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTCondition;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTConditionSolution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTEmptySolution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTExpression;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTFullSolution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTFunctionExpression;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTFunctionName;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTIdentifier;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTMaplePiecewise;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTNumber;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTSimpleCondition;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTSimpleSolution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTSimpleSolutionContent;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTSimpleToken;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTSolution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTString;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.MapleResultParserVisitor;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.SimpleNode;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * This visitor traverses the abstract syntax tree of a maple result
 * and creates a map containing as keys the conditions of the solutions
 * and as values the coresponding solutions for the variable t.
 * 
 * @author André Bolles
 *
 */
public class CreateExpressionMapVisitor implements MapleResultParserVisitor  {
	
	@Override
	public Object visit(SimpleNode node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTMaplePiecewise node, Object data) {
		Map<IPredicate<RelationalTuple<?>>, ISolution> solutions = new HashMap<IPredicate<RelationalTuple<?>>, ISolution>();
		
		// the children of this node are the condition/solution combinations
		
		for(int i = 0; i<node.jjtGetNumChildren(); i++){
			if(node.jjtGetChild(i) instanceof ASTConditionSolution){
				ArrayList condSolCombi = (ArrayList)node.jjtGetChild(i).jjtAccept(this, data);
				solutions.put((IPredicate)condSolCombi.get(0), (ISolution)condSolCombi.get(1));
			}
			// the "otherwise" part of the maple solution
			// this can only be true, for the last child, so
			// we can read from the EntrySet of the map, since
			// all elements must already be in this map.
			else if(node.jjtGetChild(i) instanceof ASTSolution){
				ISolution solution = (ISolution)node.jjtGetChild(i).jjtAccept(this, data);
				// create a NotPredicate(OrPredicate) from all other predicates
				// all the other predicates must already be in the map
				if(solutions.entrySet().size() == 1){
					for(Entry<IPredicate<RelationalTuple<?>>, ISolution> entry: solutions.entrySet()){
						NotPredicate notPred = new NotPredicate(entry.getKey());
						solutions.put(notPred, solution);
					}
				}
				else if(solutions.entrySet().size() == 2){
					OrPredicate orPred = new OrPredicate();
					Iterator<Entry<IPredicate<RelationalTuple<?>>, ISolution>> iter = solutions.entrySet().iterator();
					
					orPred.setLeft(iter.next().getKey());
					orPred.setRight(iter.next().getKey());
					
					NotPredicate notPred = new NotPredicate(orPred);
					solutions.put(notPred, solution);
				}
				else if(solutions.entrySet().size() > 2){
					Iterator<Entry<IPredicate<RelationalTuple<?>>, ISolution>> iter = solutions.entrySet().iterator();
					
					ArrayList<IPredicate<RelationalTuple<?>>> listOfPreds = new ArrayList<IPredicate<RelationalTuple<?>>>();
					for(Entry<IPredicate<RelationalTuple<?>>, ISolution> entry: solutions.entrySet()){
						listOfPreds.add(entry.getKey());
					}

					OrPredicate orPred = new OrPredicate();
					orPred.setLeft(listOfPreds.get(0));
					orPred.setRight(listOfPreds.get(1));
					
					for(int u = 2; u<listOfPreds.size(); u++){
						OrPredicate subOr = new OrPredicate();
						subOr.setLeft(listOfPreds.get(u));
						subOr.setRight(orPred);
						orPred = subOr;
					}
					
					NotPredicate notPred = new NotPredicate(orPred);
					solutions.put(notPred, solution);
				}
				
			}
		}
		
		return solutions;
	}

	@Override
	public Object visit(ASTConditionSolution node, Object data) {
		// first get the condition
		IPredicate condition = (IPredicate)node.jjtGetChild(0).jjtAccept(this, (IAttributeResolver)data);
		
		// second get the solutions
		ISolution solution = (ISolution)node.jjtGetChild(1).jjtAccept(this, (IAttributeResolver)data);
		
		ArrayList condSolCombi = new ArrayList();
		condSolCombi.add(condition);
		condSolCombi.add(solution);
		
		return condSolCombi;
	}

	@Override
	public Object visit(ASTCondition node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, (IAttributeResolver)data);
	}

	@Override
	public Object visit(ASTComplexCondition node, Object data) {
		
		//get the first simple condition
		ArrayList<IPredicate> predicates = new ArrayList<IPredicate>();
		for(int i = 0; i<node.jjtGetNumChildren(); i++){
			IPredicate pred = (IPredicate)node.jjtGetChild(0).jjtAccept(this, data);
			predicates.add(pred);
		}
		
		return buildAndPredicate(predicates);
	}
	
	/**
	 * This method recursively concatenates predicates from a list by the and
	 * operator. So an AndPredicate over all contained predicates is returned.
	 * @param predicates
	 * @return
	 */
	private IPredicate buildAndPredicate(ArrayList<IPredicate> predicates){
		IPredicate pred;
		if(predicates.size() > 1){
			AndPredicate andPred = new AndPredicate();
			andPred.setLeft(predicates.remove(0));
			andPred.setRight(buildAndPredicate(predicates));
			pred = andPred;
		}
		else{
			pred = predicates.get(0);
		}
		
		return pred;
	}

	@Override
	public Object visit(ASTSimpleCondition node, Object data) {
		String expression = node.toString();
		SDFExpression expr = new SDFExpression(null, expression, (IAttributeResolver)data);
		RelationalPredicate predicate = new RelationalPredicate(expr);
		
		return predicate;
	}

	@Override
	public Object visit(ASTCompareOperator node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTSolution node, Object data) {
		// TODO Auto-generated method stub
		if(node.isEmpty()){
			return new Solution(null, null, null);
		}
		
		return node.jjtGetChild(0).jjtAccept(this, (IAttributeResolver)data);
	}

	@Override
	public Object visit(ASTSimpleSolution node, Object data) {
		// go to SimpleSolutionContent
		return node.jjtGetChild(0).jjtAccept(this, (IAttributeResolver)data);
	}

	@Override
	public Object visit(ASTSimpleSolutionContent node, Object data) {
		// Children: expression, compareOperator, expression
		String leftExpression = node.jjtGetChild(0).toString();
		String compareOperator = node.jjtGetChild(1).toString();
		String rightExpression = node.jjtGetChild(2).toString();
		
		// we are just looking for t
		if(leftExpression.trim().equals("t")){
			Solution solution = new Solution(
					new SDFExpression(null, leftExpression.trim(), (IAttributeResolver)data), 
					compareOperator, 
					new SDFExpression(null, rightExpression, (IAttributeResolver)data));
			return solution;
		}
		
		// the variable t is on the right side of
		// the comparison. However, we need a standardized
		// form, in which it has to be on the left side.
		// so, what we have to do is to change the compare operator
		else{
			if(compareOperator.equals("<")){
				compareOperator = ">";
			}
			else if(compareOperator.equals("<=")){
				compareOperator = ">=";
			}
			else if(compareOperator.equals(">")){
				compareOperator = "<";
			}
			else if(compareOperator.equals(">=")){
				compareOperator = "<=";
			}
			else{
				throw new IllegalArgumentException("Compare operator " + compareOperator + " not supported.");
			}
			
			Solution solution = new Solution(
					new SDFExpression(null, rightExpression.trim(), (IAttributeResolver)data), 
					compareOperator, 
					new SDFExpression(null, leftExpression, (IAttributeResolver)data));
			return solution;
		}
	}
	
	@Override
	public Object visit(ASTFullSolution node, Object data) {
		// the first child is the identifier
		return new Solution(
				new SDFExpression(null, node.jjtGetChild(0).toString(), (IAttributeResolver)data), 
				null, 
				null);
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTFunctionExpression node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTFunctionName node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTNumber node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTString node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTEmptySolution node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
