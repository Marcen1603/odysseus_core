package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nfunk.jep.ASTConstant;
import org.nfunk.jep.ASTFunNode;
import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.Variable;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.CustomFunction;

/**
 * @author Jonas Jacobi
 */
public class SDFExpression implements Serializable {

	private static final long serialVersionUID = 8658794141096208317L;
	// Für P2P als transient gekennzeichnet
	private transient JEP myParser;
	// Für P2P als transient gekennzeichnet
	transient Map<String, Variable> variables;
	// Für P2P als transient gekennzeichnet
	transient ArrayList<Variable> variableArrayList = new ArrayList<Variable>();
	// Für P2P als transient gekennzeichnet
	transient Variable[] variableArray;

	// kann entfernt werden
	private int varCounter;
	// Für P2P als transient gekennzeichnet
	private transient Node myNode;

	public boolean isSetOperator(String symbol) {
		ASTFunNode fun = (ASTFunNode) myNode;
		return fun.getOperator().getSymbol().equals(symbol);
	}

	private String expression;

	private Object value;

	private List<SDFAttribute> attributes;

	/**
	 * Using prediction functions you know in advance of query processing, which
	 * attributes have to be used and where they are located in the schema. So
	 * the attribute positions can be initialized in advance.
	 */
	private int[] attributePositions;

	private SDFAttribute attribute;
	

	private IAttributeResolver attributeResolver;

	private static final List<CustomFunction> customFunctions = new ArrayList<CustomFunction>();

	private static final String variableRegexp = "(\\p{Alpha}[\\p{Alnum}\\_:\\.]*)([^\\(\\p{Alnum}_:\\.\"]|$)";

	private static final String aggregateRegexp = "\\b((SUM|COUNT|AVG|MIN|MAX)\\([^\\)]*\\))";

	private static final Pattern variablePattern = Pattern
			.compile(variableRegexp);

	private static final Pattern aggregatePattern = Pattern
			.compile(aggregateRegexp);

	private static final Set<String> functions = new HashSet<String>();
	private static boolean isFunctionsInit = false;

	// TODO alles schon im parser aufloesen und variable/attribut bindings
	// erstellen
	public SDFExpression(SDFAttribute attribute) {
		this.variableArray = new Variable[0];
		init(attribute);
	}
	
	public synchronized static void addFunction(CustomFunction function) {
		customFunctions.add(function);
		functions.add(function.getName());
	}

	public synchronized static Set<String> getFunctions() {
		if (!isFunctionsInit) {
			JEP tmpParser = new JEP();
			tmpParser.addStandardFunctions();
			for (Object o : tmpParser.getFunctionTable().keySet()) {
				functions.add((String) o);
			}
			isFunctionsInit = true;
		}

		return Collections.unmodifiableSet(functions);
	}

	private void init(SDFAttribute attribute) {
		this.attribute = attribute;
		this.attributes = new ArrayList<SDFAttribute>(1);
		this.attributes.add(attribute);
		this.expression = attribute.toString();
	}

	private void init(String value, IAttributeResolver attributeResolver)
			throws SDFExpressionParseException {
		this.expression = value.trim();
		this.varCounter = 0;
		this.variables = new HashMap<String, Variable>();
		this.attributes = new ArrayList<SDFAttribute>();
		this.attribute = null;
		this.attributeResolver = attributeResolver;

		myParser = new JEP();
		myParser.addStandardConstants();
		myParser.addStandardFunctions();
		for (CustomFunction curFunction : customFunctions) {
			myParser.addFunction(curFunction.getName(), curFunction);
		}
		myParser.setAllowUndeclared(false);

		String result = "";
		Matcher m2 = aggregatePattern.matcher(value);

		int start = 0;
		while (m2.find()) {
			String group = m2.group(1);
			if (group == null) {
				continue;
			}
			// TODO regexp zum finden der richtigen ersetzungsdoedeleien
			Variable variable = toVariable(group);
			result += value.substring(start, m2.start(1));
			result += variable.getName();
			start = m2.end(1);
		}
		result += value.substring(start);

		Matcher m = variablePattern.matcher(value);
		String totalResult = "";
		while (m.find()) {
			String group = m.group(1);
			if (group == null) {
				continue;
			}
			Variable variable = toVariable(group);
			totalResult += result.substring(start, m.start(1));
			totalResult += variable.getName();
			start = m.end(1);
		}
		totalResult += result.substring(start);
		try {
			myNode = myParser.parse(totalResult);
			variableArray = this.variableArrayList
					.toArray(new Variable[this.variableArrayList.size()]);
			if (myNode instanceof ASTConstant) {
				setValue(myParser.evaluate(myNode));
			}
		} catch (ParseException e) {
			System.out.println("Expr: " + value);
			throw new SDFExpressionParseException(e);
		}
	}

