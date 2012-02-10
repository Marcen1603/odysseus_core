/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.relational.base.schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SchemaHelper;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SchemaIndex;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SchemaIndexPath;

/**
 * Iteratorklasse, um über ein oder mehrere Tuple zu iterieren. Die Tuple können
 * objektrelational sein. Dazu wird das <code>foreach</code>-Konstrukt genutzt.
 * Die Zählvariable ist vom Typ <code>TupleInfo</code>. Dort sind während der
 * Iteration alle notwendigen Informationen enthalten. Über unterschiedliche
 * Konstruktoren kann bestimmt werden, wie sich der Iterator verhalten soll.
 * <p>
 * <b>Beispielanwendung</b><br>
 * <code>for( TupleInfo tupleInfo : new TupleIterator(tuple, schema)) {<br>
 * } </code> <br>
 * Es sollte vermieden werden, den Iterator nach einer Iteration erneut zu
 * verwenden.
 * 
 * @author Timo Michelsen
 */
public class TupleIterator implements Iterable<TupleInfo>, Iterator<TupleInfo> {

	// Interne Klasse zur Verwaltung
	private class IteratorEntry {

		public Object obj;
		public int index;

		public IteratorEntry(Object obj) {
			this.obj = obj;
			this.index = 0;
		}
	}

	private RelationalTuple<?> tuple;
	private Stack<IteratorEntry> pointer = new Stack<IteratorEntry>();
	private Stack<SchemaIndex> schemaIndices = new Stack<SchemaIndex>();
	private Stack<Boolean> insideList = new Stack<Boolean>();
	private Stack<TupleIndex> tupleIndices = new Stack<TupleIndex>();
	private int maxLevels;

	private SchemaIndexPath schemaStart = null;
	private TupleIndexPath tupleStart = null;

	/**
	 * Konstruktor. Erstellt eine neue <code>TupleIterator</code>-Instanz. Die
	 * Iteration erfolgt über den angegeben vollständigen Tupel. Das Tupel kann
	 * objektrelational sein. Dabei beginnt die Iteration bei dem angegeben
	 * SchemaIndexPath im Tupel und geht so tief wie möglich. Alle
	 * übergeordneten Tupel werden nicht erfasst. Sollen <b>alle</b> Attribute
	 * in einem Tupel erfasst werden, so sollte der Konstruktor
	 * <code>TupleIterator(MVRelationalTuple<?>,SDFSchema)</code> genutzt
	 * werden.
	 * 
	 * @param tuple
	 *            Vollständiges, evtl. objektrelationales Tupel, worin iteriert
	 *            werden soll. Darf nicht <code>null</code> sein.
	 * @param start
	 *            SchemaIndexPath zu einer Stelle im Tuple, wo mit der Iteration
	 *            begonnen werden soll. Darf nicht <code>null</code> sein.
	 */
	public TupleIterator(RelationalTuple<?> tuple, SchemaIndexPath start) {
		this(tuple, start, Integer.MAX_VALUE);
	}

	/**
	 * Konstruktor. Erstellt eine neue <code>TupleIterator</code>-Instanz. Die
	 * Iteration erfolgt über den angegeben vollständigen Tupel. Von Anfang bis
	 * Ende. Auf die Tiefe wird keine Rücksicht genommen. Das Tupel kann
	 * objektrelational sein. Die Iteration erfolgt über den <b>ganzen</b>
	 * Tupel.
	 * 
	 * @param tuple
	 *            Vollständiges, evtl. objektrelationales Tupel, worin iteriert
	 *            werden soll. Darf nicht <code>null</code> sein.
	 * @param completeSchema
	 *            Zum Tupel zugehöriges Schema. Muss vollständig sein, darf
	 *            nicht <code>null</code> sein.
	 */
	public TupleIterator(RelationalTuple<?> tuple, SDFSchema completeSchema) {
		this(tuple, completeSchema, Integer.MAX_VALUE);
	}

