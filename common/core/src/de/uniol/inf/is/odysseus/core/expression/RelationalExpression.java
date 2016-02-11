package de.uniol.inf.is.odysseus.core.expression;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class RelationalExpression<T extends IMetaAttribute> extends SDFExpression {

	private static final long serialVersionUID = -5237770549231080761L;
	protected VarHelper[] variables;

	public RelationalExpression(SDFExpression expression) {
		super(expression);
	}

	protected VarHelper initAttribute(List<SDFSchema> schemata, SDFAttribute curAttribute) {
		// Maybe the attribute is not given totally, use another way to find
		// attribute
		for (int i = 0; i < schemata.size(); i++) {
			SDFSchema schema = schemata.get(i);
			int index = schema.indexOf(curAttribute);

			if (index == -1) {
				index = schema.findAttributeIndex(curAttribute.getAttributeName());
			}
			// Attribute is part of payload
			if (index >= 0) {
				return new VarHelper(index, (-1)*(i+1), 0);
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

	public void initVars(List<SDFSchema> schema) {
		List<SDFAttribute> neededAttributes = getAllAttributes();
		VarHelper[] newArray = new VarHelper[neededAttributes.size()];
		this.variables = newArray;
		int j = 0;
		for (SDFAttribute curAttribute : neededAttributes) {
			newArray[j++] = initAttribute(schema, curAttribute);
		}
		// Call once to init all type calculations
		this.getType();
	}
	
	public void initVars(SDFSchema schema){
		List<SDFSchema> schemata = new ArrayList<>();
		schemata.add(schema);
		initVars(schemata);
	}

	public Object evaluate(Tuple<T> object, List<ISession> sessions, List<Tuple<T>> history) {

		Object[] values = new Object[this.variables.length];

		for (int j = 0; j < this.variables.length; ++j) {
			processObject(object, history, values, j);
		}

		bindVariables(values);
		setSessions(sessions);
		
		return getValue();
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
	
	public Object evaluate(Tuple<T> left, Tuple<T> right, List<ISession> sessions, List<Tuple<T>> history) {

		Object[] values = new Object[this.variables.length];
		
		for (int j = 0; j < this.variables.length; ++j) {
			// Is input from left or from right schema
			Tuple<T> object = variables[j].getSchema()==-1?left:right;
			processObject(object, history, values, j);
		}

		bindVariables(values);
		setSessions(sessions);

		return getValue();

	}
	
	public Object evaluate(List<Tuple<T>> input, List<ISession> sessions, List<Tuple<T>> history) {

		Object[] values = new Object[this.variables.length];
		
		for (int j = 0; j < this.variables.length; ++j) {
			// Is input from one of the input schemata
			Tuple<T> object = input.get((-1)*variables[j].getSchema());
			processObject(object, history, values, j);
		}

		bindVariables(values);
		setSessions(sessions);

		return getValue();

	}

	protected Tuple<T> determineObjectForExpression(Tuple<T> object, List<Tuple<T>> history, int j) {
		return object;
	}

}
