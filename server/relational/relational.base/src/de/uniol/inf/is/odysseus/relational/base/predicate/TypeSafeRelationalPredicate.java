/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

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
					values[i]=Double.parseDouble((String)input.getAttribute(this.attributePositions[i].getE2()));
				}
				else{
					values[i] = input.getAttribute(this.attributePositions[i].getE2());
				}
			}
			else{
				values[i] = input.getAttribute(this.attributePositions[i].getE2());
			}
		}
//		this.expression.bindAdditionalContent(input.getAdditionalContent());
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public boolean evaluate(Tuple<?> left, Tuple<?> right) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			Tuple<?> r = fromRightChannel[i] ? right : left;
			values[i] = getValue(r,i);
		}
		Map<String, Serializable> additionalContent = new HashMap<String, Serializable>();
        additionalContent.putAll(left.getAdditionalContent());
        additionalContent.putAll(right.getAdditionalContent());
        
//        this.expression.bindAdditionalContent(additionalContent);
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}
	
	@Override
    public TypeSafeRelationalPredicate clone(){
		return new TypeSafeRelationalPredicate(this);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<Tuple<?>> and(IPredicate<Tuple<?>> predicate) {
        if (predicate instanceof TypeSafeRelationalPredicate) {
            SDFExpression expr = ((TypeSafeRelationalPredicate) predicate).expression;
            AndOperator and = new AndOperator();
            and.setArguments(new IExpression<?>[] { expression.getMEPExpression(), expr.getMEPExpression() });
            return new TypeSafeRelationalPredicate(new SDFExpression(and, expression.getAttributeResolver(), expression.getExpressionParser()));
        }
        return super.and(predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<Tuple<?>> or(IPredicate<Tuple<?>> predicate) {
        if (predicate instanceof TypeSafeRelationalPredicate) {
            SDFExpression expr = ((TypeSafeRelationalPredicate) predicate).expression;
            OrOperator or = new OrOperator();
            or.setArguments(new IExpression<?>[] { expression.getMEPExpression(), expr.getMEPExpression() });
            return new TypeSafeRelationalPredicate(new SDFExpression(or, expression.getAttributeResolver(), expression.getExpressionParser()));
        }
        return super.or(predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<Tuple<?>> not() {
        NotOperator not = new NotOperator();
        not.setArguments(new IExpression<?>[] { expression.getMEPExpression() });
        return new TypeSafeRelationalPredicate(new SDFExpression(not, expression.getAttributeResolver(), expression.getExpressionParser()));
    }
}
