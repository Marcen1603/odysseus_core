package de.uniol.inf.is.odysseus.keyvalue.predicate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class KeyValuePredicate extends AbstractPredicate<KeyValueObject<?>> {

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
	
	public KeyValuePredicate(KeyValuePredicate predicate) {
		this.expression = predicate.expression == null ? null : predicate.expression.clone();
		this.replacementMap = new HashMap<SDFAttribute, SDFAttribute>(predicate.replacementMap);
		this.neededAttributes = expression.getAllAttributes();
	}
	
	@Override
	public boolean evaluate(KeyValueObject<?> input) {
		Object[] values = new Object[neededAttributes.size()];
		for (int i = 0; i < values.length; ++i) {
			values[i] = input.getAttribute(neededAttributes.get(i).getURI());
		}
//		this.expression.bindMetaAttribute(input.getMetadata());
		this.expression.bindAdditionalContent(input.getAdditionalContent());
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
		Map<String, Serializable> additionalContent = new HashMap<String, Serializable>();
        additionalContent.putAll(left.getAdditionalContent());
        additionalContent.putAll(right.getAdditionalContent());
        
        this.expression.bindAdditionalContent(additionalContent);
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public AbstractPredicate<KeyValueObject<?>> clone() {
		return new KeyValuePredicate(this);
	}

}
