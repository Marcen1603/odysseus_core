package de.uniol.inf.is.odysseus.cep.metamodel;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.uniol.inf.is.odysseus.cep.metamodel.xml.adapter.SymbolTableOperationAdapter;

/**
 * Eintrag des Symboltabellenschemas.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SymbolTableSchemeEntry<T> {

	/**
	 * ID eines Events / Zustands. Darf nur aus Buchstaben und Ziffern bestehen,
	 * wobei das erste Zeichen ein Buchstabe sein muss. Darf nicht null oder
	 * leer sein.
	 */
	private String stateIdentifier;
	/**
	 * Index des Elements im StateBuffer. Negativ, wenn das oberste Element
	 * referenziert werden soll.
	 */
	private int index;
	/**
	 * ID des Attributs. Muss von der verwendeten Implementierung von @link
	 * {@link AbstractEventReader} aufgelöst werden können.
	 */
	private String attribute;
	/**
	 * Definiert die Operation, die bei der Aktualisierung der Symboltabelle
	 * ausgeführt werden soll.
	 */
	private SymbolTableOperation<T> operation;

	/**
	 * Erzeugt einen Eintrag-Objekt für das Symboltabellenschema
	 * 
	 * @param entryID
	 *            Die ID des Eintrags
	 * @param stateIdentifier
	 *            die ID des Events / Zustands
	 * @param index
	 *            der Index im {@link StateBuffer}
	 * @param attribute
	 *            Der Name des Attributs
	 * @param operation
	 *            Operation, die bei der Aktualisierung des Eintrags ausgeführt
	 *            werden soll
	 */
	public SymbolTableSchemeEntry(int entryID, String stateIdentifier,
			int index, String attribute, SymbolTableOperation<T> operation) {
		this.stateIdentifier = stateIdentifier;
		this.index = index;
		this.attribute = attribute;
		this.setOperation(operation);
	}

	/**
	 * leerer Standardkonstruktor
	 */
	public SymbolTableSchemeEntry() {
	}

	/**
	 * gibt den Namen der Variablen, auf die sich der Eintrag bezieht, in der
	 * internen Kodierung <operationname>_<statename>_<index>_<attributname>
	 * zurück
	 * 
	 * @return der Name der Variablen in der Kodierung
	 *         <operationname>_<statename>_<index>_<attributname>
	 */
	public String getVariableName() {
		String name = this.operation.getName() + CepVariable.getSeperator();  
		name += this.stateIdentifier + CepVariable.getSeperator();
		if (this.index >= 0) {
			name += this.index;
		}
		return name + CepVariable.getSeperator() + this.attribute;
	}

	/**
	 * Setzt den Operator für den Symboltabellenschema-Eintrag.
	 * 
	 * @param operation
	 *            Eine konkrete Implementierung der Symboltabellenoperation,
	 *            nicht null.
	 */
	public void setOperation(SymbolTableOperation<T> operation) {
		this.operation = operation;
	}

	/**
	 * Gibt die Symboltabellenoperation des Schema-Eintrags zurück.
	 * 
	 * @return Die Symboltabellenoperation des Eintrags.
	 */
	@XmlJavaTypeAdapter(SymbolTableOperationAdapter.class)
	public SymbolTableOperation<T> getOperation() {
		return operation;
	}

	/**
	 * Liefert den Namen des Events / Zustands, auf das sich der Eintrag im
	 * Symboltabellenschema bezieht.
	 * 
	 * @return Den Zustands-/Eventnamen.
	 */
	public String getStateIdentifier() {
		return stateIdentifier;
	}

	/**
	 * Setzt den Namen des Events / Zustands, auf das sich der Eintrag im
	 * Symboltabellensschema bezieht.
	 * 
	 * @param eventIdentifier
	 *            Neuer Name des Events / Zustands. Muss ein String sein, der
	 *            nur aus Buchstaben und Ziffern besteht, wobei das erste
	 *            Zeichen ein Buchstabe sein muss. Darf nicht null sein. Ein
	 *            leerer String kann übergeben werden, wenn sich der Eintrag
	 *            imer aufs aktuelle Attribut beziehen soll (obwohl dieser Fall
	 *            typischerweise nicht in der Symboltabelle auftaucht).
	 */
	public void setStateIdentifier(String eventIdentifier) {
		this.stateIdentifier = eventIdentifier;
	}

	/**
	 * Gibt den Index des Events im {@link StateBuffer} zurück, auf das sich der
	 * Eintrag bezieht.
	 * 
	 * @return Index des Events im {@link StateBuffer}. Ist negativ, wenn das
	 *         oberste Element des {@link StateBuffer} gemeint ist, ansonsten
	 *         positiv.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Setzt den Index des Events im {@link StateBuffer}, auf den sich der
	 * Eintrag bezieht.
	 * 
	 * @param index
	 *            Der neue Index. Negative wenn das oberste Element des
	 *            {@link StateBuffer} referenziert werden soll.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Liefert den Namen des Attributs, auf den sich der Eintrag im
	 * Symboltabellenschema bezieht.
	 * 
	 * @return Der Name des Attributs, auf das sich der Eintrag bezieht.
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * Setzt den Namen des Attributs, auf das sich der Eintrag im
	 * Symboltabellenschema beziehen soll.
	 * 
	 * @param attribute
	 *            Der neue Attributname, nicht null. Nicht leerer String, der
	 *            nur aus Buchstaben und Zahlen bestehen darf, wobei das erste
	 *            Zeichen ein Buchstabe sein muss.
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String toString(String indent) {
		String str = indent + "SymTabSchemeEntry: " + this.hashCode();
		indent += "  ";
		str += indent + "Variable name: " + this.getVariableName();
		str += this.operation.toString(indent);
		return str;
	}

}
