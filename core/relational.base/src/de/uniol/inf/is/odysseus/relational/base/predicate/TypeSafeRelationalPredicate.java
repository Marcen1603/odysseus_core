package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class TypeSafeRelationalPredicate extends RelationalPredicate{
	
	private static final long serialVersionUID = 3481710292889478059L;

	public TypeSafeRelationalPredicate(SDFExpression expression){
		super(expression);
	}
	
	public TypeSafeRelationalPredicate(TypeSafeRelationalPredicate original){
		super(original);
	}
	
	@Override
	public boolean evaluate(Tuple<?> input) {
		Object[] values = new Object[this.attributePositions.length];
		ArrayList<Variable> vars = this.expression.getVariables();
		for (int i = 0; i < values.length; ++i) {
			//SDFSchema curSchema = this.fromRightChannel[i] ? this.rightSchema : this.leftSchema;
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
					values[i]=Double.parseDouble((String)input.getAttribute(this.attributePositions[i]));
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
	public boolean evaluate(Tuple<?> left, Tuple<?> right) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			Tuple<?> r = fromRightChannel[i] ? right : left;
			values[i] = r.getAttribute(this.attributePositions[i]);
		}
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}
	
	@Override
    public TypeSafeRelationalPredicate clone(){
		return new TypeSafeRelationalPredicate(this);
	}
}
