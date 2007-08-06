package mg.dynaquest.sourcedescription.sdf.schema;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.Variable;

public class SDFExpression extends SDFNumberConstant {

	private static final long serialVersionUID = 8658794141096208317L;

	private JEP myParser;

	Map<String, Variable> variables;

	private int varCounter;

	private Node myNode;

	/**
	 * @param URI
	 * @param value
	 * @throws ParseException
	 */
	public SDFExpression(String URI, String value) {
		super(URI, 0);
		this.varCounter = 0;
		this.variables = new HashMap<String, Variable>();
		StringBuffer buffer = new StringBuffer(value);

		String completeStr = new String();
		String token = new String();
		boolean isURI = false;
		for (int i = 0; i < buffer.length(); ++i) {
			char c = buffer.charAt(i);
			if (c == '/' && !isURI && token.endsWith(":")) {
				isURI = true;
				token += c;
				continue;
			}

			if (c == ' ' || c == '\t') {
				if (isURI) {
					completeStr += toVariable(token).getName() + "_";
					isURI = false;
				} else {
					completeStr += token + "_";
				}
			} else {
				token += c;
			}
		}
		if (isURI) {
			completeStr += toVariable(token).getName() + "_";
			isURI = false;
		} else {
			completeStr += token;
		}

		myParser = new JEP();
		myParser.addStandardConstants();
		myParser.addStandardFunctions();
		try {
			myNode = myParser.parse(completeStr);

			if (variables.isEmpty()) {
				setValue(myParser.evaluate(myNode));
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private Variable toVariable(String token) {
		Variable var = this.variables.get(token);
		if (var == null) {
			myParser.addVariable("A" + ++varCounter, null);
			var = myParser.getVar("A" + varCounter);
		}

		return var;
	}

	@Override
	public boolean isDouble() {
		return true;
	}

	@Override
	public boolean isString() {
		return false;
	}

	public void bindVariables(Map<SDFAttribute, SDFConstant> attributeAssignment) {
		if (variables.isEmpty()) {
			return;
		}

		// TODO exception werden wenn nicht alle vars gebunden werden
		for (Variable var : variables.values()) {
			for (Map.Entry<SDFAttribute, SDFConstant> curEntry : attributeAssignment
					.entrySet()) {
				if (curEntry.getKey().getURI(false).equals(var.getName())) {
					var.setValue(curEntry.getValue().getDouble());
					break;
				}
			}
		}

		try {
			setValue(myParser.evaluate(myNode));
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public Set<String> attributes() {
		return this.variables.keySet();
	}

}
