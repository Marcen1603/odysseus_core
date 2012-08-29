package de.uniol.inf.is.odysseus.core.server.predicate;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

/**
 * This predicate can be evaluated with a KeyValueObject. The attribute names in the expression
 * must correspond to the keys in the KeyValueObject
 * 
 * @author Marco Grawunder
 *
 */

public class KeyValueObjectPredicate extends AbstractPredicate<KeyValueObject<?>>{

	private static final long serialVersionUID = 2982916759647665469L;

	final SDFExpression expression;
	final List<SDFAttribute> neededAttributes;
	
	public KeyValueObjectPredicate(SDFExpression expression){
		this.expression = expression;
		neededAttributes = expression.getAllAttributes();
	}
	
	public KeyValueObjectPredicate(KeyValueObjectPredicate other){
		this.expression = other.expression == null ? null : other.expression.clone();
		neededAttributes = expression.getAllAttributes();
	}
	
	@Override
	public boolean evaluate(KeyValueObject<?> input) {
		Object[] values = new Object[neededAttributes.size()];
		for (int i = 0; i < values.length; ++i) {
			values[i] = input.getAttribute(neededAttributes.get(i).getURI());
		}
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public boolean evaluate(KeyValueObject<?> left, KeyValueObject<?> right) {
		Object[] values = new Object[neededAttributes.size()];
		for (int i = 0; i < values.length; ++i) {
			values[i] = left.getAttribute(neededAttributes.get(i).getURI());
			if (values[i]==null){
				values[i] = right.getAttribute(neededAttributes.get(i).getURI());
			}
		}
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public AbstractPredicate<KeyValueObject<?>> clone() {
		return new KeyValueObjectPredicate(this);
	}

	public static void main(String[] args) {
		String predicate = "a > b";
		SDFExpression expression = new SDFExpression(null,predicate, MEP.getInstance());
		KeyValueObjectPredicate pre = new KeyValueObjectPredicate(expression);
		KeyValueObject<IMetaAttribute> input = new KeyValueObject<IMetaAttribute>();
		input.setAttribute("a", 10);
		input.setAttribute("b", 8);
		System.out.println(pre.evaluate(input));
	}
	
}
