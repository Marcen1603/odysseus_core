package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.mep.IExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.Variable;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

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
	public void init(SDFAttributeList... schemata) {
		for(SDFAttributeList schema : schemata) {
			initAttributePaths(schema);
		}
	}

	protected void initAttributePaths(SDFAttributeList schema) {
		for(IStreamCarsExpressionVariable var : variables) {
			var.init(schema);
		}
		if(target.isSchemaVariable()) {
			target.init(schema);
		}
	}

	@Override
	public void replaceVaryingIndex(SDFAttributeList schema, int index) {
		for(IStreamCarsExpressionVariable var : variables) {
			if(var.isSchemaVariable(schema)) var.replaceVaryingIndex(index);
		}
		if(target.isSchemaVariable(schema)) {
			target.replaceVaryingIndex(index);
		}
	}

	@Override
	public void replaceVaryingIndex(SDFAttributeList schema, int index, boolean copy) {
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
	public void bindTupleValues(SDFAttributeList schema, MVRelationalTuple<?> tuple) {
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

		public boolean isVirtual() {
			return true;
		}
	}

	public static void main(String[] args) {
		SDFAttributeList scan = new SDFAttributeList();

		SDFAttribute list = new SDFAttribute("a.list");
		list.setDatatype(SDFDatatypeFactory.getDatatype("List"));

		SDFAttribute obj = new SDFAttribute("obj");
		obj.setDatatype(SDFDatatypeFactory.getDatatype("Record"));

		SDFAttribute pos = new SDFAttribute("pos");
		pos.setDatatype(SDFDatatypeFactory.getDatatype("Record"));

		SDFAttribute x = new SDFAttribute("x");
		x.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		SDFAttribute y = new SDFAttribute("y");
		y.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		SDFAttribute z = new SDFAttribute("z");
		z.setDatatype(SDFDatatypeFactory.getDatatype("MV"));

		SDFAttributeList time = new SDFAttributeList();

		SDFAttribute currentTime = new SDFAttribute("b.currentTime");
		currentTime.setDatatype(SDFDatatypeFactory.getDatatype("MV"));

		scan.add(list);
		list.addSubattribute(obj);
		obj.addSubattribute(pos);
		pos.addSubattribute(x);
		pos.addSubattribute(y);
		pos.addSubattribute(z);

		time.add(currentTime);

		String expression = "[1;2] + [2;4] * a.list:obj#covariance + a.list:obj:pos:y + a.list:obj:pos:z * 10 * b.currentTime + GAIN";
		StreamCarsExpression e = new StreamCarsExpression(expression);
		e.init(scan, time);
		System.out.println("TEST: BEFORE INDEX REPLACE:");
		System.out.println(e);
		e.replaceVaryingIndex(scan, 5);
		System.out.println("TEST: AFTER INDEX REPLACE:");
		System.out.println(e);

		e.bind("a.list:obj#covariance", 10);
		e.bind("a.list:obj:pos:y", 10);
		e.bind("a.list:obj:pos:z", 10);
		e.bind("b.currentTime", 3);
		e.bind("someVaryingValue", 1000);

		e.evaluate();

		System.out.println("TEST: AFTER PREDICTION:");
		System.out.println("##########value: " + e.getValue());
		System.out.println(e);
	}


}