	public void initAttributePositions(SDFAttributeList schema) {
		this.attributePositions = new int[this.attributes.size()];

		int j = 0;
		for (SDFAttribute curAttribute : this.attributes) {
			this.attributePositions[j++] = schema.indexOf(curAttribute);
		}
	}

	public int[] getAttributePositions() {
		return this.attributePositions;
	}

	/**
	 * @param URI
	 * @param value
	 * @param attributeResolver
	 * @throws ParseException
	 */
	public SDFExpression(String URI, String value,
			IAttributeResolver attributeResolver)
			throws SDFExpressionParseException {
		init(value, attributeResolver);
	}

	public SDFExpression(SDFExpression expression)
			throws SDFExpressionParseException {
		if (expression.attribute == null) {
			init(expression.expression, expression.attributeResolver);
		} else {
			init(expression.attribute);
		}
		this.attributePositions = expression.attributePositions;
	}

	private Variable toVariable(String token) {
		for (Variable v : this.variableArrayList) {
			if (v.getName() == token) {
				return v;
			}
		}
		SDFAttribute attribute = this.attributeResolver.getAttribute(token);
		String aliasName = null;

		/**
		 * We have to allow the variable "t" for "time" in expressions, although
		 * time is not an attribute of a relational schema. This is because,
		 * prediction function make use of the time attribute.
		 * 
		 * So, what we have to do is to check if the token equals "t" if no
		 * attribute has been found for the token name. TODO an ABo, kann man
		 * diese spezialbehandlung nicht irgendwie rausziehen?
		 */
		if (attribute == null && !token.equals("t")) {
			System.err.println("no such attribute: " + token);
			throw new SDFExpressionParseException("No such attribute: " + token);
		} else if (token.equals("t")) {
			aliasName = "t";
			attribute = new CQLAttribute(null, "t");
		} else {
			aliasName = attribute.getURI(false);
		}

		Variable var = this.variables.get(aliasName);
		if (var == null) {
			String varName = "__V" + ++this.varCounter;
			myParser.addVariable(varName, null);
			var = myParser.getVar(varName);
			this.variableArrayList.add(var);
			this.attributes.add(attribute);
			this.variables.put(aliasName, var);
		}

		return var;
	}

	private void setValue(Object object) {
		this.value = object;
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) this.value;
	}

	public double getDouble() {
		return (((Number) this.value)).doubleValue();
	}

	public boolean isString() {
		return this.value instanceof String;
	}

	public boolean isNumber() {
		return this.value instanceof Number;
	}

	@Override
	public SDFExpression clone() {
		return new SDFExpression(this);
	}

	public List<SDFAttribute> getAllAttributes() {
		return this.attributes;
	}

	@Override
	public String toString() {
		return this.expression;
	}
	
	public String getExpression() {
		return expression;
	}

	public void bindVariables(Object... values) {
		if (myNode instanceof ASTConstant) {
			return;
		}

		if (attribute != null) {
			setValue(values[0]);
			return;
		}

		if (values.length != variableArray.length) {
			throw new IllegalArgumentException(
					"illegal variable bindings in expression");
		}

		for (int i = 0; i < values.length; ++i) {
			variableArray[i].setValue(values[i]);
		}

		try {
			setValue(myParser.evaluate(myNode));
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
