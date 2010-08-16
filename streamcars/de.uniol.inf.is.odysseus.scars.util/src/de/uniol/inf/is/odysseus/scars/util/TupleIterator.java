package de.uniol.inf.is.odysseus.scars.util;

import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * Der TupleIterator bietet eine Funktionalität, um über einen Tupel zu
 * iterieren. Über den Konstruktor kann angegeben werden, an welchen Punkt im
 * Tupelbaum begonnen werden soll. Danach kann mit next() immer auf die nächsten
 * Tupel navigiert werden. reset() ermöglicht das zurücksetzen der Iteration auf
 * einen anderen Punkt. Mittels isFinished() lässt sich ermitteln, ob die
 * Iteration keinen Nachfolger mehr findet, also beendet ist.
 * 
 * @author Timo Michelsen
 * 
 */
public class TupleIterator {

	private class IteratorEntry {

		public Object obj;
		public int index;

		public IteratorEntry(Object obj) {
			this.obj = obj;
			this.index = 0;
		}
	}

	private MVRelationalTuple<?> tuple;
	private Stack<IteratorEntry> pointer = new Stack<IteratorEntry>();
	private Stack<SchemaIndex> schemaIndices = new Stack<SchemaIndex>();
	private Stack<Boolean> insideList = new Stack<Boolean>();
	private Stack<TupleIndex> tupleIndices = new Stack<TupleIndex>();
	private int maxLevels;

	/**
	 * Erstellt eine neue TupelIterator-Instanz. Über dem angegebenen Tupel wird
	 * iteriert. Dabei muss das vollstädige Tupel angegeben werden. Über einen
	 * SchemaIndexPath lässt sich der Knoten im Tupelbaum angeben, wo die
	 * Iteration beginnt. Beginnt dieser inmitten des Baums, so werden die
	 * Elternknoten nicht iteriert. Damit ist es möglich, nur durch Teilbäume zu
	 * iterieren. Ein Pfad lässt sich mittels der Klasse SchemaHelper erstellen.
	 * 
	 * Der Navigator geht beliebig in die Tiefe.
	 * 
	 * Es ist zu beachten, dass Tupel, welche Untertupel besitzen, auch besucht
	 * werden, bevor in die Kindtupel fortgesetzt wird.
	 * 
	 * @param tuple
	 *            Durchzunavigierender vollständiger Tupel. Darf nicht null sein.
	 * @param start
	 *            Pfad zum Startpunkt innerhalb des Tupels. Darf nicht null
	 *            sein.
	 */
	public TupleIterator(MVRelationalTuple<?> tuple, SchemaIndexPath start) {
		this(tuple, start, Integer.MAX_VALUE);
	}

	/**
	 * Erstellt eine neue TupelIterator-Instanz. Über dem angegebenen Tupel wird
	 * iteriert. Mittels diesem Konstruktor wird über den Tuple iteriert, also
	 * von der Wurzel an. Warnung: Die Wurzel selbst wird NICHT erfasst.
	 * 
	 * Der Navigator geht beliebig in die Tiefe.
	 * 
	 * Es ist zu beachten, dass Tupel, welche Untertupel besitzen, auch besucht
	 * werden, bevor in die Kindtupel fortgesetzt wird.
	 * 
	 * @param tuple
	 *            Durchzunavigierender Tupel. Darf nicht null sein.
	 * @param completeSchema
	 *            Komplettes Schema des Tupels. Darf nicht null sein.
	 */
	public TupleIterator(MVRelationalTuple<?> tuple, SDFAttributeList completeSchema) {
		this(tuple, completeSchema, Integer.MAX_VALUE);
	}

