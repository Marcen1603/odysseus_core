package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.expression;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.mep.ParseException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.style.PersistentCondition;

public abstract class Condition<T>{

	private String expressionStr = null;
	private T defaultValue = null;
	private IMepExpression<?> exp = null;
	private Set<Entry<Integer, IMepVariable>> variables = null;
	public Condition(PersistentCondition condition) {
		this.defaultValue = getValue(condition.defaultValue);
		this.expressionStr = condition.expression;
		if (this.defaultValue == null){
			try {
				IMepExpression<?> exp = MEP.getInstance().parse(expressionStr, (List<SDFSchema>)null);
				if (exp.isConstant()){
					this.defaultValue = getValue(exp.getValue());
				}					
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Condition(T defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void init(SDFSchema schema) {
		if (expressionStr != null && expressionStr.length() != 0){
			try {
				exp = MEP.getInstance().parse(expressionStr, schema);
				Set<IMepVariable> vars = exp.getVariables();
				TreeMap<Integer, IMepVariable> variables = new TreeMap<Integer, IMepVariable>();
				for (IMepVariable variable : vars) {
					SDFAttribute attr = schema.findAttribute(variable.getIdentifier());
					int index = schema.indexOf(attr);
					variables.put(index, variable);
					if (variable.getIdentifier().equals("defaultValue")){
						variable.bind(defaultValue, -1);
					}
				}
				this.variables = variables.entrySet();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	public T eval(Tuple<?> tuple) {
		T eval = null;
		if (tuple != null && exp != null){
			for (Entry<Integer,IMepVariable> element : this.variables) {
				element.getValue().bind(tuple.getAttribute(element.getKey()), element.getKey());
			}
			eval = getValue(this.exp.getValue());
		}
		return (eval == null? defaultValue : eval);
	}
	
	public T getDefault(){
		return this.defaultValue;
	}
	
	public void setDefault(T value){
		this.defaultValue = value;
	}
	protected abstract T getValue(Object o);
	
	protected abstract Object getPersistentDefaultValue();
	
	public PersistentCondition getSerializable(){
		return new PersistentCondition(getPersistentDefaultValue(), expressionStr);
	}
}
