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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIndex;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIndexPath;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * Repräsentiert einen Pfad innerhalb eines Tupels. Dieser wurde so
 * implementiert, dass sich dieser nach der Instanziierung nicht ändern kann.
 * <p>
 * Diese Klasse kann als Iterator verwendet werden, um durch Listen zu
 * iterieren. Zeigt der TupleIndexPath auf eine List oder beinhaltet mindestens
 * eine Liste so, kann mittels dem <code>foreach</code>-Konstrukt durch die
 * Listenelemente iteriert werden. Die Iterationsvariable ist vom Typ
 * <code>TupleInfo</code>. Wurde mit einem TupleIndexPath-Instanz iteriert, so
 * sollte diese nicht weiter verwendet werden. Muss sie dennoch verwendet
 * werden, so sollte vor der Iteration mittels <code>clone()</code> eine Kopie
 * erzeugt werden.
 * <p>
 * Nutzer können nicht selbständig neue Instanzen erzeugen. Dies geschieht über
 * TupleIterator oder SchemaIndexPath. Dort werden sie verwendet.
 * 
 * @author Timo Michelsen
 * 
 */
public class TupleIndexPath implements Iterable<TupleInfo>, Iterator<TupleInfo> {

	private List<TupleIndex> indices;
	private SchemaIndexPath schemaIndexPath;
	private List<Integer> listIndices;
	
	// Interner Konstruktor
	TupleIndexPath(List<TupleIndex> indices, SchemaIndexPath schemaIndexPath) {
		this.indices = indices;
		this.schemaIndexPath = schemaIndexPath;
		this.listIndices = new ArrayList<Integer>();

		for (int i = 0; i < schemaIndexPath.getSchemaIndices().size(); i++) {
			SchemaIndex idx = schemaIndexPath.getSchemaIndex(i);
			if (idx.isList())
				this.listIndices.add(i);
		}
	}

	// Interner Konstruktor
	TupleIndexPath(TupleIndexPath other) {
		this.indices = new ArrayList<TupleIndex>();
		for (int i = 0; i < other.indices.size(); i++) {
			indices.add(other.indices.get(i).clone());
		}
		this.schemaIndexPath = other.schemaIndexPath;
		this.listIndices = new ArrayList<Integer>();
		for (Integer i : other.listIndices)
			this.listIndices.add(i);
	}

	/**
	 * Liefert einen neuen Tupelindexpfad, der dem urprünglichen
	 * gleicht, aber den neuen angegebenen Index erweitert wurde.
	 * 
	 * @param index Neuer Index, der an das neue Tupelindexpfad angehängt werden soll
	 * 
	 * @return Neuer Tupelindexpfad mit zusätzlichem Index
	 */
	public TupleIndexPath appendClone( int index ) {
		TupleIndexPath c = clone();
		SchemaIndexPath path = c.schemaIndexPath.appendClone(index);
		c.indices.add(new TupleIndex(c.getLastTupleIndex().getValue(), index, path.getAttribute()));
		c.listIndices.add(index);
		c.schemaIndexPath = path;
		return c;
	}
	
	/**
	 * Liefert die Indices des Pfades als Liste von TupleIndex zurück. Damit
	 * können genauere Informationen zu jedem Pfadschritt abgerufen werden. Die
	 * Reihenfolge der Indizes geht von der Wurzel bis zum betreffenden Knoten.
	 * 
	 * @return Liste von TupleIndex
	 */
	public List<TupleIndex> getTupleIndices() {
		return Collections.unmodifiableList(indices);
	}

	/**
	 * Liefert das TupleIndex an der angegeben Stelle im Pfad.
	 * 
	 * @param index
	 *            Index im Pfad.
	 * @return Index im Tuple.
	 */
	public TupleIndex getTupleIndex(int index) {
		return indices.get(index);
	}

	/**
	 * Liefert das letzte Tupleindex im Pfad.
	 * 
	 * @return Letzten Tupleindex.
	 */
	public TupleIndex getLastTupleIndex() {
		return getTupleIndex(indices.size() - 1);
	}

	/**
	 * Liefert den korrespondierenden SchemaIndexPath. Dieser zeigt von der
	 * Schemawurzel aus auf genau das Schemaobjekt, welches zu dem Tupelobjekt
	 * gehört, welches mit diesem TupelIndexPath erreicht wird.
	 * 
	 * @return Korrespondierender SchemaIndexPath
	 */
	public SchemaIndexPath toSchemaIndexPath() {
		return schemaIndexPath;
	}

