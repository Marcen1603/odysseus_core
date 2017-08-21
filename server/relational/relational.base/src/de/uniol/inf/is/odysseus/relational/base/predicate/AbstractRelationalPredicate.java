/**
 * 
 */
package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.IBinaryOperator;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @deprecated 
 */
public abstract class AbstractRelationalPredicate<T extends Tuple<?>> extends AbstractPredicate<T>
		implements IRelationalPredicate<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5696868093867495724L;

	private Logger logger = LoggerFactory.getLogger(RelationalPredicate.class);

	protected final SDFExpression expression;

	// stores which attributes are needed from which input at which position for
	// variable bindings
	protected Pair<Integer, Integer>[] attributePositions;

	protected final List<SDFAttribute> neededAttributes;

	// fromRightChannel[i] stores if the getAttribute(attributePositions[i])
	// should be called on the left or on the right input tuple
	protected boolean[] fromRightChannel;

	protected Map<SDFAttribute, SDFAttribute> replacementMap = new HashMap<SDFAttribute, SDFAttribute>();

	protected SDFSchema leftSchema;
	protected SDFSchema rightSchema;

	/**
	 * 
	 */
	public AbstractRelationalPredicate(SDFExpression expression) {
		this.expression = expression;
		this.neededAttributes = expression.getAllAttributes();
	}

	public AbstractRelationalPredicate(AbstractRelationalPredicate<T> predicate) {
		this.attributePositions = predicate.attributePositions == null ? null
				: (Pair<Integer, Integer>[]) predicate.attributePositions.clone();
		this.fromRightChannel = predicate.fromRightChannel == null ? null
				: (boolean[]) predicate.fromRightChannel.clone();
		this.expression = predicate.expression == null ? null : predicate.expression.clone();
		this.replacementMap = new HashMap<SDFAttribute, SDFAttribute>(predicate.replacementMap);
		this.neededAttributes = new ArrayList<SDFAttribute>(predicate.neededAttributes);
		// logger.debug("Cloned "+this+ " "+attributePositions);
	}

	/**
	 * @return the expression
	 */
	public SDFExpression getExpression() {
		return this.expression;
	}

	@Override
	public List<SDFAttribute> getAttributes() {
		return neededAttributes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
		init(leftSchema, rightSchema, true);
	}

	public void init(List<SDFSchema> schema, boolean checkRightSchema) {
		if (schema.size() == 1) {
			init(schema.get(0), null, checkRightSchema);
		} else if (schema.size() == 2) {
			init(schema.get(0), schema.get(1), checkRightSchema);
		} else {
			throw new IllegalArgumentException("Predicate cannot have more than two input schema");
		}

	}

	@SuppressWarnings("unchecked")
	public void init(SDFSchema leftSchema, SDFSchema rightSchema, boolean checkRightSchema) {
		logger.debug("Init (" + this + "): Left " + leftSchema + " Right " + rightSchema);
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;

		List<SDFAttribute> neededAttributes = expression.getAllAttributes();
		this.attributePositions = new Pair[neededAttributes.size()];
		this.fromRightChannel = new boolean[neededAttributes.size()];

		int i = 0;
		for (SDFAttribute curAttr : neededAttributes) {
			SDFAttribute replOfCurAttr = this.getReplacement(curAttr);
			SDFAttribute curAttribute = leftSchema.findAttribute(replOfCurAttr.getURI());
			if (curAttribute == null && !checkRightSchema) {
				logger.error("Attribute " + curAttr.getURI() + " not found in " + leftSchema);
				throw new IllegalArgumentException("Attribute " + curAttr.getURI() + " not found in " + leftSchema);
			}
			int pos = leftSchema.indexOf(curAttribute);
			Pair<Integer, Integer> metaPos = null;
			if (pos == -1) {
				metaPos = leftSchema.indexOfMetaAttribute(curAttribute);

				if (metaPos == null && rightSchema == null && checkRightSchema) {

					throw new IllegalArgumentException(
							"Attribute " + curAttribute + " not in " + leftSchema + " and rightSchema is null!");
				}
				if (rightSchema != null && checkRightSchema) {
					// if you get here, there is an attribute
					// in the predicate that does not exist
					// in the left schema, so there must also be
					// a right schema
					curAttribute = rightSchema.findAttribute(replOfCurAttr.getURI());
					pos = rightSchema.indexOf(curAttribute);
					if (pos == -1) {
						metaPos = rightSchema.indexOfMetaAttribute(curAttribute);
						if (metaPos == null) {
							throw new IllegalArgumentException("Attribute " + curAttribute + " not in " + rightSchema);
						}
					}
				}
				this.fromRightChannel[i] = true;
			}
			if (pos != -1) {
				this.attributePositions[i++] = new Pair<Integer, Integer>(-1, pos);
			} else {
				this.attributePositions[i++] = metaPos;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean equals(IPredicate<T> pred) {

		if (pred instanceof AbstractRelationalPredicate) {
			if (this.expression.equals(((AbstractRelationalPredicate<T>) pred).expression)) {
				return true;
			}

			if (this.expression.getMEPExpression() instanceof IMepFunction) {
				IMepFunction thisFunction = (IMepFunction) expression.getMEPExpression();
				IMepExpression otherExpression =  ((AbstractRelationalPredicate<T>) pred).expression.getMEPExpression();

				if (thisFunction.cnfEquals(otherExpression)) {
					return true;
				}
			}

		}
		return this.isContainedIn(pred) && pred.isContainedIn(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
		if (!curAttr.equals(newAttr)) {
			replacementMap.put(curAttr, newAttr);
		} else {
			logger.warn("Replacement " + curAttr + " --> " + newAttr + " not added because they are equal!");
		}
	}

	public boolean isAndPredicate() {
		return expression.getMEPExpression().isFunction()
				&& expression.getMEPExpression().toFunction().getSymbol().equalsIgnoreCase("&&");
	}

	public boolean isOrPredicate() {
		return expression.getMEPExpression().isFunction()
				&& expression.getMEPExpression().toFunction().getSymbol().equalsIgnoreCase("||");
	}

	public boolean isNotPredicate() {
		return expression.getMEPExpression().isFunction()
				&& expression.getMEPExpression().toFunction() instanceof NotOperator;
	}

	/**
	 * Proxy for splitPredicate function.
	 * 
	 * @param init
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<IPredicate> splitPredicate(boolean init) {
		return splitPredicate();
	}

	@SuppressWarnings("rawtypes")
	public List<IPredicate> splitPredicate() {
		final List<IPredicate> result = new LinkedList<IPredicate>();
		if (this.isAndPredicate()) {
			final Stack<IMepExpression<?>> expressionStack = new Stack<IMepExpression<?>>();
			expressionStack.push(this.expression.getMEPExpression());

			while (!expressionStack.isEmpty()) {
				final IMepExpression<?> curExpression = expressionStack.pop();
				if (this.isAndExpression(curExpression)) {
					expressionStack.push(curExpression.toFunction().getArgument(0));
					expressionStack.push(curExpression.toFunction().getArgument(1));
				} else {
					final SDFExpression expr = new SDFExpression(curExpression.toString(), this.getExpression().getAttributeResolver(), MEP.getInstance());
					final RelationalPredicate relationalPredicate = new RelationalPredicate(expr);
					relationalPredicate.init(this.expression.getSchema(), false);
					result.add(relationalPredicate);
				}
			}
			return result;

		}
		result.add(this);
		return result;
	}

	/**
	 * Return true if the given relational predicate is of the form:
	 * 
	 * A.x=B.y AND A.y=B.z AND * ...
	 * 
	 * @return <code>true</code> iff the relational predicate is of the given
	 *         form
	 */
	public boolean isEquiPredicate() {
		Objects.requireNonNull(this.getExpression());
		Objects.requireNonNull(this.getExpression().getMEPExpression());
		final IMepExpression<?> expression = this.getExpression().getMEPExpression();
		return isEquiExpression(expression);
	}

	/**
	 * Return the map of attributes used in an equi predicate.
	 * 
	 * @param resolver
	 *            The attribute resolver
	 * @return The map of attributes
	 */
	public Map<SDFAttribute, List<SDFAttribute>> getEquiExpressionAtributes(final IAttributeResolver resolver) {
		Objects.requireNonNull(this.getExpression());
		Objects.requireNonNull(this.getExpression().getMEPExpression());
		final IMepExpression<?> expression = this.getExpression().getMEPExpression();
		return getEquiExpressionAtributes(expression, resolver);
	}

	/**
	 * Return the map of attributes used in an equi expression.
	 * 
	 * @param expression
	 *            The expression
	 * @param resolver
	 *            The attribute resolver
	 * @return The map of attributes
	 */
	private Map<SDFAttribute, List<SDFAttribute>> getEquiExpressionAtributes(final IMepExpression<?> expression,
			final IAttributeResolver resolver) {
		Objects.requireNonNull(expression);
		Objects.requireNonNull(resolver);
		final Map<SDFAttribute, List<SDFAttribute>> attributes = new HashMap<SDFAttribute, List<SDFAttribute>>();
		if ((expression.isFunction()) && (expression.toFunction().getSymbol().equalsIgnoreCase("&&"))) {
			final Map<SDFAttribute, List<SDFAttribute>> leftAttributes = getEquiExpressionAtributes(
					((AndOperator) expression).getArgument(0), resolver);
			for (final SDFAttribute key : leftAttributes.keySet()) {
				if (!attributes.containsKey(key)) {
					attributes.put(key, new ArrayList<SDFAttribute>());
				}
				attributes.get(key).addAll(leftAttributes.get(key));
			}
			final Map<SDFAttribute, List<SDFAttribute>> rigthAttributes = getEquiExpressionAtributes(
					((AndOperator) expression).getArgument(1), resolver);
			for (final SDFAttribute key : rigthAttributes.keySet()) {
				if (!attributes.containsKey(key)) {
					attributes.put(key, new ArrayList<SDFAttribute>());
				}
				attributes.get(key).addAll(rigthAttributes.get(key));
			}
		}
		if ((expression.isFunction()) && (expression.toFunction().getSymbol().equalsIgnoreCase("=="))) {
			final IBinaryOperator<?> eq = (IBinaryOperator<?>) expression;
			final IMepExpression<?> arg1 = eq.getArgument(0);
			final IMepExpression<?> arg2 = eq.getArgument(1);
			if ((arg1 instanceof IMepVariable) && (arg2 instanceof IMepVariable)) {
				final SDFAttribute key = resolver.getAttribute(((IMepVariable) arg1).getIdentifier());
				if (!attributes.containsKey(key)) {
					attributes.put(key, new ArrayList<SDFAttribute>());
				}
				attributes.get(key).add(resolver.getAttribute(((IMepVariable) arg2).getIdentifier()));
			}
		}
		return attributes;
	}

	/**
	 * Return true if the given expression is of the form:
	 * 
	 * A.x=B.y AND A.y=B.z AND * ...
	 * 
	 * @param expression
	 *            The expression
	 * @return <code>true</code> iff the expression is of the given form
	 */
	private boolean isEquiExpression(final IMepExpression<?> expression) {
		Objects.requireNonNull(expression);
		if (expression instanceof AndOperator) {
			return isEquiExpression(((AndOperator) expression).getArgument(0))
					&& isEquiExpression(((AndOperator) expression).getArgument(1));

		}
		if ((expression.isFunction()) && (expression.toFunction().getSymbol().equalsIgnoreCase("=="))) {
			final IBinaryOperator<?> eq = (IBinaryOperator<?>) expression;
			final IMepExpression<?> arg1 = eq.getArgument(0);
			final IMepExpression<?> arg2 = eq.getArgument(1);
			if ((arg1 instanceof IMepVariable) && (arg2 instanceof IMepVariable)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isContainedIn(IPredicate<?> o) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.expression.toString();
	}

	protected boolean isAndExpression(IMepExpression<?> expression) {
		return expression.isFunction() && expression.toFunction().getSymbol().equalsIgnoreCase("&&");
	}

	// private int indexOf(SDFSchema schema, SDFAttribute attr) {
	// SDFAttribute cqlAttr = getReplacement(attr);
	// Iterator<SDFAttribute> it = schema.iterator();
	// for (int i = 0; it.hasNext(); ++i) {
	// SDFAttribute a = it.next();
	// if (cqlAttr.equalsCQL(a)) {
	// return i;
	// }
	// }
	// return -1;
	// }

	private SDFAttribute getReplacement(SDFAttribute a) {
		SDFAttribute ret = a;
		SDFAttribute tmp = null;
		while ((tmp = replacementMap.get(ret)) != null) {
			ret = getReplacement(tmp);
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<T> and(IPredicate<T> predicate) {
		return new AndPredicate<>(this, predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<T> or(IPredicate<T> predicate) {
		return new OrPredicate<>(this, predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<T> not() {
		return new NotPredicate<>(this);

	}
}
