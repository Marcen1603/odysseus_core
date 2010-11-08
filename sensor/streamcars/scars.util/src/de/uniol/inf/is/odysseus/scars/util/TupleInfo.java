package de.uniol.inf.is.odysseus.scars.util;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * Einfache Klasse, welche alle Informationen während einer Iteration durch
 * Tuple zusammenfasst. Sie dient als Iterationsvariable. Sie wird nach jedem
 * Durchlauf neu erstellt. Sie beinhaltet das TupleObjekt, IndexPaths, das
 * zugehörige SDFAttribute usw. Änderungen innerhalb dieser Klasse haben keinen
 * Einfluss auf die Iteration selbst.
 * <p>
 * Nutzern ist es nicht gestattet, eigene Instanzen von dieser Klasse zu
 * erstellen.
 * 
 * @author Timo Michelsen.
 * 
 */
public class TupleInfo {

	/**
	 * Das aktuelle Object/Tupel, worauf der Iterator gerade zeigt. 
	 */
	public Object tupleObject = null;

	/**
	 * Das SchemaIndexPath, welcher dem aktuellen Object/Tupel zugeordnet ist.
	 * Ist nie <code>null</code>.
	 */
	public SchemaIndexPath schemaIndexPath = null;

	/**
	 * Das dem Object/Tupel korrespondierende SDFAttribut im Schema. Ist nie
	 * <code>null</code>.
	 */
	public SDFAttribute attribute = null;

	/**
	 * Das TupleIndexPath des Object/Tupels. Damit lässt sich der genaue Weg
	 * von der Wurzel bis zum Attribut/Tupel selbst nachvollziehen. Ist nie
	 * <code>null</code>.
	 */
	public TupleIndexPath tupleIndexPath = null;

	/**
	 * <code>true</code>, wenn mindestens ein Tupel im Pfad vom Datentyp "List"
	 * ist, sonst <code>false</code>.
	 */
	public boolean isInList = false;

	/**
	 * Hierachieebene im Schema/Tupel. Ist immer 0 oder positiv.
	 */
	public int level = -1;

	/**
	 * <code>true</code>, wenn es sich um ein Tupel(MVRelationalTuple) handelt,
	 * sonst <code>false</code>.
	 */
	public boolean isTuple = false;

	// Nutzer soll keine eigenen Instanzen erstellen
	TupleInfo() {
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(tupleObject);
		if (isTuple)
			sb.append("[TUPLE]");
		sb.append(", SchemaPath=").append(schemaIndexPath);
		sb.append(", SDFAttribute=").append(attribute).append(", TuplePath=").append(tupleIndexPath);
		sb.append(", isInList=").append(isInList).append(", Level=").append(level).append("}");
		return sb.toString();
	}
}