	/**
	 * Liefert die Länge des Pfades. D.h. die Anzahl der Indizes innerhalb des
	 * Pfades.
	 * 
	 * @return Länge des Pfades
	 */
	public int getLength() {
		return indices.size();
	}

	/**
	 * Gibt zurück, ob mindestens ein Tupel innerhalb des Pfades vom Datentyp
	 * "List" ist. true, wenn es so ist, ansonsten false. Um herauszufinden,
	 * welche Indizes es genau sind, muss das korrespondierende SchemaIndexPath
	 * genutzt werden.
	 * 
	 * @return true, wenn ein Tupel innerhalb des Pfades von Datentyp "List"
	 *         ist, sonst false.
	 */
	public boolean isInList() {
		return schemaIndexPath.hasList();
	}

	/**
	 * Liefert das korrespondierende Schemaattribut des Tupelobjektes, worauf
	 * dieser Pfad zeigt, zurück.
	 * 
	 * @return Korrespondierendes Schemaattribut
	 */
	public SDFAttribute getAttribute() {
		return schemaIndexPath.getAttribute();
	}

	/**
	 * Liefert das Tupelobjekt, worauf dieser Pfad zeigt.
	 * 
	 * @return Tupelobjekt, worauf dieser Pfad zeigt.
	 */
	public Object getTupleObject() {
		return indices.get(indices.size() - 1).getValue();
	}

	/**
	 * Setzt das Tupelattribut, worauf dieser Pfad zeigt, auf den gegebenen
	 * Wert. Eine Typ- und Werteprüfung findet nicht statt.
	 * 
	 * @param obj
	 *            Neuer Wert
	 */
	public void setTupleObject(Object obj) {
		indices.get(indices.size() - 1).setValue(obj);
	}

	/**
	 * Liefert den Pfad als int-Array zurück. Darf nicht ver�ndert werden!
	 * 
	 * @return Int-Array
	 */
	public int[] toArray() {
		int[] indexArray = new int[this.indices.size()];
		for (int i = 0; i < this.indices.size(); i++) {
			indexArray[i] = this.indices.get(i).toInt();
		}
		return indexArray;
	}
	
