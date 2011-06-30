/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTMaple;
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
import de.uniol.inf.is.odysseus.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * This visitor traverses the abstract syntax tree of a maple result and creates
 * a map containing as keys the conditions of the solutions and as values the
 * coresponding solutions for the variable t.
 * 
 * @author Andr� Bolles
 * 
 */
public class CreateExpressionMapVisitor implements MapleResultParserVisitor {

	@Override
	public Object visit(SimpleNode node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTMaple node, Object data) {
		if (node.jjtGetChild(0) instanceof ASTSolution) {
			ISolution solution = (ISolution) node.jjtGetChild(0).jjtAccept(
					this, data);
			Map<IPredicate<RelationalTuple<?>>, ISolution> solutions = new HashMap<IPredicate<RelationalTuple<?>>, ISolution>();
			// TODO: Funktioniert das wirklich oder ist das TruePredicate in
			// Konkurrenz zum
			// default predicate des prediction assign operators?
			// Nein, kein Problem. Die default prediction function mapped auf
			// ein
			// RangePredicate. Das RangePredicate wiederum enth�lt eine Map
			// mit Pr�dikaten und Solutions. Die TruePredicates befinden sich
			// also
			// in zwei verschiedenen Maps.
			solutions.put(new TruePredicate(), solution);
			return solutions;
		} else {
			return node.jjtGetChild(0).jjtAccept(this, data);
		}
	}

	@Override
	public Object visit(ASTMaplePiecewise node, Object data) {
		Map<IPredicate<RelationalTuple<?>>, ISolution> solutions = new HashMap<IPredicate<RelationalTuple<?>>, ISolution>();

		// the children of this node are the condition/solution combinations

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			if (node.jjtGetChild(i) instanceof ASTConditionSolution) {
				ArrayList condSolCombi = (ArrayList) node.jjtGetChild(i)
						.jjtAccept(this, data);
				solutions.put((IPredicate) condSolCombi.get(0),
						(ISolution) condSolCombi.get(1));
			}
			// the "otherwise" part of the maple solution
			// this can only be true, for the last child, so
			// we can read from the EntrySet of the map, since
			// all elements must already be in this map.
			else if (node.jjtGetChild(i) instanceof ASTSolution) {
				ISolution solution = (ISolution) node.jjtGetChild(i).jjtAccept(
						this, data);
				// create a NotPredicate(OrPredicate) from all other predicates
				// all the other predicates must already be in the map
				if (solutions.entrySet().size() == 1) {
					for (Entry<IPredicate<RelationalTuple<?>>, ISolution> entry : solutions
							.entrySet()) {
						IPredicate notPred = ComplexPredicateHelper
								.createNotPredicate(entry.getKey());
						solutions.put(notPred, solution);
					}
				} else if (solutions.entrySet().size() == 2) {
					Iterator<Entry<IPredicate<RelationalTuple<?>>, ISolution>> iter = solutions
							.entrySet().iterator();
					IPredicate orPred = ComplexPredicateHelper
							.createOrPredicate(iter.next().getKey(), iter
									.next().getKey());

					IPredicate notPred = ComplexPredicateHelper
							.createNotPredicate(orPred);
					solutions.put(notPred, solution);
				} else if (solutions.entrySet().size() > 2) {
					Iterator<Entry<IPredicate<RelationalTuple<?>>, ISolution>> iter = solutions
							.entrySet().iterator();

					ArrayList<IPredicate<RelationalTuple<?>>> listOfPreds = new ArrayList<IPredicate<RelationalTuple<?>>>();
					for (Entry<IPredicate<RelationalTuple<?>>, ISolution> entry : solutions
							.entrySet()) {
						listOfPreds.add(entry.getKey());
					}

					IPredicate orPred = ComplexPredicateHelper
							.createOrPredicate(listOfPreds.get(0),
									listOfPreds.get(1));

					for (int u = 2; u < listOfPreds.size(); u++) {
						IPredicate subOr = ComplexPredicateHelper
								.createOrPredicate(listOfPreds.get(u), orPred);
						orPred = subOr;
					}

					IPredicate notPred = ComplexPredicateHelper
							.createNotPredicate(orPred);
					solutions.put(notPred, solution);
				}

			}
		}

