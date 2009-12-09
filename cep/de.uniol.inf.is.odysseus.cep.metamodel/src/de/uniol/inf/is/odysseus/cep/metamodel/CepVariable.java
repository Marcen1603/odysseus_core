package de.uniol.inf.is.odysseus.cep.metamodel;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.ISymbolTableOperation;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTableOperationFactory;
import de.uniol.inf.is.odysseus.cep.metamodel.xml.adapter.SymbolTableOperationAdapter;

/**
 * Cep-Varible mit Moeglichkeiten zur Konvertierung in einem String.
 * 
 * 
 * Die internen Variablennamen sind nach folgendem Muster aufgebaut:
 * <OperationName>#<StateID>#<Index>#<AttributName>
 * 
 * wobei das hier dargestellte Trennzeichen # hier definiert wird
 * 
 * OperationName: Name der Symboltabellenoperation. Bezieht sich der
 * Variablenname auf das aktuelle Event, so ist OperationName leer.
 * 
 * StateID: Name des Zustands, in dem das Event konsumiert wurde. Bezieht sich
 * der Variablenname auf das aktuelle Event, so ist StateID leer
 * 
 * Index: Der Index des Events im StateBuffer. Bezieht sich der Variablenname
 * auf das aktuelle Event, so ist Index leer
 * 
 * AttributName: Der Name des Attributs, auf das sich die Variable bezieht. Ist
 * leer, wenn sich die Variable nicht auf ein Attribut, sondern direkt auf ein
 * Event bezieht (nur für Operation Count)
 * 
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
public class CepVariable {

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
	private String attributename;
	/**
	 * Definiert die Operation, die bei der Aktualisierung der Symboltabelle
	 * ausgeführt werden soll.
	 */
	private ISymbolTableOperation operation;

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
	public CepVariable(int entryID, String stateIdentifier, int index,
			String attribute, ISymbolTableOperation operation) {
		this.stateIdentifier = stateIdentifier;
		this.index = index;
		this.attributename = attribute;
		this.setOperation(operation);
	}

	public CepVariable(String varName) {
		String[] split = varName.split(getSeperator());
		this.operation = SymbolTableOperationFactory.getOperation(split[0]);
		this.stateIdentifier = split[1];
		if (split[2].length() > 0){
			this.index = Integer.parseInt(split[2]);
		}else{
			this.index = -1;
		}
		this.attributename = split[3];
	}

	/**
	 * leerer Standardkonstruktor
	 */
	public CepVariable() {
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
		StringBuffer ret = new StringBuffer();
		if (this.operation != null) ret.append(this.operation.getName());
		ret.append(getSeperator());
		ret.append(this.stateIdentifier).append(getSeperator());
		if (this.index >= 0) {
			ret.append(this.index);
		}
		ret.append(getSeperator()).append(this.attributename);
		return ret.toString();
	}

	/**
	 * Setzt den Operator für den Symboltabellenschema-Eintrag.
	 * 
	 * @param operation
	 *            Eine konkrete Implementierung der Symboltabellenoperation,
	 *            nicht null.
	 */
	public void setOperation(ISymbolTableOperation operation) {
		this.operation = operation;
	}

	/**
	 * Gibt die Symboltabellenoperation des Schema-Eintrags zurück.
	 * 
	 * @return Die Symboltabellenoperation des Eintrags.
	 */
	@XmlJavaTypeAdapter(SymbolTableOperationAdapter.class)
	public ISymbolTableOperation getOperation() {
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
	 * Gibt den Index des Events im {@link StateBuffer} zurück, auf das sich
	 * der Eintrag bezieht.
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
		return attributename;
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
	public void setAttribute(String attributename) {
		this.attributename = attributename;
	}

	public String toString(String indent) {
		String str = indent + "SymTabSchemeEntry: " + this.hashCode();
		indent += "  ";
		str += indent + "Variable name: " + this.getVariableName();
		return str;
	}

	@Override
	public String toString() {
		return toString("");
	}
	
	public static String getSeperator() {
		return "_";
	}

	public static String getAttributeName(String varName) {
		String[] split = varName.split(getSeperator());
		return split[3];
	}

	/*
	 * **************************************************************************
	 * Hilfsmethode fuers Namensschema der Variablen *
	 * ***************************
	 * ***********************************************
	 */

	/**
	 * Überprüft, ob sich der übergebene Variablenname auf ein aktuelles
	 * Event bezieht.
	 * 
	 * @param name
	 *            Der zu prüfende Variablenname
	 * @return true, wenn sich der Variablenname auf ein aktuelles Event
	 *         bezieht, ansonsten false.
	 */
	public static boolean isActEventName(String name) {
		String[] split = name.split(CepVariable.getSeperator());
		if (split[0].isEmpty() && split[1].isEmpty() && split[2].isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

}
