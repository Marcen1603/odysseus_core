package de.uniol.inf.is.odysseus.cep.metamodel;

import org.nfunk.jep.JEP;

import de.uniol.inf.is.odysseus.cep.epa.exceptions.UndefinedExpressionLabelException;

/**
 * Eine Instanz diser Klasse stellt einen Eintrag im Ausgabeschema dar.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class OutputSchemeEntry {

	/**
	 * textuelle Darstellung des Ausdrucks
	 */
	private String label;
	/**
	 * JEP Expression, der den Wert der Ausgabe kodiert.
	 */
	private JEP expression;

	public OutputSchemeEntry() {
	}

	/**
	 * Erzeugt einen Eintrag für das Ausgabeschema aus einer textuellen
	 * Beschreibung des Ausdrucks.
	 * 
	 * @param label
	 *            textuelle Darstellung des Ausdrucks. Nicht null.
	 */
	public OutputSchemeEntry(String label) {
		this.setLabel(label);
	}

	/**
	 * Setzt die textuelle Darstellung des Ausgabeschema-Eintrags und erzeugt
	 * daraus automatisch einen Ausdrucksbaum.
	 * 
	 * @param label De textuelle darstellung des Ausgabeschema-Eintrags.
	 * @throws UndefinedExpressionLabelException Falls das Label null oder leer ist.
	 */
	public void setLabel(String label) throws UndefinedExpressionLabelException {
		if (label == null) {
			throw new UndefinedExpressionLabelException();
		} else if (label.isEmpty()) {
			throw new UndefinedExpressionLabelException();
		} else {
			this.label = label;
			this.expression = new JEP();
			this.expression.setAllowUndeclared(true);
			this.expression.parseExpression(label);
		}
	}

	/**
	 * Gibt das Label des Ausgabeschema-Eintrags zurück.
	 * 
	 * @return Das Label des Eintrags.
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Liefert den Wert des Ausgabeschema-Eintrags als JEP-Ausdruck.
	 * 
	 * @return JEP-Ausruck, der den Auszugebenden Ausdruck codiert.
	 */
	public JEP getExpression() {
		return this.expression;
	}

	public String toString(String indent) {
		String str = indent + "Output scheme entry: " + this.hashCode();
		indent += "  ";
		str += indent + "Label: " + this.label;
		str += indent + "Expression" + this.expression.getValue();
		return str;
	}

}
