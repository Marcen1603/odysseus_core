package de.uniol.inf.is.odysseus.scars.util;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * Einfache Klasse, welche alle Informationen während einer Iteration durch
 * Schemata zusammenfasst. Sie dient als Iterationsvariable für den
 * SchemaIterator. Sie wird nach jedem Durchlauf neu erstellt. Änderungen
 * innerhalb dieser Klasse haben keinen Einfluss auf die Iteration selbst.
 * <p>
 * Nutzern ist es nicht gestattet, eigene Instanzen von dieser Klasse zu
 * erstellen.
 * 
 * @author Timo Michelsen.
 * 
 */
public class SchemaInfo {

	/**
	 * Das SDFAttribute, worauf die Iteration gerade zeigt.
	 */
	public SDFAttribute attribute = null;

	/**
	 * Das SchemaIndexPath zum aktuellen SDFAttribut, worauf die Iteration
	 * zeigt.
	 */
	public SchemaIndexPath schemaIndexPath = null;

	/**
	 * Aktuelle Iterationstiefe relativ zum Startpunkt.
	 */
	public int level = -1;

	/**
	 * <code>true</code>, wenn im aktuellen SchemaIndexPath mindestens ein
	 * Listenattribut vorkommt, sonst <code>false</code>.
	 */
	public boolean isInList = false;

	// Nutzer davon abhalten, eigene Instanzen davon
	// zu erzeugen
	SchemaInfo() {

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(attribute).append(", SchemaIndexPath=");
		sb.append(schemaIndexPath).append(", Level=").append(level);
		sb.append(", IsInList=").append(isInList).append("}");
		return sb.toString();
	}
}
