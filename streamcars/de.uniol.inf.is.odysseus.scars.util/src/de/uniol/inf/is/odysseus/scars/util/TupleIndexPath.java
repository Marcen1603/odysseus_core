package de.uniol.inf.is.odysseus.scars.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * Repräsentiert einen Pfad innerhalb eines Tupels.
 * Dieser wurde so implementiert, dass sich dieser nach der Instanziierung
 * nicht ändern kann. 
 * 
 * Nutzer können nicht selbständig neue Instanzen erzeugen. Dies geschieht über
 * TupleIterator oder SchemaIndexPath. Dort werden sie verwendet.
 * 
 * @author Timo Michelsen
 *
 */
public class TupleIndexPath {

	private List<TupleIndex> indices;
	private SchemaIndexPath schemaIndexPath;
	
	// Interner Konstruktor
	TupleIndexPath( List<TupleIndex> indices, SchemaIndexPath schemaIndexPath ) {
		this.indices = indices;
		this.schemaIndexPath = schemaIndexPath;
	}
	
	TupleIndexPath( TupleIndexPath other ) {
		this.indices = new ArrayList<TupleIndex>();
		for( TupleIndex idx : other.indices ) {
			indices.add( idx.clone() );
		}
		this.schemaIndexPath = other.schemaIndexPath;
	}
	
	/**
	 * Liefert die Indices des Pfades als Liste von TupleIndex zurück.
	 * Damit können genauere Informationen zu jedem Pfadschritt abgerufen werden.
	 * Die Reihenfolge der Indizes geht von der Wurzel bis zum betreffenden Knoten.
	 * 
	 * @return Liste von TupleIndex
	 */
	public List<TupleIndex> getIndices() {
		return Collections.unmodifiableList(indices);
	}
	
	/**
	 * Liefert den korrespondierenden SchemaIndexPath. Dieser zeigt von der Schemawurzel aus
	 * auf genau das Schemaobjekt, welches zu dem Tupelobjekt gehört, welches mit diesem
	 * TupelIndexPath erreicht wird.
	 * 
	 * @return Korrespondierender SchemaIndexPath
	 */
	public SchemaIndexPath toSchemaIndexPath() {
		return schemaIndexPath;
	}
	
	/**
	 * Liefert die Länge des Pfades. D.h. die Anzahl der Indizes innerhalb des Pfades.
	 * 
	 * @return Länge des Pfades
	 */
	public int getLength() {
		return indices.size();
	}
	
	/**
	 * Gibt zurück, ob mindestens ein Tupel innerhalb des Pfades vom Datentyp "List" ist. true, wenn es
	 * so ist, ansonsten false. Um herauszufinden, welche Indizes es genau sind, muss das korrespondierende SchemaIndexPath genutzt werden.
	 * 
	 * @return true, wenn ein Tupel innerhalb des Pfades von Datentyp "List" ist, sonst false.
	 */
	public boolean isInList() {
		return schemaIndexPath.hasList();
	}
	
	/**
	 * Liefert das korrespondierende Schemaattribut des Tupelobjektes, worauf dieser Pfad zeigt, zurück.
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
	 * Setzt das Tupelattribut, worauf dieser Pfad zeigt, auf den
	 * gegebenen Wert. Eine Typ- und Werteprüfung findet nicht statt.
	 * 
	 * @param obj Neuer Wert
	 */
	public void setTupleObject( Object obj ) {
		indices.get(indices.size() - 1).setValue(obj);
	}
	
	/**
	 * Liefert den Pfad als int-Array zurück.
	 * 
	 * @return Int-Array
	 */
	public int[] toArray() {
		int[] array = new int[indices.size()];
		for( int i = 0; i < indices.size(); i++ ) 
			array[i] = indices.get(i).toInt();
		return array;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for( int i = 0; i < indices.size(); i++ ) {
			sb.append(indices.get(i));
			if( i < indices.size() - 1 ) 
				sb.append(", ");
		}
		sb.append("}");
			
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj == null ) return false;
		if( obj == this ) return true;
		if( !(obj instanceof SchemaIndexPath )) return false;
		
		TupleIndexPath idx = (TupleIndexPath)obj;
		
		return idx.indices.equals(this.indices) && idx.schemaIndexPath.equals(this.schemaIndexPath);
	}
	
	/**
	 * Liefert eine tiefe Kopie des aktuellen TupleIndexPaths.
	 * Das referenzierte SchemaIndexPath wird nicht geklont.
	 */
	public TupleIndexPath clone() {
		return new TupleIndexPath( this );
	}
}
