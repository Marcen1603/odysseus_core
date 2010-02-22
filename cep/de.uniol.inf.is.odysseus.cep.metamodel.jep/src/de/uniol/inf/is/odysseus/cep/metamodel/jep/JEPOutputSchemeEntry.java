package de.uniol.inf.is.odysseus.cep.metamodel.jep;

import java.util.Set;

import org.nfunk.jep.JEP;

import de.uniol.inf.is.odysseus.cep.metamodel.AbstractOutputSchemeEntry;
import de.uniol.inf.is.odysseus.cep.metamodel.exception.UndefinedExpressionLabelException;

public class JEPOutputSchemeEntry extends AbstractOutputSchemeEntry {
	/**
	 * JEP Expression, der den Wert der Ausgabe kodiert.
	 */
	private JEP expression;
	
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
	public void setLabel(String label) throws UndefinedExpressionLabelException {
		super.setLabel(label);
		this.expression = new JEP();
		this.expression.setAllowUndeclared(true);
		this.expression.parseExpression(label);
	}
	
	public String toString(String indent) {
		String str = indent + "Output scheme entry: " + this.hashCode();
		indent += "  ";
		str += indent + "Label: " + getLabel();
		str += indent + "Expression " + this.expression.getValue();
		return str;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getVarNames() {
		return expression.getSymbolTable().keySet();
	}
	
	public void setValue(String varName, Object newValue) {
		expression.getVar(varName).setValue(newValue);
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
