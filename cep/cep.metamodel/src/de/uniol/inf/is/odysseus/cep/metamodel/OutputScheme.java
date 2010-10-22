package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.LinkedList;

/**
 * Objekte dieser Klasse stellen das Ausgabeschema des EPA dar.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class OutputScheme {

	private LinkedList<IOutputSchemeEntry> entries;

	/**
	 * Erzeugt ein neues leeres Ausgabeschema
	 */
	public OutputScheme() {
		this.entries = new LinkedList<IOutputSchemeEntry>();
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
	public OutputScheme(LinkedList<IOutputSchemeEntry> entries) {
		this.entries = entries;
	}

	/**
	 * Liefert eine Liste von Ausdrücken, die die Werte der Ausgabe definieren.
	 * 
	 * @return Liste von Ausdrücken, die die auszugebenden Werte definieren.
	 */
	public LinkedList<IOutputSchemeEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(LinkedList<IOutputSchemeEntry> entries) {
		this.entries = entries;
	}

	public String toString(String indent) {
		String str = indent + "OutputScheme: " + this.hashCode();
		indent += "  ";
		for (IOutputSchemeEntry entry : this.entries) {
			str += entry.toString();
		}
		return str;
	}

	public void append(IOutputSchemeEntry e) {
		entries.add(e);
	}
	
	@Override
	public String toString() {
		return toString(" ");
	}

}