	/**
	 * Konstruktor. Erstellt eine neue <code>TupleIterator</code>-Instanz. Die
	 * Iteration erfolgt über den angegeben vollständigen Tupel.Dabei wird nur
	 * maximal die angegebene Tiefe berücksichtigt. Die Tiefenangabe ist relativ
	 * zur Schemawurzel. Das Tupel kann objektrelational sein. Die Iteration
	 * erfolgt über den <b>ganzen</b> Tupel. <br>
	 * <br>
	 * <code>maxLevels=0</code> Es wird nicht in der Tiefe iteriert.<br>
	 * <code>maxLevels=1</code> Es wird nur die unmittelbare nächste Tiefe
	 * iteriert.<br>
	 * usw.
	 * 
	 * @param tuple
	 *            Vollständiges, evtl. objektrelationales Tupel, worin iteriert
	 *            werden soll. Darf nicht <code>null</code> sein.
	 * @param completeSchema
	 *            Zum Tupel zugehöriges Schema. Muss vollständig sein, darf
	 *            nicht <code>null</code> sein.
	 * @param maxLevels
	 *            Anzahl Ebenen in die Tiefe, durch die maximal iteriert werden
	 *            soll.
	 */
	public TupleIterator(RelationalTuple<?> tuple, SDFSchema completeSchema, int maxLevels) {
		this.tuple = tuple;
		this.maxLevels = maxLevels;

		SDFAttribute attr = completeSchema.get(0);
		String name = attr.getAttributeName();

		SchemaHelper hlp = new SchemaHelper(completeSchema);
		SchemaIndexPath path = hlp.getSchemaIndexPath(name);

		reset(path);
	}

	/**
	 * Konstruktor. Erstellt eine neue <code>TupleIterator</code>-Instanz. Die
	 * Iteration erfolgt über den angegeben vollständigen Tupel. Das Tupel kann
	 * objektrelational sein. Dabei beginnt die Iteration bei dem angegeben
	 * TupleIndexPath im Tupel und geht so tief wie möglich. Alle übergeordneten
	 * Tupel werden nicht erfasst. Sollen <b>alle</b> Attribute in einem Tupel
	 * erfasst werden, so sollte der Konstruktor
	 * <code>TupleIterator(MVRelationalTuple<?>,SDFSchema)</code> genutzt
	 * werden.
	 * 
	 * @param tuple
	 *            Vollständiges, evtl. objektrelationales Tupel, worin iteriert
	 *            werden soll. Darf nicht <code>null</code> sein.
	 * @param start
	 *            SchemaIndexPath zu einer Stelle im Tuple, wo mit der Iteration
	 *            begonnen werden soll. Darf nicht <code>null</code> sein.
	 */
	public TupleIterator(RelationalTuple<?> tuple, TupleIndexPath start) {
		this(tuple, start, Integer.MAX_VALUE);
	}

	/**
	 * Konstruktor. Erstellt eine neue <code>TupleIterator</code>-Instanz. Die
	 * Iteration erfolgt über den angegeben vollständigen Tupel. Dabei beginnt
	 * die Iteration bei dem angegeben TupleIndexPath im Tupel. Alle
	 * übergeordneten Tupel werden nicht erfasst. Dabei wird nur maximal die
	 * angegebene Tiefe berücksichtigt. Die Tiefenangabe ist relativ zum Tupel,
	 * welcher mit dem TupleIndexPath angegeben wurde.Das Tupel kann
	 * objektrelational sein. Die Iteration erfolgt über den <b>ganzen</b>
	 * Tupel. <br>
	 * <br>
	 * <code>maxLevels=0</code> Es wird nicht in der Tiefe iteriert.<br>
	 * <code>maxLevels=1</code> Es wird nur die unmittelbare nächste Tiefe
	 * iteriert.<br>
	 * usw.
	 * 
	 * @param tuple
	 *            Vollständiges, evtl. objektrelationales Tupel, worin iteriert
	 *            werden soll. Darf nicht <code>null</code> sein.
	 * @param start
	 *            TupleIndexPath zu einer Stelle im Tuple, wo mit der Iteration
	 *            begonnen werden soll. Darf nicht <code>null</code> sein.
	 * @param maxLevels
	 *            Anzahl Ebenen in die Tiefe, durch die maximal iteriert werden
	 *            soll. Muss 0 oder positiv sein.
	 */
	public TupleIterator(RelationalTuple<?> tuple, TupleIndexPath start, int maxLevels) {
		this.tuple = tuple;
		this.maxLevels = maxLevels;
		reset(start);
	}