	/**
	 * Erstellt eine neue TupelIterator-Instanz. Über dem angegebenen Tupel wird
	 * iteriert. Mittels diesem Konstruktor wird über den Tupel iteriert, also
	 * von der Wurzel an. Warnung: Die Wurzel selbst wird NICHT erfasst.
	 * 
	 * Über maxLevels lässt sich die maximale Tiefe bestimmen, wie weit der
	 * Iterator iterieren soll. Ist maxLevels=0, so wird nicht in die Tiefe
	 * gegangen, die Iteration ist nach einem next() vorbei. maxLevels=1
	 * bewirkt, dass nur durch die unmittelbaren Untertupel navigiert wird.
	 * 
	 * Es ist zu beachten, dass Tupel, welche Untertupel besitzen, auch besucht
	 * werden, bevor in die Kindtupel fortgesetzt wird.
	 * 
	 * @param tuple
	 *            Durchzunavigierender Tupel. Darf nicht null sein.
	 * @param completeSchema
	 *            Komplettes Schema des Tupels. Darf nicht null sein.
	 * @param maxLevels
	 *            Maximale Iterationstiefe. Muss positiv oder null sein.
	 */
	public TupleIterator(MVRelationalTuple<?> tuple, SDFAttributeList completeSchema, int maxLevels) {
		this.tuple = tuple;
		this.maxLevels = maxLevels;

		SDFAttribute attr = completeSchema.get(0);
		String name = attr.getAttributeName();

		SchemaHelper hlp = new SchemaHelper(completeSchema);
		SchemaIndexPath path = hlp.getSchemaIndexPath(name);

		reset(path);
	}

	/**
	 * Erstellt eine neue TupelIterator-Instanz. Über dem angegebenen Tupel wird
	 * iteriert. Über einen SchemaIndexPath lässt sich der Knoten im Tupelbaum
	 * angeben, wo die Iteration beginnt. Beginnt dieser inmitten des Baums, so
	 * werden die Elternknoten nicht iteriert. Damit ist es möglich, nur durch
	 * Teilbäume zu iterieren. Ein Pfad lässt sich mittels der Klasse
	 * SchemaHelper erstellen.
	 * 
	 * Über maxLevels lässt sich die maximale Tiefe bestimmen, wie weit der
	 * Iterator iterieren soll. Ist maxLevels=0, so wird nicht in die Tiefe
	 * gegangen, die Iteration ist nach einem next() vorbei. maxLevels=1
	 * bewirkt, dass nur durch die unmittelbaren Untertupel navigiert wird.
	 * 
	 * Es ist zu beachten, dass Tupel, welche Untertupel besitzen, auch besucht
	 * werden, bevor in die Kindtupel fortgesetzt wird.
	 * 
	 * @param tuple
	 *            Durchzunavigierender Tupel. Darf nicht null sein.
	 * @param start
	 *            Pfad zum Startpunkt innerhalb des Tupels. Darf nicht null
	 *            sein.
	 * @param maxLevels
	 *            Maximale Iterationstiefe. Muss positiv oder null sein.
	 */
	public TupleIterator(MVRelationalTuple<?> tuple, SchemaIndexPath start, int maxLevels) {
		this.tuple = tuple;
		this.maxLevels = maxLevels;
		reset(start);
	}

	// private Methode
	private Object get(SchemaIndexPath path) {
		if (path == null || path.getLength() == 0)
			return tuple;

		TupleHelper tupleHelper = new TupleHelper(tuple);
		return tupleHelper.getObject(path);
	}

	/**
	 * Setzt die Iteration auf einen neuen Punkt innerhalb des Tupels zurück.
	 * 
	 * @param start
	 *            Pfad zum Startpunkt innerhalb des Tupels.
	 */
	public void reset(SchemaIndexPath start) {
		pointer.clear();
		schemaIndices.clear();
		insideList.clear();
		tupleIndices.clear();

		if (start != null) {
			pointer.push(new IteratorEntry(get(start)));

			for (int i = 0; i < start.getSchemaIndices().size(); i++) {
				schemaIndices.push(start.getSchemaIndex(i));

				Object parent = null;
				if (pointer.size() > 1)
					parent = pointer.get(pointer.size() - 2).obj;
				else
					parent = tuple;

				if (parent instanceof MVRelationalTuple) {
					tupleIndices.push(new TupleIndex((MVRelationalTuple<?>) parent, schemaIndices.peek().toInt(), schemaIndices.peek().getAttribute()));
				} else {
					// something wrong
					throw new RuntimeException("Programming error");
				}

				if (insideList.isEmpty())
					insideList.push(start.getSchemaIndex(0).isList());
				else
					insideList.push(start.getSchemaIndex(i).isList() | insideList.peek());
			}

		} else {
			throw new IllegalArgumentException("start is null");
		}

	}

