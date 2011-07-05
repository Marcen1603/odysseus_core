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
package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.ExistenceAO.Type;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAllPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAnyPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTComplexSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDBSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTElementPriorities;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTExists;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTGroupByClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHavingClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTWhereClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.AbstractQuantificationPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.IExistencePredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.usermanagement.User;

//creates join operators
//visit returns the topmost operator of an operator tree:
//- the topmost join, if there are multiple sources and no filter predicates
//- a select operator, if there are filter predicates
//- a source, if there are no filter predicates and only one source
/**
 * @author Jonas Jacobi
 */
public class CreateJoinAOVisitor extends AbstractDefaultVisitor {

	private AttributeResolver attributeResolver;
	private User caller;
	private IDataDictionary dataDictionary;

	public CreateJoinAOVisitor(User caller, IDataDictionary dd) {
		super();
		this.caller = caller;
		this.dataDictionary = dd;
	}

	public void init(AttributeResolver attributeResolver) {
		this.attributeResolver = attributeResolver;
	}

	@Override
	public Object visit(ASTSubselect node, Object data) {
		return createJoin(node.getAlias(), data);
	}

	@Override
	public Object visit(ASTSimpleSource node, Object data) {
		String name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		if (node.hasAlias()) {
			name = node.getAlias();
		}

		return createJoin(name, data);
	}
	
	@Override
	public Object visit(ASTGroupByClause node, Object data) {
		return data;
	}
	
	@Override
	public Object visit(ASTHavingClause node, Object data) {
		//don't visit having clause and ensure that the correct return value is returned.
		//TODO inside the having clause some astelements can return null on visit instead of data
		//as this is consistent in the abstractdefaultvisitor
		return data;
	}

	@Override
	public Object visit(ASTElementPriorities node, Object data) {
		return data;
	}

	private Object createJoin(String alias, Object data) {
		final ILogicalOperator source = this.attributeResolver
				.getSource(alias);

		if (data == null) {
			return source;
		}

		AbstractLogicalOperator leftSource = (AbstractLogicalOperator) data;
		JoinAO join = new JoinAO();
		
		join.subscribeToSource(leftSource, 0, 0, leftSource.getOutputSchema());
		join.subscribeToSource(source, 1, 0, source.getOutputSchema());
		return join;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTWhereClause node, Object data) {
		AbstractLogicalOperator inputOp = (AbstractLogicalOperator) data;
		ASTPredicate wherePredicate = (ASTPredicate) node.jjtGetChild(0);
		IPredicate<RelationalTuple<?>> predicate;
		predicate = CreatePredicateVisitor.toPredicate(wherePredicate,
				this.attributeResolver);
		predicate = ComplexPredicateHelper.pushDownNegation(predicate, false);
		List<IPredicate> conjunctivePredicates = ComplexPredicateHelper.splitPredicate(predicate);

		AbstractLogicalOperator curInputAO = inputOp;
		Iterator<?> it = conjunctivePredicates
				.iterator();
		IPredicate<?> selectPredicate = null;
		while (it.hasNext()) {
			IPredicate<RelationalTuple<?>> next = (IPredicate<RelationalTuple<?>>) it.next();
			if (containsQuantification(next)) {
				curInputAO = createQuantificationPlan(curInputAO, next);
			} else {
				if (selectPredicate == null) {
					selectPredicate = next;
				} else {
					selectPredicate = ComplexPredicateHelper.createAndPredicate(selectPredicate, next);
				}
			}
		}

		if (selectPredicate != null) {
			// Convert Predicate to single predicate
			String pred = selectPredicate.toString();
			System.out.println(pred);
			SDFExpression expression = new SDFExpression("",pred,this.attributeResolver);
			RelationalPredicate relSelPred = new RelationalPredicate(expression);
			SelectAO selectAO = new SelectAO();
			selectAO.subscribeTo(curInputAO, curInputAO.getOutputSchema());
			selectAO.setPredicate(relSelPred);
			return selectAO;
		}

		return curInputAO;
	}
	
