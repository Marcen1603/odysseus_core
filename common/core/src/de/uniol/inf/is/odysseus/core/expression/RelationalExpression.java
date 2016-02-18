package de.uniol.inf.is.odysseus.core.expression;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class RelationalExpression<T extends IMetaAttribute> extends SDFExpression implements IRelationalExpression<T>{

	private static final long serialVersionUID = -5237770549231080761L;
	protected VarHelper[] variables;

	public RelationalExpression(SDFExpression expression) {
		super(expression);
	}

	public RelationalExpression(RelationalExpression<T> other) {
		super(other);
		if (other.variables != null) {
			this.variables = new VarHelper[other.variables.length];
			int i = 0;
			for (VarHelper v : other.variables) {
				this.variables[i++] = new VarHelper(v);
			}
		}
	}

	protected VarHelper initAttribute(List<SDFSchema> schemata, SDFAttribute attribute) {
		// Maybe the attribute is not given totally, use another way to find
		// attribute
		SDFAttribute curAttribute = getReplacement(attribute);
		for (int i = 0; i < schemata.size(); i++) {
			SDFSchema schema = schemata.get(i);
			int index = schema.indexOf(curAttribute);

			if (index == -1) {
				index = schema.findAttributeIndex(curAttribute.getURI());
			}
			// Attribute is part of payload
			if (index >= 0) {
				return new VarHelper(index, (-1) * (i + 1), 0);
			} else { // Attribute is (potentially) part of meta data;
				Pair<Integer, Integer> pos = schema.indexOfMetaAttribute(curAttribute);
				if (pos != null) {
					return new VarHelper(pos.getE2(), pos.getE1(), 0);
				}
			}
			if (curAttribute.getAttributeName().equalsIgnoreCase(SDFAttribute.THIS)) {
				VarHelper vh = new VarHelper(-1, -1);
				vh.setThis(true);
				return vh;
			}
		}
		throw new RuntimeException("Cannot find attribute " + curAttribute + " in input stream!");
	}

	@Override
	public void initVars(List<SDFSchema> schema) {
		List<SDFAttribute> neededAttributes = getAllAttributes();
		VarHelper[] tmp = new VarHelper[neededAttributes.size()];
		int j = 0;
		for (SDFAttribute curAttribute : neededAttributes) {
			tmp[j++] = initAttribute(schema, curAttribute);
		}
		// Call once to init all type calculations
		this.getType();
		
		// if everything goes right (no wrong schema should destroy the right values)
		// could happen e.g. in rewrite phase
		this.variables = tmp; 
	}

	@Override
	public void initVars(SDFSchema schema) {
		if (schema == null){
			throw new IllegalArgumentException("Schema is not allowed to be null");
		}
		List<SDFSchema> schemata = new ArrayList<>();
		schemata.add(schema);
		initVars(schemata);
	}

	@Override
	public void initVars(SDFSchema left, SDFSchema right) {
		List<SDFSchema> schemata = new ArrayList<>();
		if (left == null){
			throw new IllegalArgumentException("Schema is not allowed to be null");
		}
		schemata.add(left);
		if (right != null) {
			schemata.add(right);
		}
		initVars(schemata);
	}

	@Override
	public Object evaluate(Tuple<T> object, List<ISession> sessions, List<Tuple<T>> history) {

		if (isContant()) {
			return getValue();
		}

		Object[] values = new Object[this.variables.length];

		for (int j = 0; j < this.variables.length; ++j) {
			processObject(object, history, values, j);
		}

		bindVariables(values);
		setSessions(sessions);

		return getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean evaluate(Object input) {
		return (Boolean) evaluate((Tuple<T>) input, null, null);
	}

	private void processObject(Tuple<T> object, List<Tuple<T>> history, Object[] values, int j) {
		Tuple<T> obj = determineObjectForExpression(object, history, j);
		if (obj != null) {
			if (this.variables[j].isThis()) {
				values[j] = obj;
			} else if (this.variables[j].getSchema() < 0) {
				values[j] = obj.getAttribute(this.variables[j].getPos());
			} else {
				values[j] = obj.getMetadata().getValue(variables[j].getSchema(), variables[j].getPos());
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean evaluate(Object left, Object right) {
		return (Boolean) evaluate((Tuple<T>) left, (Tuple<T>) right, null, null);
	}

	public Object evaluate(Tuple<T> left, Tuple<T> right, List<ISession> sessions, List<Tuple<T>> history) {

		if (isContant()) {
			return getValue();
		}

		Object[] values = new Object[this.variables.length];

		for (int j = 0; j < this.variables.length; ++j) {
			// Is input from left or from right schema
			Tuple<T> object = variables[j].getSchema() == -1 ? left : right;
			processObject(object, history, values, j);
		}

		bindVariables(values);
		setSessions(sessions);

		return getValue();

	}

	public Object evaluate(List<Tuple<T>> input, List<ISession> sessions, List<Tuple<T>> history) {

		if (isContant()) {
			return getValue();
		}

		Object[] values = new Object[this.variables.length];

		for (int j = 0; j < this.variables.length; ++j) {
			// Is input from one of the input schemata
			Tuple<T> object = input.get((-1) * variables[j].getSchema());
			processObject(object, history, values, j);
		}

		bindVariables(values);
		setSessions(sessions);

		return getValue();

	}

	protected Tuple<T> determineObjectForExpression(Tuple<T> object, List<Tuple<T>> history, int j) {
		return object;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IPredicate<?> and(IPredicate predicate) {
		SDFExpression newExpr = (SDFExpression) super.and(predicate);
		return new RelationalExpression<>(newExpr);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IPredicate<?> or(IPredicate predicate) {
		SDFExpression newExpr = (SDFExpression) super.or(predicate);
		return new RelationalExpression<>(newExpr);
	}

	@Override
	public IPredicate<?> not() {
		SDFExpression newExpr = (SDFExpression) super.not();
		return new RelationalExpression<>(newExpr);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RelationalExpression<T>> conjunctiveSplit() {
		List<SDFExpression> sdfepxr = super.conjunctiveSplit();
		List<RelationalExpression<T>> result = new ArrayList<>(sdfepxr.size());
		for (SDFExpression s : sdfepxr) {
			result.add(new RelationalExpression<>(s));
		}
		return result;
	}
	
	@Override
	public String toString() {
		return super.toString()+ " initialized = "+(variables != null);
	}

	@Override
	public RelationalExpression<T> clone() {
		return new RelationalExpression<>(this);
	}

}