	/**
	 * Konstruktor. Erstellt eine neue <code>TupleIterator</code>-Instanz. Die
	 * Iteration erfolgt über den angegeben vollständigen Tupel. Dabei beginnt
	 * die Iteration bei dem angegeben SchemaIndexPath im Tupel. Alle
	 * übergeordneten Tupel werden nicht erfasst. Dabei wird nur maximal die
	 * angegebene Tiefe berücksichtigt. Die Tiefenangabe ist relativ zum Tupel,
	 * welcher mit dem SchemaIndexPath angegeben wurde.Das Tupel kann
	 * objektrelational sein. Die Iteration erfolgt über den <b>ganzen</b>
	 * Tupel. <br>
	 * <br>
	 * <code>maxLevels=0</code> Es wird nicht in der Tiefe iteriert.<br>
	 * <code>maxLevels=1</code> Es wird nur die unmittelbare nächste Tiefe
	 * iteriert.<br>
	 * usw.
	 * 
	 * @param tuple
	 *            Vollständiges, evtl. objektrelationales Tupel, worin iteriert
	 *            werden soll. Darf nicht <code>null</code> sein.
	 * @param start
	 *            SchemaIndexPath zu einer Stelle im Tuple, wo mit der Iteration
	 *            begonnen werden soll. Darf nicht <code>null</code> sein.
	 * @param maxLevels
	 *            Anzahl Ebenen in die Tiefe, durch die maximal iteriert werden
	 *            soll. Muss 0 oder positiv sein.
	 */
	public TupleIterator(RelationalTuple<?> tuple, SchemaIndexPath start, int maxLevels) {
		this.tuple = tuple;
		this.maxLevels = maxLevels;
		reset(start);
	}

	// private Methode: liefert ein Objekt nach dem Path
	private Object get(SchemaIndexPath path) {
		if (path == null || path.getLength() == 0)
			return tuple;

		TupleHelper tupleHelper = new TupleHelper(tuple);
		return tupleHelper.getObject(path);
	}

	// private Methode: set den Iterator zur Wiederverwendung zurück
	private void reset() {
		if (tupleStart != null)
			reset(tupleStart);
		else
			reset(schemaStart);
	}

	// private Methode: set den Iterator zur Wiederverwendung zurück
	private void reset(TupleIndexPath tupleStart) {
		this.tupleStart = tupleStart.clone();
		this.schemaStart = null;

		SchemaIndexPath start = tupleStart.toSchemaIndexPath();

		pointer.clear();
		schemaIndices.clear();
		insideList.clear();
		tupleIndices.clear();

		if (start != null) {
			IteratorEntry e = new IteratorEntry(tupleStart.getTupleObject());
//			e.index = tupleStart.getTupleIndices().get(tupleStart.getLength() - 1).toInt();
			e.index = 0;

			this.pointer.push(e);

			for (int i = 0; i < start.getSchemaIndices().size(); i++) {
				schemaIndices.push(start.getSchemaIndex(i));

				tupleIndices.push(tupleStart.getTupleIndices().get(i));

				if (insideList.isEmpty())
					insideList.push(start.getSchemaIndex(0).isList());
				else
					insideList.push(start.getSchemaIndex(i).isList() | insideList.peek());
			}

		} else {
			throw new IllegalArgumentException("start is null");
		}
	}

