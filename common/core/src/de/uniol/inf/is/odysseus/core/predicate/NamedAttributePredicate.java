package de.uniol.inf.is.odysseus.core.predicate;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.INamedAttributeStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class NamedAttributePredicate<T extends INamedAttributeStreamObject<?>> extends AbstractPredicate<T> {

	private static final long serialVersionUID = 6578575151834596318L;

	final protected SDFExpression expression;
	final protected List<SDFAttribute> neededAttributes;
	final protected List<SDFSchema> inputSchema;

	public NamedAttributePredicate(SDFExpression expression, List<SDFSchema> inputSchema) {
		this.expression = expression;
		this.neededAttributes = expression.getAllAttributes();
		if (inputSchema == null){
			this.inputSchema = new ArrayList<>();
		}else{
			this.inputSchema = new ArrayList<>(inputSchema);
		}
	}

	public NamedAttributePredicate(NamedAttributePredicate<T> predicate) {
		this.expression = predicate.expression == null ? null : predicate.expression.clone();
		this.neededAttributes = expression.getAllAttributes();
		this.inputSchema = predicate.inputSchema;
	}

	@Override
	public Boolean evaluate(T input) {
		Object[] values = new Object[neededAttributes.size()];
		for (int i = 0; i < values.length; ++i) {
			values[i] = input.getAttribute(neededAttributes.get(i).getURI());
			if (values[i] == null) {
				if (inputSchema.get(0) != null) {
					Pair<Integer, Integer> pos = inputSchema.get(0).indexOfMetaAttribute(neededAttributes.get(i).getURI());
					if (pos != null) {
						values[i] = input.getMetadata().getValue(pos.getE1(), pos.getE2());
					}
				}
			}
		}
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public Boolean evaluate(T left, T right) {
		Object[] values = new Object[neededAttributes.size()];
		for (int i = 0; i < values.length; ++i) {
			values[i] = left.getAttribute(neededAttributes.get(i).getURI());
			if (values[i] == null) {
				values[i] = right.getAttribute(neededAttributes.get(i).getURI());
			}
		}

		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public AbstractPredicate<T> clone() {
		return new NamedAttributePredicate<T>(this);
	}


}
