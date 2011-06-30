package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.mep.Variable;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class TypeSafeRelationalPredicate extends RelationalPredicate{

	public TypeSafeRelationalPredicate(SDFExpression expression){
		super(expression);
	}
	
	public TypeSafeRelationalPredicate(TypeSafeRelationalPredicate original){
		super(original);
	}
	
	@Override
	public boolean evaluate(RelationalTuple<?> input) {
		Object[] values = new Object[this.attributePositions.length];
		ArrayList<Variable> vars = this.expression.getVariables();
		for (int i = 0; i < values.length; ++i) {
			SDFAttributeList curSchema = this.fromRightChannel[i] ? this.rightSchema : this.leftSchema;
			// a variable can have only one accepted type, since
			// the user should have be explicit in defining the
			// expression, e. g. by using toLong(?x)
			if(vars.get(i).getAcceptedTypes().length == 2 && 
					this.expression.getAllAttributes().get(i).getDatatype().getURI().equalsIgnoreCase("String")){
				throw new IllegalArgumentException("Expression variable " + vars.get(i).getIdentifier() + " has ambiguous data types: " + vars.get(i).getAcceptedTypes());
			}
			if(this.expression.getAllAttributes().get(i).getDatatype().getURI().equalsIgnoreCase("String") &&
					vars.get(i).getAcceptedTypes().length == 1 ){ 
				if(vars.get(i).getAcceptedTypes()[0].isNumeric()){
					values[i]=(Double)Double.parseDouble((String)input.getAttribute(this.attributePositions[i]));
				}
				else{
					values[i] = input.getAttribute(this.attributePositions[i]);
				}
			}
			else{
				values[i] = input.getAttribute(this.attributePositions[i]);
			}
		}
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public boolean evaluate(RelationalTuple<?> left, RelationalTuple<?> right) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			RelationalTuple<?> r = fromRightChannel[i] ? right : left;
			values[i] = r.getAttribute(this.attributePositions[i]);
		}
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}
	
	public TypeSafeRelationalPredicate clone(){
		return new TypeSafeRelationalPredicate(this);
	}
}
