package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.nfunk.jep.ParseException;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.base.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.ExistenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnionAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.ExistenceAO.Type;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAllPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAnyPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTComplexSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTElementPriorities;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTExists;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTWhereClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.AbstractQuantificationPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.IExistencePredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.CQLAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

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

	public CreateJoinAOVisitor() {
		super();
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
	public Object visit(ASTElementPriorities node, Object data) {
		return data;
	}

	private Object createJoin(String alias, Object data) {
		final AbstractLogicalOperator source = this.attributeResolver
				.getSource(alias);

		if (data == null) {
			return source;
		}

		AbstractLogicalOperator leftSource = (AbstractLogicalOperator) data;
		JoinAO join = new JoinAO();

		join.subscribeTo(leftSource, 0, 0, leftSource.getOutputSchema());
		join.subscribeTo(source, 1, 0, source.getOutputSchema());
		return join;
	}

	@SuppressWarnings("unchecked")
	private IPredicate<? super RelationalTuple<?>> pushDownNegation(
			IPredicate<? super RelationalTuple<?>> pred, boolean negatived) {
		if (pred instanceof NotPredicate) {
			return pushDownNegation(((NotPredicate<RelationalTuple>) pred)
					.getChild(), !negatived);
		}
		if (pred instanceof ComplexPredicate) {
			ComplexPredicate<RelationalTuple<?>> compPred = (ComplexPredicate<RelationalTuple<?>>) pred;
			if (negatived) {
				if (pred instanceof OrPredicate) {
					compPred = new AndPredicate<RelationalTuple<?>>();
				} else {
					compPred = new OrPredicate<RelationalTuple<?>>();
				}
			}
			compPred.setLeft(pushDownNegation(compPred.getLeft(), negatived));
			compPred.setRight(pushDownNegation(compPred.getRight(), negatived));
			return compPred;
		}
		if (negatived) {
			return new NotPredicate<RelationalTuple<?>>(pred);
		} else {
			return pred;
		}
	}

	@SuppressWarnings("unchecked")
	private List<IPredicate<RelationalTuple<?>>> splitPredicate(
			IPredicate<RelationalTuple<?>> predicate) {
		LinkedList<IPredicate<RelationalTuple<?>>> result = new LinkedList<IPredicate<RelationalTuple<?>>>();
		Stack<IPredicate<RelationalTuple<?>>> predicateStack = new Stack<IPredicate<RelationalTuple<?>>>();
		predicateStack.push(predicate);
		while (!predicateStack.isEmpty()) {
			IPredicate<RelationalTuple<?>> curPredicate = predicateStack.pop();
			if (curPredicate instanceof AndPredicate) {
				predicateStack
						.push((IPredicate<RelationalTuple<?>>) ((AndPredicate) curPredicate)
								.getLeft());
				predicateStack
						.push((IPredicate<RelationalTuple<?>>) ((AndPredicate) curPredicate)
								.getRight());
			} else {
				result.add(curPredicate);
			}
		}
		return result;
	}

	@Override
	public Object visit(ASTWhereClause node, Object data) {
		AbstractLogicalOperator inputOp = (AbstractLogicalOperator) data;
		ASTPredicate wherePredicate = (ASTPredicate) node.jjtGetChild(0);
		IPredicate<RelationalTuple<?>> predicate;
		predicate = CreatePredicateVisitor.toPredicate(wherePredicate,
				this.attributeResolver);
		pushDownNegation(predicate, false);
		List<IPredicate<RelationalTuple<?>>> conjunctivePredicates = splitPredicate(predicate);

		AbstractLogicalOperator curInputAO = inputOp;
		Iterator<IPredicate<RelationalTuple<?>>> it = conjunctivePredicates
				.iterator();
		IPredicate<RelationalTuple<?>> selectPredicate = null;
		while (it.hasNext()) {
			IPredicate<RelationalTuple<?>> next = it.next();
			if (containsQuantification(next)) {
				curInputAO = createQuantificationPlan(curInputAO, next);
			} else {
				if (selectPredicate == null) {
					selectPredicate = next;
				} else {
					selectPredicate = new AndPredicate<RelationalTuple<?>>(
							selectPredicate, next);
				}
			}
		}

		if (selectPredicate != null) {
			SelectAO selectAO = new SelectAO();
			selectAO.subscribeTo(curInputAO,curInputAO.getOutputSchema());
			selectAO.setPredicate(selectPredicate);
			return selectAO;
		}

		return curInputAO;
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

			if (pred instanceof AndPredicate) {
				if (left instanceof SelectAO) {
					if (right instanceof SelectAO) {
						((SelectAO) left).setPredicate(new AndPredicate(left
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
			if (pred instanceof OrPredicate) {
				if (left instanceof SelectAO) {
					if (right instanceof SelectAO) {
						((SelectAO) left).setPredicate(new OrPredicate(left
								.getPredicate(), right.getPredicate()));
						return left;
					}
				}
				UnionAO result = new UnionAO();
				result.subscribeTo(left, 0, 0, left.getOutputSchema());
				result.subscribeTo(right, 1, 0, right.getOutputSchema());
				return result;
			}
			throw new IllegalArgumentException("unsupported predicate type");
		}
		boolean negatived = false;
		IPredicate tmpPred = pred;
		if (pred instanceof NotPredicate) {
			tmpPred = ((NotPredicate) pred).getChild();
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
			for (LogicalSubscription l : curOp.getSubscribedTo(oldInput)) {
				l.getTarget().unsubscribe(curOp, l.getSinkPort(),
						l.getSourcePort());
				replacement
						.subscribe(curOp, l.getSinkPort(), l.getSourcePort());
			}
		}
	}

	private void getBottomOperators(ILogicalOperator op,
			ILogicalOperator bottom, List<ILogicalOperator> ops) {
		for (LogicalSubscription s : op.getSubscribedTo()) {
			ILogicalOperator curInput = s.getTarget();
			if (curInput == bottom) {
				ops.add(op);
				return;
			} else {
				getBottomOperators(curInput, bottom, ops);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private boolean containsQuantification(IPredicate<RelationalTuple<?>> pred) {
		if (pred instanceof QuantificationPredicate) {
			return true;
		}
		if (pred instanceof ComplexPredicate) {
			return containsQuantification(((ComplexPredicate) pred).getLeft())
					|| containsQuantification(((ComplexPredicate) pred)
							.getRight());
		}
		if (pred instanceof NotPredicate) {
			return containsQuantification(((NotPredicate) pred).getChild());
		}
		return false;
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
			return outputSchema.get(0).getURI(false);
		}
		AttributeResolver tmpResolver = new AttributeResolver();
		StringBuffer buffer = new StringBuffer();
		buffer.append('[');
		for (int i = 0; i < outputSchema.size(); ++i) {
			if (i > 0) {
				buffer.append(',');
			}
			CQLAttribute attribute = (CQLAttribute) outputSchema
					.getAttribute(i);
			buffer.append(attribute.toString());
			tmpResolver.addAttribute(attribute);
		}
		buffer.append(']');
		return buffer.toString();
	}

	private AbstractLogicalOperator subquery(ASTComplexSelectStatement query) {
		CQLParser v = new CQLParser();
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
		existsAO.setLeftInput(inputAO, inputAO.getOutputSchema());
		AbstractLogicalOperator subquery = subquery(node.getQuery());
		if (node.getTuple().jjtGetNumChildren() != subquery.getOutputSchema()
				.size()) {
			throw new IllegalArgumentException(
					"wrong number of outputs in predicate subquery");
		}
		existsAO.setRightInput(subquery, subquery.getOutputSchema());
		existsAO.setType(type);
		try {
			String expression = node.getTuple().toString()
					+ node.getCompareOperator().toString()
					+ toExpression(subquery);
			AttributeResolver tmpResolver = new AttributeResolver(
					this.attributeResolver);
			for (SDFAttribute attr : subquery.getOutputSchema()) {
				tmpResolver.addAttribute((CQLAttribute) attr);
			}
			RelationalPredicate predicate = new RelationalPredicate(
					new SDFExpression("", expression, tmpResolver));
			existsAO.setPredicate(predicate);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
		return existsAO;
	}

	@Override
	public Object visit(ASTExists node, Object data) {
		AbstractLogicalOperator inputAO = (AbstractLogicalOperator) data;
		ExistenceAO existsAO = new ExistenceAO();
		existsAO.setLeftInput(inputAO, inputAO.getOutputSchema());
		existsAO.setRightInput(subquery(node.getQuery()), subquery(node.getQuery()).getOutputSchema());
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

}
