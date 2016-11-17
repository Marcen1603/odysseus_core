package de.uniol.inf.is.odysseus.keyvalue.predicate;

import java.util.List;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

/**
 * This predicate can be evaluated with a KeyValueObject. The attribute names in
 * the expression
 * must correspond to the keys in the KeyValueObject
 *
 * @author Marco Grawunder, Christian Kuka
 *
 */
public class KeyValueObjectPredicate extends AbstractPredicate<KeyValueObject<?>> {

    private static final long serialVersionUID = 2982916759647665469L;

    final SDFExpression expression;
    final List<SDFAttribute> neededAttributes;

    /**
     *
     * Class constructor.
     *
     * @param expression
     */
    public KeyValueObjectPredicate(SDFExpression expression) {
        this.expression = expression;
        this.neededAttributes = expression.getAllAttributes();
    }

    /**
     *
     * Class constructor.
     *
     * @param other
     */
    public KeyValueObjectPredicate(KeyValueObjectPredicate other) {
        this.expression = other.expression == null ? null : other.expression.clone();
        this.neededAttributes = this.expression.getAllAttributes();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
	public Boolean evaluate(KeyValueObject<?> input) {
        Object[] values = new Object[this.neededAttributes.size()];
        for (int i = 0; i < values.length; ++i) {
            values[i] = input.getAttribute(this.neededAttributes.get(i).getURI());
        }
//        this.expression.bindAdditionalContent(input.getAdditionalContent());
        this.expression.bindVariables(values);
		return ((Boolean) this.expression.getValue());
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
	public Boolean evaluate(KeyValueObject<?> left, KeyValueObject<?> right) {
        Object[] values = new Object[this.neededAttributes.size()];
        for (int i = 0; i < values.length; ++i) {
            values[i] = left.getAttribute(this.neededAttributes.get(i).getURI());
            if (values[i] == null) {
                values[i] = right.getAttribute(this.neededAttributes.get(i).getURI());
            }
        }
        this.expression.bindVariables(values);
		return ((Boolean) this.expression.getValue());
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public AbstractPredicate<KeyValueObject<?>> clone() {
        return new KeyValueObjectPredicate(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<KeyValueObject<?>> and(IPredicate<KeyValueObject<?>> predicate) {
        if (predicate instanceof KeyValueObjectPredicate) {
            SDFExpression expr = ((KeyValueObjectPredicate) predicate).expression;
            AndOperator and = new AndOperator();
            and.setArguments(new IExpression<?>[] { this.expression.getMEPExpression(), expr.getMEPExpression() });
            IAttributeResolver resolver = new DirectAttributeResolver(expression.getAttributeResolver().getSchema(), expr.getAttributeResolver().getSchema());
            return new KeyValueObjectPredicate(new SDFExpression(and.toString(), resolver, this.expression.getExpressionParser()));
        }
        return super.and(predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<KeyValueObject<?>> or(IPredicate<KeyValueObject<?>> predicate) {
        if (predicate instanceof KeyValueObjectPredicate) {
            SDFExpression expr = ((KeyValueObjectPredicate) predicate).expression;
            OrOperator or = new OrOperator();
            or.setArguments(new IExpression<?>[] { this.expression.getMEPExpression(), expr.getMEPExpression() });
            IAttributeResolver resolver = new DirectAttributeResolver(expression.getAttributeResolver().getSchema(), expr.getAttributeResolver().getSchema());
            return new KeyValueObjectPredicate(new SDFExpression(or.toString(), resolver, this.expression.getExpressionParser()));
        }
        return super.or(predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<KeyValueObject<?>> not() {
        NotOperator not = new NotOperator();
        not.setArguments(new IExpression<?>[] { this.expression.getMEPExpression() });
        return new KeyValueObjectPredicate(new SDFExpression(not.toString(), expression.getAttributeResolver(), this.expression.getExpressionParser()));
    }

    @SuppressWarnings("boxing")
    public static void main(String[] args) {
        String predicate = "a*10-100 > b";
        SDFExpression expression = new SDFExpression(predicate, null, MEP.getInstance());
        KeyValueObjectPredicate pre = new KeyValueObjectPredicate(expression);
        KeyValueObject<IMetaAttribute> input = KeyValueObject.createInstance();
        input.setAttribute("a", 10);
        input.setAttribute("b", 8);
        System.out.println(pre.evaluate(input));
    }
}
