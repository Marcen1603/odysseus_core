/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.cep.metamodel;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.ISymbolTableOperationFactory;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;

/**
 * Cep-Varible mit Moeglichkeiten zur Konvertierung in einen String.
 * 
 * 
 * Die internen Variablennamen sind nach folgendem Muster aufgebaut:
 * <OperationName>_<StateID>_<Index>_<AttributName>
 * 
 * wobei das hier dargestellte Trennzeichen _ hier definiert werden kann
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
 * Event bezieht (nur fuer Operation Count)
 * 
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
public class CepVariable implements Serializable{

	private static final long serialVersionUID = 5673792398657038594L;
	private static ISymbolTableOperationFactory symTabOpFac = null;
	
	static public void setSymbolTableOperationFactory(ISymbolTableOperationFactory symTabOpFactory) {
		symTabOpFac = symTabOpFactory;
	}
	
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
	private Integer index;
	/**
	 * ID des Attributs. Muss von der verwendeten Implementierung von @link
	 * {@link AbstractEventReader} aufgelöst werden können.
	 */
	// TODO: Hier sollte man nicht einen Attributenamen, sondern eine Expression
	// erlauben. Dann k�nnte man auch sum(i[1]-i[1]) oder so etwas machen ...
	private String attributename;
	/**
	 * Definiert die Operation, die bei der Aktualisierung der Symboltabelle
	 * ausgeführt werden soll.
	 */
	@SuppressWarnings("rawtypes")
	private IAggregateFunction operation;

	/**
	 * Erzeugt einen Eintrag-Objekt für das Symboltabellenschema
	 * 
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
	@SuppressWarnings("rawtypes")
	public CepVariable(String stateIdentifier, Integer index, String attribute, IAggregateFunction operation) {
		this.stateIdentifier = stateIdentifier;
		this.index = index;
		this.attributename = attribute;
		this.setOperation(operation);
	}

	public CepVariable(String varName) {
		String[] split = varName.split(getSeperator());
		this.operation = symTabOpFac.getOperation(split[0]);
		this.stateIdentifier = split[1];
		this.index = -1;
		if (split[2].length() > 0){
			try{
				this.index = Integer.parseInt(split[2]);
			}catch(NumberFormatException e){
				System.err.println("NumberFormatException");
			}
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
	@SuppressWarnings("rawtypes")
	public void setOperation(IAggregateFunction operation) {
		this.operation = operation;
	}

	/**
	 * Gibt die Symboltabellenoperation des Schema-Eintrags zurück.
	 * 
	 * @return Die Symboltabellenoperation des Eintrags.
	 */
	@SuppressWarnings("rawtypes")
	public IAggregateFunction getOperation() {
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
	public Integer getIndex() {
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
	public void setIndex(Integer index) {
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
		String str = "CEP VarName: " + this.getVariableName();
		return str;
	}

	@Override
	public String toString() {
		return toString("");
	}
	
	public static String getSeperator() {
		return "_";
	}

	public static String createAttribute(String name){
		return getSeperator()+getSeperator()+getSeperator()+name;
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
		}
        return false;
	}

	public static String getStringFor(String operation, String stateIdentifier, String index, String attribute) {
		return operation+getSeperator()+stateIdentifier+getSeperator()+(index!=null?index:"")+getSeperator()+attribute;
		
	}

	public boolean isActEventName() {
		return (this.stateIdentifier == null || stateIdentifier.equals("")) && (this.index == null || this.index <0) && (this.operation == null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributename == null) ? 0 : attributename.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		result = prime * result
				+ ((operation == null) ? 0 : operation.hashCode());
		result = prime * result
				+ ((stateIdentifier == null) ? 0 : stateIdentifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CepVariable other = (CepVariable) obj;
		if (attributename == null) {
			if (other.attributename != null)
				return false;
		} else if (!attributename.equals(other.attributename))
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		if (operation == null) {
			if (other.operation != null)
				return false;
		} else if (!operation.equals(other.operation))
			return false;
		if (stateIdentifier == null) {
			if (other.stateIdentifier != null)
				return false;
		} else if (!stateIdentifier.equals(other.stateIdentifier))
			return false;
		return true;
	}

	
}
