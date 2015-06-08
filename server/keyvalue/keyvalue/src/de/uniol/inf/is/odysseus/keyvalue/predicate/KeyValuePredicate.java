package de.uniol.inf.is.odysseus.keyvalue.predicate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

public class KeyValuePredicate<T extends KeyValueObject<?>> extends AbstractPredicate<T> {

	private static final long serialVersionUID = 6578575151834596318L;

	protected SDFExpression expression;
	final List<SDFAttribute> neededAttributes;
	// stores which attributes are needed at which position for
	// variable bindings
	protected int[] attributePositions;
	protected Map<SDFAttribute, SDFAttribute> replacementMap = new HashMap<SDFAttribute, SDFAttribute>();
	protected SDFSchema leftSchema;
	protected SDFSchema rightSchema;

	public KeyValuePredicate(SDFExpression expression) {
		this.expression = expression;
		this.neededAttributes = expression.getAllAttributes();		
	}
	
	public KeyValuePredicate(KeyValuePredicate<T> predicate) {
		this.expression = predicate.expression == null ? null : predicate.expression.clone();
		this.replacementMap = new HashMap<SDFAttribute, SDFAttribute>(predicate.replacementMap);
		this.neededAttributes = expression.getAllAttributes();
	}
	
	@Override
	public boolean evaluate(T input) {
		Object[] values = new Object[neededAttributes.size()];
		for (int i = 0; i < values.length; ++i) {
			values[i] = input.getAttribute(neededAttributes.get(i).getURI());
		}
//		this.expression.bindMetaAttribute(input.getMetadata());
//		this.expression.bindAdditionalContent(input.getAdditionalContent());
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public boolean evaluate(T left, T right) {
		Object[] values = new Object[neededAttributes.size()];
		for (int i = 0; i < values.length; ++i) {
			values[i] = left.getAttribute(neededAttributes.get(i).getURI());
			if (values[i]==null){
				values[i] = right.getAttribute(neededAttributes.get(i).getURI());
			}
		}
//		Map<String, Serializable> additionalContent = new HashMap<String, Serializable>();
//        additionalContent.putAll(left.getAdditionalContent());
//        additionalContent.putAll(right.getAdditionalContent());
        
//        this.expression.bindAdditionalContent(additionalContent);
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public AbstractPredicate<T> clone() {
		return new KeyValuePredicate<T>(this);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> and(IPredicate<T> predicate) {
        if (predicate instanceof KeyValuePredicate) {
            SDFExpression expr = ((KeyValuePredicate<T>) predicate).expression;
            AndOperator and = new AndOperator();
            and.setArguments(new IExpression<?>[] { expression.getMEPExpression(), expr.getMEPExpression() });
            return new KeyValuePredicate<>(new SDFExpression(and.toString(), expression.getExpressionParser()));
        }
        return super.and(predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> or(IPredicate<T> predicate) {
        if (predicate instanceof KeyValuePredicate) {
            SDFExpression expr = ((KeyValuePredicate<T>) predicate).expression;
            OrOperator or = new OrOperator();
            or.setArguments(new IExpression<?>[] { expression.getMEPExpression(), expr.getMEPExpression() });
            return new KeyValuePredicate<>(new SDFExpression(or.toString(), expression.getExpressionParser()));
        }
        return super.or(predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> not() {
        NotOperator not = new NotOperator();
        not.setArguments(new IExpression<?>[] { expression.getMEPExpression() });
        return new KeyValuePredicate<>(new SDFExpression(not, expression.getAttributeResolver(), expression.getExpressionParser()));
    }
}
