package de.uniol.inf.is.odysseus.core.predicate;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.INamedAttributeStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class NamedAttributePredicate<T extends INamedAttributeStreamObject<?>> extends AbstractPredicate<T> {

	private static final long serialVersionUID = 6578575151834596318L;

	final protected SDFExpression expression;
	final protected List<String> neededAttributes;
	final protected List<SDFSchema> inputSchema;

	public NamedAttributePredicate(SDFExpression expression, List<SDFSchema> inputSchema) {
		this.expression = expression;
		this.neededAttributes = new ArrayList<String>();
		// remove potential leading "."
		for (SDFAttribute attribute:expression.getAllAttributes()) {
			if (attribute.getAttributeName().startsWith(".")) {
				neededAttributes.add(attribute.getAttributeName().substring(1));
			}else{
				neededAttributes.add(attribute.getAttributeName());
			}
		}
		
		if (inputSchema == null){
			this.inputSchema = new ArrayList<>();
		}else{
			this.inputSchema = new ArrayList<>(inputSchema);
		}
	}

	public NamedAttributePredicate(NamedAttributePredicate<T> predicate) {
		this.expression = predicate.expression == null ? null : predicate.expression.clone();
		this.neededAttributes = new ArrayList<String>();
		// remove potential leading "."
		for (SDFAttribute attribute:expression.getAllAttributes()) {
			if (attribute.getAttributeName().startsWith(".")) {
				neededAttributes.add(attribute.getAttributeName().substring(1));
			}else{
				neededAttributes.add(attribute.getAttributeName());
			}
		}		this.inputSchema = predicate.inputSchema;
	}

	@Override
	public Boolean evaluate(T input) {
		Object[] values = determineInputValues(input);
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	protected Object evaluate_internal(T input, List<ISession> sessions, List<T> history) {
		Object[] values = determineInputValues(input);
		this.expression.setSessions(sessions);
		this.expression.bindVariables(values);
		return this.expression.getValue();
	}

	private Object[] determineInputValues(T input) {
		Object[] values = new Object[neededAttributes.size()];
		for (int i = 0; i < values.length; ++i) {
			values[i] = determineValue(input, neededAttributes.get(i), inputSchema.get(0));			
		}
		return values;
	}

	@Override
	public Boolean evaluate(T left, T right) {
		Object[] values = new Object[neededAttributes.size()];
		for (int i = 0; i < values.length; ++i) {
			values[i] = determineValue(left, neededAttributes.get(i), inputSchema.get(0));			
			if (values[i] == null) {
				values[i] = determineValue(right, neededAttributes.get(i), inputSchema.get(1));			
			}
		}

		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}
	
	private Object determineValue(T input, String neededAttribute, SDFSchema sdfSchema) {
		Object value;
		value = input.getAttribute(neededAttribute);
		if (value == null) {
			if (sdfSchema != null) {
				Pair<Integer, Integer> pos = sdfSchema.indexOfMetaAttribute(neededAttribute);
				if (pos != null) {
					value = input.getMetadata().getValue(pos.getE1(), pos.getE2());
				}
			}
		}
		// Could be path expression
		if (value == null) {
			value = input.path(neededAttribute);
		}
		return value;
	}

	@Override
	public AbstractPredicate<T> clone() {
		return new NamedAttributePredicate<T>(this);
	}


}
