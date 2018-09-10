package de.uniol.inf.is.odysseus.core.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepExpressionParser;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

abstract public class AbstractRelationalExpression<T extends IMetaAttribute> implements IRelationalExpression<T>, IPredicate<Tuple<T>> {

	private static final long serialVersionUID = -3373856682130638058L;
	protected SDFExpression expression;
	protected VarHelper[] variables;

	final private Map<SDFAttribute, SDFAttribute> replacementMap = new HashMap<>();


	protected AbstractRelationalExpression(SDFExpression expression){
		this.expression = expression.clone();
	}
	
	protected AbstractRelationalExpression(AbstractRelationalExpression<T> other){
		this.expression = other.expression.clone();
		if (other.variables != null) {
			this.variables = new VarHelper[other.variables.length];
			int i = 0;
			for (VarHelper v : other.variables) {
				this.variables[i++] = new VarHelper(v);
			}
		}
	}
	
	public SDFExpression getExpression() {
		return expression;
	}
	
	public IAttributeResolver getAttributeResolver() {
		return expression.getAttributeResolver();
	}

	public IMepExpressionParser getExpressionParser() {
		return expression.getExpressionParser();
	}

	public IMepExpression<?> getMEPExpression() {
		return expression.getMEPExpression();
	}
	
	@Override
	public List<SDFAttribute> getAttributes() {
		return expression.getAllAttributes();
	}
	
	public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
		if (!curAttr.equals(newAttr)) {
			replacementMap.put(curAttr, newAttr);
		}
	}

	
	protected SDFAttribute getReplacement(SDFAttribute a) {
		SDFAttribute ret = a;
		SDFAttribute tmp = null;
		while ((tmp = replacementMap.get(ret)) != null) {
			ret = getReplacement(tmp);
		}
		return ret;
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
		throw new RuntimeException("Cannot find attribute " + curAttribute + " in input stream! Schemata are "+schemata);
	}

	@Override
	public void initVars(List<SDFSchema> schema) {
		List<SDFAttribute> neededAttributes = expression.getAllAttributes();
		VarHelper[] tmp = new VarHelper[neededAttributes.size()];
		int j = 0;
		for (SDFAttribute curAttribute : neededAttributes) {
			tmp[j++] = initAttribute(schema, curAttribute);
		}
		// Call once to init all type calculations
		this.expression.getType();
		
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
	public boolean isAlwaysTrue() {
		return expression.isAlwaysTrue();
	}

	@Override
	public boolean isAlwaysFalse() {
		return expression.isAlwaysFalse();
	}
	
	@Override
	public Object evaluate(Tuple<T> object, List<ISession> sessions, List<Tuple<T>> history) {

		if (expression.isContant()) {
			return expression.getValue();
		}

		Object[] values = new Object[this.variables.length];

		for (int j = 0; j < this.variables.length; ++j) {
			processObject(object, history, values, j);
		}

		expression.setSessions(sessions);
		expression.bindVariables(values);

		return expression.getValue();
	}

	@Override
	public Boolean evaluate(Tuple<T> input) {
		return (Boolean) evaluate(input, null, null);
	}

	public SDFDatatype getType() {
		return expression.getType();
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

	@Override
	public Boolean evaluate(Tuple<T> left, Tuple<T> right) {
		return (Boolean) evaluate(left, right, null, null);
	}

	public Object evaluate(Tuple<T> left, Tuple<T> right, List<ISession> sessions, List<Tuple<T>> history) {

		if (expression.isContant()) {
			return expression.getValue();
		}

		Object[] values = new Object[this.variables.length];

		for (int j = 0; j < this.variables.length; ++j) {
			// Is input from left or from right schema
			Tuple<T> object = variables[j].getSchema() == -1 ? left : right;
			processObject(object, history, values, j);
		}

		expression.setSessions(sessions);
		expression.bindVariables(values);

		return expression.getValue();

	}

	public Object evaluate(List<Tuple<T>> input, List<ISession> sessions, List<Tuple<T>> history) {

		if (expression.isContant()) {
			return expression.getValue();
		}

		Object[] values = new Object[this.variables.length];

		for (int j = 0; j < this.variables.length; ++j) {
			// Is input from one of the input schemata
			Tuple<T> object = input.get((-1) * variables[j].getSchema());
			processObject(object, history, values, j);
		}
	
		expression.setSessions(sessions);
		expression.bindVariables(values);

		return expression.getValue();

	}

	protected Tuple<T> determineObjectForExpression(Tuple<T> object, List<Tuple<T>> history, int j) {
		return object;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isContainedIn(IPredicate<?> predicate) {
		if (predicate instanceof AbstractRelationalExpression) {
			if (this.expression instanceof IMepFunction) {
				return ((IMepFunction<T>) this.expression).isContainedIn(((AbstractRelationalExpression<T>) predicate).expression.getMEPExpression());
			}
		}
		return false;
	}

	public boolean isAndPredicate() {
		return expression.getMEPExpression().isFunction() && expression.getMEPExpression().toFunction().isAndPredicate();
	}



	public boolean isOrPredicate() {
		return expression.getMEPExpression().isFunction() && expression.getMEPExpression().toFunction().isOrPredicate();
	}


	
	public boolean isNotPredicate(){
		return expression.getMEPExpression().isFunction() && expression.getMEPExpression().toFunction().isNotPredicate();
	}
	
	@Override
	public String toString() {
		return expression.toString() + " initialized = "+(variables != null);
	}
	
	@Override
	abstract public AbstractRelationalExpression<T> clone();


	
	
}