	/**
	 * Setzt den Iterator auf den nächsten Tupel, falls vorhanden. Es sollte
	 * vorher mittels isFinished() geprüt werden, ob die Iteration fortgesetzt
	 * werden kann. Wird next() aufgerufen, obwohl die Iteration beendet ist,
	 * passiert nichts.
	 */
	public void next() {
		if (pointer.isEmpty())
			return;

		IteratorEntry entry = pointer.peek();
		SchemaIndex index = schemaIndices.peek();

		if (entry.obj instanceof MVRelationalTuple && pointer.size() <= maxLevels) {
			MVRelationalTuple<?> t = (MVRelationalTuple<?>) entry.obj;
			int size = t.getAttributeCount();

			if (entry.index == size) {
				// Listenende erreicht
				pointer.pop();
				schemaIndices.pop();
				insideList.pop();
				tupleIndices.pop();
				next(); // nächstes ausprobieren
			} else {
				pointer.push(new IteratorEntry(t.getAttribute(entry.index)));
				// tupleIndices.push(entry.index);

				if (index.getAttribute().getDatatype().getQualName().equals("List")) {
					schemaIndices.push(new SchemaIndex(0, index.getAttribute().getSubattribute(0)));
					insideList.push(true);
				} else {
					schemaIndices.push(new SchemaIndex(entry.index, index.getAttribute().getSubattribute(entry.index)));
					insideList.push(insideList.peek());
				}

				Object parent = null;
				if (pointer.size() > 1)
					parent = pointer.get(pointer.size() - 2).obj;
				else
					parent = tuple;

				if (parent instanceof MVRelationalTuple) {
					tupleIndices.push(new TupleIndex((MVRelationalTuple<?>) parent, entry.index, schemaIndices.peek().getAttribute()));
				} else {
					// something wrong
					throw new RuntimeException("Programming error");
				}

				entry.index++;
			}
		} else {
			// kein tupel
			pointer.pop();
			schemaIndices.pop();
			insideList.pop();
			tupleIndices.pop();
			next();
		}
	}

	/**
	 * Liefert das TupelObjekt, worauf der Iterator gerade zeigt.
	 * 
	 * @return
	 */
	public Object getTupleObject() {
		return pointer.peek().obj;
	}

	/**
	 * Liefert den SchemaIndexPath des Tupelobjektes, worauf der Iterator gerade
	 * zeigt. Korrespondiert zum Schema.
	 * 
	 * @return Tupelpfad des Tupelobjektes
	 */
	public SchemaIndexPath getSchemaIndexPath() {
		return new SchemaIndexPath(schemaIndices.subList(0, schemaIndices.size()), schemaIndices.peek().getAttribute());
	}

	/**
	 * Liefert das korrespondierende Schemaattribut des Tupelobjektes, worauf
	 * der Iterator gerade zeigt.
	 * 
	 * @return
	 */
	public SDFAttribute getAttribute() {
		return schemaIndices.peek().getAttribute();
	}

	/**
	 * Liefert den Tupelpfad des Tupelobjektes, worauf der Iterator gerade
	 * zeigt.
	 * 
	 * @return Tupelpfad des Tupelobjektes
	 */
	public TupleIndexPath getTupleIndexPath() {
		List<TupleIndex> list = tupleIndices.subList(0, tupleIndices.size());
		return new TupleIndexPath(list, getSchemaIndexPath());
	}

	/**
	 * Liefert true zurück, wenn der Iterator gerade ein Attribut mit dem
	 * Datentyp "List" durchläuft. Dabei ist es unerheblich, in welcher
	 * Hierarchieebende sich diese befindet.
	 * 
	 * @return
	 */
	public boolean isInList() {
		return insideList.peek();
	}

	/**
	 * Liefert true, wenn die Iteration beendet ist. Wenn true, zeigt der
	 * Iterator auf nichts, sodass andere Zugriffsmethoden fehlschlagen. Mit
	 * reset() kann der Iterator zurückgesetzt werden.
	 * 
	 * @return true, falls Iteration beendet
	 */
	public boolean isFinished() {
		return pointer.isEmpty();
	}

	/**
	 * Liefert die aktuelle Hierarchietiefe.
	 * 
	 * @return Hierarchietiefe
	 */
	public int getLevel() {
		return tupleIndices.size() - 1;
	}
}
