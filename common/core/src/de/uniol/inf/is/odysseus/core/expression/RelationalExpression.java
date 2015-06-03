package de.uniol.inf.is.odysseus.core.expression;

import java.util.LinkedList;
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

	protected VarHelper initAttribute(SDFSchema schema, SDFAttribute curAttribute) {
		int index = schema.indexOf(curAttribute);
		// Maybe the attribute is not given totally, use another way to find attribute
		if (index == -1){
			index = schema.findAttributeIndex(curAttribute.getAttributeName());
		}
		// Attribute is part of payload
		if (index >= 0) {
			return new VarHelper(index, 0);
		} else { // Attribute is (potentially) part of meta data;
			Pair<Integer, Integer> pos = schema.indexOfMetaAttribute(curAttribute);
			if (pos != null){
				return new VarHelper(pos.getE2(), pos.getE1(), 0);
			}
		}
		throw new RuntimeException("Cannot find attribute "+curAttribute+" in input stream!");
	}
	
	public void initVars(SDFSchema schema) {
		List<SDFAttribute> neededAttributes = getAllAttributes();
		VarHelper[] newArray = new VarHelper[neededAttributes.size()];
		this.variables = newArray;
		int j = 0;
		for (SDFAttribute curAttribute : neededAttributes) {
			newArray[j++] = initAttribute(schema, curAttribute);
		}
	}
	
	public Object evaluate(Tuple<T> object, List<ISession> sessions, LinkedList<Tuple<T>> history ){

		Object[] values = new Object[this.variables.length];
		IMetaAttribute[] meta = new IMetaAttribute[this.variables.length];
		for (int j = 0; j < this.variables.length; ++j) {
			Tuple<T> obj = determineObjectForExpression(object,
					history, j);
			if (obj != null) {
				if (this.variables[j].getSchema() == -1){
					values[j] = obj.getAttribute(this.variables[j].getPos());
				}else{
					values[j] = obj.getMetadata().getValue(variables[j].getSchema(), variables[j].getPos());
				}
				meta[j] = obj.getMetadata();
			}
		}
		
		bindMetaAttribute(object.getMetadata());
		bindAdditionalContent(object
				.getAdditionalContent());
		bindVariables(meta, values);
		setSessions(sessions);
		
		return getValue();

	}
	
	protected Tuple<T> determineObjectForExpression(Tuple<T> object,
			LinkedList<Tuple<T>> history, int j) {
		return object;
	}
	
}
