/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.mep.IExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.Variable;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

/**
 * 
 * @author Benjamin G
 *
 */
public class StreamCarsExpression implements IStreamCarsExpression {

	public static final String DEFAULT_TARGET_VARIABLE_NAME = "DEFAULT_TARGET_VARIABLE";

	private IStreamCarsExpressionVariable target;
	private String expression;

	private IExpression<?> mepExpression;
	private List<IStreamCarsExpressionVariable> variables;

	/**
	 * 
	 * @param expression
	 */
	public StreamCarsExpression(String expression) {
		this(null, expression);
	}

	/**
	 * 
	 * @param targetName
	 * @param expression
	 */
	public StreamCarsExpression(String targetName, String expression) {
		this.expression = expression;

		if(targetName != null) {
			target = new TargetVariable(this, targetName);
		} else {
			target = new TargetVariable(this, DEFAULT_TARGET_VARIABLE_NAME);
		}

		try {
			mepExpression = MEP.parse(expression);
			Set<Variable> mepVars = mepExpression.getVariables();
			variables = new ArrayList<IStreamCarsExpressionVariable>(mepVars.size());

			for(Variable var : mepVars) {
				variables.add(new StreamCarsExpressionVariable(this, var));
			}
		} catch (de.uniol.inf.is.odysseus.mep.ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getExpression() {
		return expression;
	}

	@Override
	public List<IStreamCarsExpressionVariable> getVariables() {
		return variables;
	}

	@Override
	public Object getValue() {
		return target.getValue();
	}

	@Override
	public double getDoubleValue() {
		return (((Number) this.getValue())).doubleValue();
	}

	@Override
	public IStreamCarsExpressionVariable getTarget() {
		return target;
	}

	@Override
	public void evaluate() {
		target.bind(mepExpression.getValue());
	}

	@Override
	public void init(SDFSchema... schemata) {
		for(SDFSchema schema : schemata) {
			initAttributePaths(schema);
		}
	}

	protected void initAttributePaths(SDFSchema schema) {
		for(IStreamCarsExpressionVariable var : variables) {
			var.init(schema);
		}
		if(target.isSchemaVariable()) {
			target.init(schema);
		}
	}

	@Override
	public void replaceVaryingIndex(SDFSchema schema, int index) {
		for(IStreamCarsExpressionVariable var : variables) {
			if(var.isSchemaVariable(schema)) var.replaceVaryingIndex(index);
		}
		if(target.isSchemaVariable(schema)) {
			target.replaceVaryingIndex(index);
		}
	}

	@Override
	public void replaceVaryingIndex(SDFSchema schema, int index, boolean copy) {
		for(IStreamCarsExpressionVariable var : variables) {
			if(var.isSchemaVariable(schema)) var.replaceVaryingIndex(index, copy);
		}
		if(target.isSchemaVariable(schema)) {
			target.replaceVaryingIndex(index, copy);
		}
	}

	@Override
	public void bind(String variableName, Object value) {
		for(IStreamCarsExpressionVariable var : variables) {
			if(variableName.equals(var.getName())) {
				var.bind(value);
			}
		}
	}
	
	@Override
	public void bindTupleValues(SDFSchema schema, MVRelationalTuple<?> tuple) {
		for(IStreamCarsExpressionVariable var : variables) {
			if(var.isSchemaVariable(schema)) {
				var.bindTupleValue(tuple);
			}
		}
	}

	@Override
	public void bind(IStreamCarsExpressionVariable variable, Object value) {
		variable.bind(value);
	}

	@Override
	public String toString() {
		return "ScarsExpression ["
				+ "\n target: " + target
				+ "\n expression: " + expression
				+ "\n MEP: " + mepExpression
				+ "\n variables: " + (variables != null ? variables : null)
			    + "\n]";
	}

	private static class TargetVariable extends StreamCarsExpressionVariable {

		private Object value;

		protected TargetVariable(IStreamCarsExpression parent, String name) {
			super(parent, name);
		}

		@Override
		public void bind(Object value) {
			this.value = value;
		}

		@Override
		public Object getValue() {
			return this.value;
		}

		@Override
		public boolean isVirtual() {
			return true;
		}
	}

//	public static void main(String[] args) {
//		SDFSchema scan = new SDFSchema();
//
//		SDFAttribute list = new SDFAttribute("a.list");
//		list.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("List"));
//
//		SDFAttribute obj = new SDFAttribute("obj");
//		obj.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("Record"));
//
//		SDFAttribute pos = new SDFAttribute("pos");
//		pos.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("Record"));
//
//		SDFAttribute x = new SDFAttribute("x");
//		x.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("MV"));
//		SDFAttribute y = new SDFAttribute("y");
//		y.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("MV"));
//		SDFAttribute z = new SDFAttribute("z");
//		z.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("MV"));
//
//		SDFSchema time = new SDFSchema();
//
//		SDFAttribute currentTime = new SDFAttribute("b.currentTime");
//		currentTime.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("MV"));
//
//		scan.add(list);
//		list.addSubattribute(obj);
//		obj.addSubattribute(pos);
//		pos.addSubattribute(x);
//		pos.addSubattribute(y);
//		pos.addSubattribute(z);
//
//		time.add(currentTime);
//
//		String expression = "[1;2] + [2;4] * a.list:obj#covariance + a.list:obj:pos:y + a.list:obj:pos:z * 10 * b.currentTime + GAIN";
//		StreamCarsExpression e = new StreamCarsExpression(expression);
//		e.init(scan, time);
//		System.out.println("TEST: BEFORE INDEX REPLACE:");
//		System.out.println(e);
//		e.replaceVaryingIndex(scan, 5);
//		System.out.println("TEST: AFTER INDEX REPLACE:");
//		System.out.println(e);
//
//		e.bind("a.list:obj#covariance", 10);
//		e.bind("a.list:obj:pos:y", 10);
//		e.bind("a.list:obj:pos:z", 10);
//		e.bind("b.currentTime", 3);
//		e.bind("someVaryingValue", 1000);
//
//		e.evaluate();
//
//		System.out.println("TEST: AFTER PREDICTION:");
//		System.out.println("##########value: " + e.getValue());
//		System.out.println(e);
//	}

	@Override
	public void reset() {
		target.reset();
		
		for(IStreamCarsExpressionVariable var : variables) {
			var.reset();
		}
	}


}
