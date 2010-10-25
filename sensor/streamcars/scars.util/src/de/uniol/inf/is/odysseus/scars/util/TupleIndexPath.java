package de.uniol.inf.is.odysseus.scars.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

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
	private int[] indicesArray;

	public static TupleIndexPath fromIntArray(int[] array, MVRelationalTuple<?> tuple, SchemaIndexPath path) {
		
		List<TupleIndex> indices = new ArrayList<TupleIndex>();
		
		Object parent = tuple;
		for (int i = 0; i < path.getSchemaIndices().size(); i++) {
			
			TupleIndex index = new TupleIndex((MVRelationalTuple<?>) parent, array[i], path.getSchemaIndex(i).getAttribute());
			indices.add(index);
			if (parent instanceof MVRelationalTuple)
				parent = ((MVRelationalTuple<?>) parent).getAttribute(path.getSchemaIndex(i).toInt());
			else
				throw new RuntimeException("Corrupted SchemaIndexPath: " + path);
		}		
		
		return new TupleIndexPath(indices, path); 
	}
	
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
		this.indicesArray = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++)
			this.indicesArray[i] = indices.get(i).toInt();
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
		this.indicesArray = new int[other.indicesArray.length];
		for (int i = 0; i < this.indicesArray.length; i++)
			this.indicesArray[i] = other.indicesArray[i];
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
	public int[] toArray(boolean getCopy) {
		if(getCopy){
			int[] copy = new int[this.indicesArray.length];
			System.arraycopy(this.indicesArray, 0, copy, 0, this.indicesArray.length);
			return copy;
		}
		else{
			return this.indicesArray;
		}
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

		MVRelationalTuple<?> obj = (MVRelationalTuple<?>) indices.get(listIndex).getValue();
		int index = indices.get(listIndex + 1).getValueIndex();
		int maxIndex = obj.getAttributeCount();
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

		MVRelationalTuple<?> obj = (MVRelationalTuple<?>) indices.get(lastListIndex).getValue();
		if( obj.getAttributeCount() == 0) {
			return false;
		}
		if (lastListIndex == indices.size() - 1) {
			SDFAttribute lastAttribute = schemaIndexPath.getLastSchemaIndex().getAttribute().getSubattribute(0);
			indices.add(new TupleIndex(obj, 0, lastAttribute));
			List<SchemaIndex> idx = schemaIndexPath.getSchemaIndices();
			List<SchemaIndex> newIdx = new ArrayList<SchemaIndex>();
			newIdx.addAll(idx);
			newIdx.add(new SchemaIndex(0, lastAttribute));
			schemaIndexPath = new SchemaIndexPath(newIdx, lastAttribute);
		}

		int actIndex = indices.get(lastListIndex + 1).getValueIndex();
		int maxIndex = obj.getAttributeCount();

		return actIndex < maxIndex;
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
		info.isTuple = (info.tupleObject != null ? (info.tupleObject instanceof MVRelationalTuple) : false);
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
}