	/**
	 * Setzt die Werte im TupleIndexPath auf Werte vom gegebenen
	 * Tupel.
	 * 
	 * @param Tuple
	 */
	public void updateValues( Tuple<?> tuple ) {
		List<TupleIndex> list = new ArrayList<TupleIndex>();
		Object parent = tuple;
		for (int i = 0; i < indices.size(); i++) {
			TupleIndex idx = new TupleIndex(parent, indices.get(i).toInt(), indices.get(i).getAttribute());
			list.add(idx);

			if (parent instanceof Tuple)
				parent = ((Tuple<?>) parent).getAttribute(indices.get(i).toInt());
			else if( parent instanceof List ) 
				parent = ((List<?>)parent).get(indices.get(i).toInt());
				
		}
		
		indices = list;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0; i < indices.size(); i++) {
			sb.append(indices.get(i));
			if (i < indices.size() - 1)
				sb.append(", ");
		}
		sb.append("}");

		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof SchemaIndexPath))
			return false;

		TupleIndexPath idx = (TupleIndexPath) obj;

		return idx.indices.equals(this.indices) && idx.schemaIndexPath.equals(this.schemaIndexPath);
	}

	/**
	 * Liefert eine tiefe Kopie des aktuellen TupleIndexPaths. Das referenzierte
	 * SchemaIndexPath wird nicht geklont.
	 */
	@Override
	public TupleIndexPath clone() {
		return new TupleIndexPath(this);
	}

	/* ITERATOR */

	private boolean done = false;

	// Verändert den TuplePath!
	void nextStep() {
		if (listIndices.size() > 0)
			nextStep(listIndices.size() - 1);
	}

	// Verändert den TuplePath!
	void nextStep(int listNumber) {
		int listIndex = listIndices.get(listNumber);

		Object obj = indices.get(listIndex).getValue();
//		MVTuple<?> obj = (MVTuple<?>) indices.get(listIndex).getValue();
		int index = indices.get(listIndex + 1).getValueIndex();
		int maxIndex = 0;
		if( obj instanceof Tuple )
			maxIndex = ((Tuple<?>)obj).size();
		else
			maxIndex = ((List<?>)obj).size();
		index++;

		if (index >= maxIndex)
			done = true;
		else {
			TupleIndex oldTupleIndex = indices.get(listIndex + 1);
			indices.set(listIndex + 1, new TupleIndex(oldTupleIndex.getParent(), index, oldTupleIndex.getAttribute()));
		}
	}

	@Override
	// Verändert den TuplePath
	public boolean hasNext() {
		if (done)
			return false;
		if (listIndices.size() == 0) {
			// Den aktuellen Tupel ausgeben und fertig mit der Iteration
			done = true;
			return true;
		}
		int lastListIndex = listIndices.get(listIndices.size() - 1);

		Object obj = indices.get(lastListIndex).getValue();
		if( obj instanceof Tuple) {
			Tuple<?> tuple = (Tuple<?>)obj;
			
			if( tuple.size() == 0) {
				return false;
			}
			if (lastListIndex == indices.size() - 1) {
				// seems that there must be subattributes
				SDFAttribute lastAttribute = schemaIndexPath.getLastSchemaIndex().getAttribute().getDatatype().getSchema().getAttribute(0);
				indices.add(new TupleIndex(tuple, 0, lastAttribute));
				List<SchemaIndex> idx = schemaIndexPath.getSchemaIndices();
				List<SchemaIndex> newIdx = new ArrayList<SchemaIndex>();
				newIdx.addAll(idx);
				newIdx.add(new SchemaIndex(0, lastAttribute));
				schemaIndexPath = new SchemaIndexPath(newIdx, lastAttribute);
			}

			int actIndex = indices.get(lastListIndex + 1).getValueIndex();
			int maxIndex = tuple.size();

			return actIndex < maxIndex;
			
		} else if( obj instanceof List ) {
			List<?> tuple = (List<?>)obj;
			
			if( tuple.size() == 0) {
				return false;
			}
			if (lastListIndex == indices.size() - 1) {
				// seems that there must be subattributes
				SDFAttribute lastAttribute = schemaIndexPath.getLastSchemaIndex().getAttribute().getDatatype().getSchema().getAttribute(0);
				indices.add(new TupleIndex(obj, 0, lastAttribute));
				List<SchemaIndex> idx = schemaIndexPath.getSchemaIndices();
				List<SchemaIndex> newIdx = new ArrayList<SchemaIndex>();
				newIdx.addAll(idx);
				newIdx.add(new SchemaIndex(0, lastAttribute));
				schemaIndexPath = new SchemaIndexPath(newIdx, lastAttribute);
			}

			int actIndex = indices.get(lastListIndex + 1).getValueIndex();
			int maxIndex = tuple.size();

			return actIndex < maxIndex;
		} else
			return false;
	}

	@Override
	// Verändert den TuplePath
	public TupleInfo next() {
		TupleInfo info = new TupleInfo();
		info.attribute = schemaIndexPath.getAttribute();
		info.isInList = schemaIndexPath.hasList();
		info.level = schemaIndexPath.getLength();
		info.schemaIndexPath = schemaIndexPath;
	    info.tupleIndexPath = this.clone();
		info.tupleObject = getTupleObject();
		info.isTuple = (info.tupleObject != null ? (info.tupleObject instanceof Tuple) : false);
		nextStep();
		return info;
	}

	@Override
	public void remove() {
	}

	@Override
	public Iterator<TupleInfo> iterator() {
		return new TupleIndexPath(this);
	}
	
	/**
	 * Liefert den korrespondierenden TupelIndexPfad zurück. Listen erhalten den
	 * Index 0.
	 * 
	 * @param tuple
	 *            Tupel, worauf sich der TupleIndexPfad beziehen soll. Darf
	 *            nicht <code>null</code> sein.
	 * @return TupelIndexPfad
	 */
	public static TupleIndexPath fromSchemaIndexPath(SchemaIndexPath sip, Tuple<?> tuple) {
		List<TupleIndex> list = new ArrayList<TupleIndex>();
		Object parent = tuple;
		for (int i = 0; i < sip.indices.size(); i++) {
			TupleIndex idx = new TupleIndex(parent, sip.indices.get(i).toInt(), sip.indices.get(i).getAttribute());
			list.add(idx);

			if (parent instanceof Tuple)
				parent = ((Tuple<?>) parent).getAttribute(sip.indices.get(i).toInt());
			else if (parent instanceof List)
				parent = ((List<?>) parent).get(sip.indices.get(i).toInt());
		}
		return new TupleIndexPath(list, sip);
	}
}
