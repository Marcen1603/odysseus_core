package de.uniol.inf.is.odysseus.cep.metamodel.jep;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.nfunk.jep.JEP;

import de.uniol.inf.is.odysseus.cep.metamodel.AbstractOutputSchemeEntry;
import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.metamodel.exception.UndefinedExpressionLabelException;

abstract public class JEPOutputSchemeEntry extends AbstractOutputSchemeEntry {
	/**
	 * JEP Expression, der den Wert der Ausgabe kodiert.
	 */
	private JEP expression;
	private Map<CepVariable, String> symbolTable = new HashMap<CepVariable, String>();
	
	public JEPOutputSchemeEntry(String expression) {
		setLabel(expression);
	}

	/**
	 * Setzt die textuelle Darstellung des Ausgabeschema-Eintrags und erzeugt
	 * daraus automatisch einen Ausdrucksbaum.
	 * 
	 * @param label De textuelle darstellung des Ausgabeschema-Eintrags.
	 * @throws UndefinedExpressionLabelException Falls das Label null oder leer ist.
	 */
	@SuppressWarnings("unchecked")
	public void setLabel(String label) throws UndefinedExpressionLabelException {
		super.setLabel(label);
		this.expression = new JEP();
		this.expression.setAllowUndeclared(true);
		String str = transformToJepVar(getLabel());
		this.expression.parseExpression(str);
		Set<String> v = (Set<String>) this.expression.getSymbolTable().keySet();
		for (String s : v) {
			this.symbolTable.put(transformToOutVar(s), s);
		}
	}

	private String transformToJepVar(String in) {
		String str = in.replace(CepVariable.getSeperator()+"-1"+CepVariable.getSeperator(), "$0");
		str = str.replace(CepVariable.getSeperator(), "$1");
		str = str.replace("[", "$2");
		return str.replace("]", "$3");
	}

	private CepVariable transformToOutVar(String out) {
		String str = out.replace("$0",CepVariable.getSeperator()+"-1"+CepVariable.getSeperator());
		str = str.replace("$1", CepVariable.getSeperator());
		str = str.replace("$2", "[");
		str.replace("$3", "]");
		return new CepVariable(str);
	}
	
	
	public String toString(String indent) {
		String str = indent + "Output scheme entry: " + this.hashCode();
		indent += "  ";
		str += indent + "Label: " + getLabel();
		str += indent + "Expression " + this.expression.getValue();
		return str;
	}

	public Set<CepVariable> getVarNames() {
		return symbolTable.keySet();
	}
	
	protected void setValue_internal(CepVariable varName, Object newValue) {
		expression.getVar(symbolTable.get(varName)).setValue(newValue);
	}

	public Object getValueAsObject() {
		return expression.getValueAsObject();
	}

	@Override
	public String getErrorInfo() {
		return expression.getErrorInfo();
	}

	@Override
	public double getValue() {
		return expression.getValue();
	}
	
	

}