	@SuppressWarnings("unchecked")
	public boolean containsQuantification(IPredicate pred) {
		if (pred instanceof QuantificationPredicate) {
			return true;
		}
		if (pred instanceof ComplexPredicate) {
			return containsQuantification(((ComplexPredicate) pred).getLeft())
					|| containsQuantification(((ComplexPredicate) pred)
							.getRight());
		}
		if (ComplexPredicateHelper.isNotPredicate(pred)) {
			return containsQuantification(ComplexPredicateHelper.getChild(pred));
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private AbstractLogicalOperator createQuantificationPlan(
			AbstractLogicalOperator curInputAO,
			IPredicate<RelationalTuple<?>> pred) {
		if (pred instanceof ComplexPredicate) {
			AbstractLogicalOperator left = createQuantificationPlan(curInputAO,
					((ComplexPredicate) pred).getLeft());
			AbstractLogicalOperator right = createQuantificationPlan(
					curInputAO, ((ComplexPredicate) pred).getRight());

			if (ComplexPredicateHelper.isAndPredicate(pred)) {
				if (left instanceof SelectAO) {
					if (right instanceof SelectAO) {
						((SelectAO) left).setPredicate(ComplexPredicateHelper.createAndPredicate(left
								.getPredicate(), right.getPredicate()));
						return left;
					} else {
						replaceBottomOps(right, left, curInputAO);
						return right;
					}
				} else {
					replaceBottomOps(left, right, curInputAO);
					return left;
				}
			}
			if (ComplexPredicateHelper.isOrPredicate(pred)) {
				if (left instanceof SelectAO) {
					if (right instanceof SelectAO) {
						((SelectAO) left).setPredicate(ComplexPredicateHelper.createOrPredicate(left
								.getPredicate(), right.getPredicate()));
						return left;
					}
				}
				UnionAO result = new UnionAO();
				result.subscribeToSource(left, 0, 0, left.getOutputSchema());
				result.subscribeToSource(right, 1, 0, right.getOutputSchema());
				return result;
			}
			throw new IllegalArgumentException("unsupported predicate type");
		}
		boolean negatived = false;
		IPredicate tmpPred = pred;
		if (ComplexPredicateHelper.isNotPredicate(pred)) {
			tmpPred = ComplexPredicateHelper.getChild(pred);
			negatived = true;
		}
		if (tmpPred instanceof QuantificationPredicate) {
			AbstractQuantificationPredicate astPredicate = ((QuantificationPredicate) tmpPred)
					.getAstPredicate();
			astPredicate.setNegatived(negatived);
			return (AbstractLogicalOperator) astPredicate.jjtAccept(this,
					curInputAO);
		} else {
			SelectAO selectAO = new SelectAO();
			selectAO.setPredicate(pred);
			selectAO.subscribeTo(curInputAO, curInputAO.getOutputSchema());
			return selectAO;
		}
	}

	private void replaceBottomOps(ILogicalOperator plan,
			ILogicalOperator replacement, ILogicalOperator oldInput) {
		List<ILogicalOperator> bottomOps = new LinkedList<ILogicalOperator>();
		getBottomOperators(plan, oldInput, bottomOps);
		for (ILogicalOperator curOp : bottomOps) {
			for (LogicalSubscription l : curOp.getSubscribedToSource(oldInput)) {
				l.getTarget().unsubscribeSink(curOp, l.getSinkInPort(),
						l.getSourceOutPort(), l.getSchema());
				replacement
						.subscribeSink(curOp, l.getSinkInPort(), l.getSourceOutPort(), replacement.getOutputSchema());
			}
		}
	}

	private void getBottomOperators(ILogicalOperator op,
			ILogicalOperator bottom, List<ILogicalOperator> ops) {
		for (LogicalSubscription s : op.getSubscribedToSource()) {
			ILogicalOperator curInput = s.getTarget();
			if (curInput == bottom) {
				ops.add(op);
				return;
			} else {
				getBottomOperators(curInput, bottom, ops);
			}
		}
	}

	@Override
	public Object visit(ASTAllPredicate node, Object data) {
		return createExistenceAO(node, (AbstractLogicalOperator) data, node
				.isNegatived() ? ExistenceAO.Type.EXISTS
				: ExistenceAO.Type.NOT_EXISTS);
	}

	private String toExpression(ILogicalOperator subquery) {
		SDFAttributeList outputSchema = subquery.getOutputSchema();
		if (outputSchema.size() == 1) {
			return outputSchema.get(0).getPointURI();
		}
		AttributeResolver tmpResolver = new AttributeResolver();
		StringBuffer buffer = new StringBuffer();
		buffer.append('[');
		for (int i = 0; i < outputSchema.size(); ++i) {
			if (i > 0) {
				buffer.append(',');
			}
			SDFAttribute attribute = (SDFAttribute) outputSchema
					.getAttribute(i);
			buffer.append(attribute.toPointString());
			tmpResolver.addAttribute(attribute);
		}
		buffer.append(']');
		return buffer.toString();
	}

	private AbstractLogicalOperator subquery(ASTComplexSelectStatement query) {
		CQLParser v = new CQLParser();
		v.setUser(caller);
		v.setDataDictionary(dataDictionary);
		return (AbstractLogicalOperator) v.visit(query, null);
	}

	@Override
	public Object visit(ASTAnyPredicate node, Object data) {
		return createExistenceAO(node, (AbstractLogicalOperator) data, node
				.isNegatived() ? ExistenceAO.Type.NOT_EXISTS
				: ExistenceAO.Type.EXISTS);
	}

	private ExistenceAO createExistenceAO(IExistencePredicate node,
			AbstractLogicalOperator inputAO, Type type) {
		ExistenceAO existsAO = new ExistenceAO();

		existsAO.subscribeToSource(inputAO, BinaryLogicalOp.LEFT, 0, inputAO
				.getOutputSchema());
		AbstractLogicalOperator subquery = subquery(node.getQuery());
		if (node.getTuple().jjtGetNumChildren() != subquery.getOutputSchema()
				.size()) {
			throw new IllegalArgumentException(
					"wrong number of outputs in predicate subquery");
		}
		existsAO.subscribeToSource(subquery, BinaryLogicalOp.RIGHT, 0, subquery
				.getOutputSchema());
		existsAO.setType(type);
		String expression = node.getTuple().toString()
				+ node.getCompareOperator().toString() + toExpression(subquery);
		AttributeResolver tmpResolver = new AttributeResolver(
				this.attributeResolver);
		for (SDFAttribute attr : subquery.getOutputSchema()) {
			tmpResolver.addAttribute((SDFAttribute) attr);
		}
		RelationalPredicate predicate = new RelationalPredicate(
				new SDFExpression("", expression, tmpResolver));
		existsAO.setPredicate(predicate);
		return existsAO;
	}

	@Override
	public Object visit(ASTExists node, Object data) {
		AbstractLogicalOperator inputAO = (AbstractLogicalOperator) data;
		ExistenceAO existsAO = new ExistenceAO();
		existsAO.subscribeToSource(inputAO, BinaryLogicalOp.LEFT, 0, inputAO
				.getOutputSchema());
		existsAO.subscribeToSource(subquery(node.getQuery()), BinaryLogicalOp.RIGHT,
				0, subquery(node.getQuery()).getOutputSchema());
		existsAO.setType(node.isNegatived() ? ExistenceAO.Type.NOT_EXISTS
				: ExistenceAO.Type.EXISTS);
		return existsAO;
	}

	@Override
	public Object visit(ASTInPredicate node, Object data) {
		return createExistenceAO(node, (AbstractLogicalOperator) data, node
				.isNegatived() ? ExistenceAO.Type.NOT_EXISTS
				: ExistenceAO.Type.EXISTS);
	}
	
	@Override
	public Object visit(ASTDBSelectStatement node, Object data) {
		try {
			Class<?> dbClass = Class
					.forName("de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateDatabaseAOVisitor");
			IDatabaseAOVisitor dbVisitor = (IDatabaseAOVisitor) dbClass
					.newInstance();
			return dbVisitor.visit(node, data);

		} catch (Exception e) {
			throw new RuntimeException("missing database plugin for cql parser");
		}


	}
	
	@Override	
	public Object visit(ASTBrokerSource node, Object data) {
		// same thing like simple-source
		Node child = node.jjtGetChild(0);
		ASTIdentifier ident = (ASTIdentifier) child.jjtGetChild(child.jjtGetNumChildren()-1);
		String name = ident.getName();			
		return createJoin(name, data);			
	}

}