		return solutions;
	}

	@Override
	public Object visit(ASTConditionSolution node, Object data) {
		// first get the condition
		IPredicate condition = (IPredicate) node.jjtGetChild(0).jjtAccept(this,
				(IAttributeResolver) data);

		// second get the solutions
		ISolution solution = (ISolution) node.jjtGetChild(1).jjtAccept(this,
				(IAttributeResolver) data);

		ArrayList condSolCombi = new ArrayList();
		condSolCombi.add(condition);
		condSolCombi.add(solution);

		return condSolCombi;
	}

	@Override
	public Object visit(ASTCondition node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, (IAttributeResolver) data);
	}

	@Override
	public Object visit(ASTComplexCondition node, Object data) {

		// get the first simple condition
		ArrayList<IPredicate> predicates = new ArrayList<IPredicate>();
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			IPredicate pred = (IPredicate) node.jjtGetChild(0).jjtAccept(this,
					data);
			predicates.add(pred);
		}

		return buildAndPredicate(predicates);
	}

	/**
	 * This method recursively concatenates predicates from a list by the and
	 * operator. So an AndPredicate over all contained predicates is returned.
	 * 
	 * @param predicates
	 * @return
	 */
	private IPredicate buildAndPredicate(ArrayList<IPredicate> predicates) {
		IPredicate pred;
		if (predicates.size() > 1) {
			pred = ComplexPredicateHelper.createAndPredicate(
					predicates.remove(0), buildAndPredicate(predicates));
		} else {
			pred = predicates.get(0);
		}

		return pred;
	}

	@Override
	public Object visit(ASTSimpleCondition node, Object data) {
		String expression = node.toString();
		SDFExpression expr = new SDFExpression(null, expression,
				(IAttributeResolver) data);
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
		if (node.isEmpty()) {
			return new Solution(null, null, null);
		}

		return node.jjtGetChild(0).jjtAccept(this, (IAttributeResolver) data);
	}

	@Override
	public Object visit(ASTSimpleSolution node, Object data) {
		// go to SimpleSolutionContent
		return node.jjtGetChild(0).jjtAccept(this, (IAttributeResolver) data);
	}

	@Override
	public Object visit(ASTSimpleSolutionContent node, Object data) {
		// Children: expression, compareOperator, expression
		String leftExpression = node.jjtGetChild(0).toString();
		String compareOperator = node.jjtGetChild(1).toString();
		String rightExpression = node.jjtGetChild(2).toString();

		// we are just looking for t
		// because for the right brackets it could also be "(t)"
		if (leftExpression.trim().equals("t")
				|| leftExpression.trim().equals("(t)")) {
			Solution solution = new Solution(new SDFExpression(null,
					leftExpression.trim(), (IAttributeResolver) data),
					compareOperator, new SDFExpression(null, rightExpression,
							(IAttributeResolver) data));
			return solution;
		}

		// the variable t is on the right side of
		// the comparison. However, we need a standardized
		// form, in which it has to be on the left side.
		// so, what we have to do is to change the compare operator
		else {
			if (compareOperator.equals("<")) {
				compareOperator = ">";
			} else if (compareOperator.equals("<=")) {
				compareOperator = ">=";
			} else if (compareOperator.equals(">")) {
				compareOperator = "<";
			} else if (compareOperator.equals(">=")) {
				compareOperator = "<=";
			} else {
				throw new IllegalArgumentException("Compare operator "
						+ compareOperator + " not supported.");
			}

			Solution solution = new Solution(new SDFExpression(null,
					rightExpression.trim(), (IAttributeResolver) data),
					compareOperator, new SDFExpression(null, leftExpression,
							(IAttributeResolver) data));
			return solution;
		}
	}

	@Override
	public Object visit(ASTFullSolution node, Object data) {
		// the first child is the identifier
		return new Solution(new SDFExpression(null, node.jjtGetChild(0)
				.toString(), (IAttributeResolver) data), null, null);
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
