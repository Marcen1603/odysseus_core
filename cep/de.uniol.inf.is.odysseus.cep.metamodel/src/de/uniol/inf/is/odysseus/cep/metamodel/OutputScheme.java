package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.LinkedList;

/**
 * Objekte dieser Klasse stellen das Ausgabeschema des EPA dar.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class OutputScheme {

	private LinkedList<OutputSchemeEntry> entries;

	/**
	 * Erzeugt ein neues leeres Ausgabeschema
	 */
	public OutputScheme() {
		this.entries = new LinkedList<OutputSchemeEntry>();
	}

	/**
	 * Erzeugt ein neues Ausgabeschema aus einer Liste von
	 * Ausgabeschema-Einträgen
	 * 
	 * @param entries
	 *            Nicht leere Liste von Ausgabeschema-Einträgen, die die
	 *            Ausgaben des komplexen Events beschreiben. Darf nicht null
	 *            sein.
	 */
	public OutputScheme(LinkedList<OutputSchemeEntry> entries) {
		this.entries = entries;
	}

	/**
	 * Liefert eine Liste von Ausdrücken, die die Werte der Ausgabe definieren.
	 * 
	 * @return Liste von Ausdrücken, die die auszugebenden Werte definieren.
	 */
	public LinkedList<OutputSchemeEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(LinkedList<OutputSchemeEntry> entries) {
		this.entries = entries;
	}

	public String toString(String indent) {
		String str = indent + "OutputScheme: " + this.hashCode();
		indent += "  ";
		for (OutputSchemeEntry entry : this.entries) {
			str += entry.toString(indent);
		}
		return str;
	}

}