	// private Methode: set den Iterator zur Wiederverwendung zurück
	private void reset(SchemaIndexPath start) {
		this.tupleStart = null;
		this.schemaStart = start.clone();

		pointer.clear();
		schemaIndices.clear();
		insideList.clear();
		tupleIndices.clear();

		Object parent = tuple;
		if (start != null) {
			pointer.push(new IteratorEntry(get(start)));

			for (int i = 0; i < start.getSchemaIndices().size(); i++) {
				schemaIndices.push(start.getSchemaIndex(i));

				tupleIndices.push(new TupleIndex((RelationalTuple<?>) parent, schemaIndices.peek().toInt(), schemaIndices.peek().getAttribute()));
				if (parent instanceof RelationalTuple)
					parent = ((RelationalTuple<?>) parent).getAttribute(schemaIndices.peek().toInt());
				else
					throw new RuntimeException("Corrupted SchemaIndexPath: " + start);

				if (insideList.isEmpty())
					insideList.push(start.getSchemaIndex(0).isList());
				else
					insideList.push(start.getSchemaIndex(i).isList() | insideList.peek());
			}

		}
	}

	@Override
	public TupleInfo next() {
		if (pointer.isEmpty())
			return null;

		IteratorEntry entry = pointer.peek();
		SchemaIndex index = schemaIndices.peek();
		
		// Informationen sammeln
		TupleInfo info = new TupleInfo();
		info.tupleObject = entry.obj;
		info.schemaIndexPath = getSchemaIndexPath();
		info.attribute = schemaIndices.peek().getAttribute();
		info.tupleIndexPath = getTupleIndexPath();
		info.isInList = insideList.peek();
		info.level = tupleIndices.size() - 1;
		info.isTuple = (info.tupleObject != null ? (info.tupleObject instanceof RelationalTuple) : false);

		if (entry.obj instanceof RelationalTuple && pointer.size() <= maxLevels) {
			RelationalTuple<?> t = (RelationalTuple<?>) entry.obj;
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

				if (index.getAttribute().getDatatype().isMultiValue()) {
					if(index.getAttribute().getDatatype().hasSchema()){
						schemaIndices.push(new SchemaIndex(0, index.getAttribute().getDatatype().getSubSchema().getAttribute(0)));
					}
					else{
						schemaIndices.push(new SchemaIndex(0, index.getAttribute())); // FIXME: Ich hoffe, dass das stimmt.
					}
					insideList.push(true);
				} else {
					// seems that there must be subattributes
					schemaIndices.push(new SchemaIndex(entry.index, index.getAttribute().getDatatype().getSubSchema().getAttribute(entry.index)));
					insideList.push(insideList.peek());
				}

				Object parent = null;
				if (pointer.size() > 1)
					parent = pointer.get(pointer.size() - 2).obj;
				else
					parent = tuple;

				if (parent instanceof RelationalTuple) {
					tupleIndices.push(new TupleIndex((RelationalTuple<?>) parent, entry.index, schemaIndices.peek().getAttribute()));
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

		return info;
	}

	private SchemaIndexPath getSchemaIndexPath() {
		List<SchemaIndex> idx = new ArrayList<SchemaIndex>();
		for( int i = 0; i < schemaIndices.size();i++ ) {
			idx.add(schemaIndices.get(i).clone());
		}
		
		return new SchemaIndexPath(idx, schemaIndices.peek().getAttribute());
	}

	private TupleIndexPath getTupleIndexPath() {
		List<TupleIndex> lst = new ArrayList<TupleIndex>();
		for( int i = 0; i < tupleIndices.size(); i++ )
			lst.add(tupleIndices.get(i).clone());
		
		return new TupleIndexPath(lst, getSchemaIndexPath());
	}

	private boolean isFinished() {
		return pointer.isEmpty();
	}

	@Override
	public boolean hasNext() {
		if (isFinished()) {
			reset();
			return false;
		}
		return true;
	}

	@Override
	public void remove() {
	}

	@Override
	public Iterator<TupleInfo> iterator() {
		return this;
	}
}
