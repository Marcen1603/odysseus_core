package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.LinkedList;

/**
 * Objekte dieser Klasse stellen das Ausgabeschema des EPA dar.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class OutputScheme {

	private LinkedList<AbstractOutputSchemeEntry> entries;

	/**
	 * Erzeugt ein neues leeres Ausgabeschema
	 */
	public OutputScheme() {
		this.entries = new LinkedList<AbstractOutputSchemeEntry>();
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
	public OutputScheme(LinkedList<AbstractOutputSchemeEntry> entries) {
		this.entries = entries;
	}

	/**
	 * Liefert eine Liste von Ausdrücken, die die Werte der Ausgabe definieren.
	 * 
	 * @return Liste von Ausdrücken, die die auszugebenden Werte definieren.
	 */
	public LinkedList<AbstractOutputSchemeEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(LinkedList<AbstractOutputSchemeEntry> entries) {
		this.entries = entries;
	}

	public String toString(String indent) {
		String str = indent + "OutputScheme: " + this.hashCode();
		indent += "  ";
		for (AbstractOutputSchemeEntry entry : this.entries) {
			str += entry.toString(indent);
		}
		return str;
	}

}
