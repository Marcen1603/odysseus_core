package de.uniol.inf.is.odysseus.relational.base;

import java.io.Serializable;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;


/**
 * Klasse repraesentiert ein Tupel im relationalen Modell und dient als
 * Austauschobjekt der relationalen Planoperatoren Kann mit Hilfe einer Oracle
 * Loader Zeile initialisiert werden
 * 
 * @author Marco Grawunder, Jonas Jacobi
 */
public class RelationalTuple<T extends IMetaAttribute> extends MetaAttributeContainer<T>
		implements Serializable, Comparable<RelationalTuple<T>> {

	private static final long serialVersionUID = 7119095568322125441L;

	protected Object[] attributes;
	protected int memSize = -1;

	// -----------------------------------------------------------------
	// static Hilfsmethoden
	// -----------------------------------------------------------------
	@Override
	public RelationalTuple<T> clone() {
		RelationalTuple<T> t = new RelationalTuple<T>(this);
		return t;
	}

	// -----------------------------------------------------------------
	// Attributzugriffsmethoden
	// -----------------------------------------------------------------

	@SuppressWarnings("unchecked")
	public final <K> K getAttribute(int pos) {
		return (K) this.attributes[pos];
	}

	@SuppressWarnings("unchecked")
	public final void addAttributeValue(int pos, Object value){		
		if(this.attributes[pos] != null && !(this.attributes[pos] instanceof Collection)){
			throw new RuntimeException("Cannot add value to non collection type");
		}
		if(this.attributes[pos] == null){
			this.attributes[pos] = new ArrayList<Object>();
		}
		((Collection)this.attributes[pos]).add(value);
	}
	
	public final void setAttribute(int pos, Object value) {
//		if( this.attributes[pos] != null ) {
//			if( !this.attributes[pos].getClass().equals(value.getClass()))
//				throw new IllegalArgumentException("Typ eines Attributes änderte sich! Von " + 
//						this.attributes[pos].getClass().getName() + " zu " + value.getClass().getName() );
//		}
		this.attributes[pos] = value;
		//calcSize();
	}
	
	public final void setAttribute(int pos, CharBuffer value){
		String stringValue = new String(value.array(), 0, value.position());
		setAttribute(pos, stringValue);	
	}
	
	public final int getAttributeCount() {
		return this.attributes.length;
	}

	// Erzeugen von neuen Objekten, basierend auf dem aktuellen

	/**
	 * erzeugen eines neuen Objektes, in dem nur die Attribute betrachtet
	 * werden, die in der attrList uebergeben werden, die Reihenfolge des neuen
	 * Objektes wird durch die Reihenfolge der Attribute im Array festgelegt
	 * Beispiel: attrList[1]=14,attrList[2]=12 erzeugt ein neues Objekt, welches
	 * die Attribute 14 und 12 enthaelt.
	 * 
	 * Falls das aktuelle RelationalTuple keine Schemainformationen enthaelt,
	 * wird das neue auch ohne erzeugt. Ansonsten enthaelt das neue Tuple auch
	 * die passenden Schemainformationen.
	 * 
	 * @param attrList
	 *            erzeugt ein neues Objekt das die Attribute der Positionen aus
	 *            attrList enth�lt
	 * @param createNew if true, a new tuple will be created, if false the original
	 * one will be modified
	 */
	public RelationalTuple<T> restrict(int[] attrList, boolean createNew) {
		Object[] newAttrs = new Object[attrList.length];
		
		for (int i = 0; i < attrList.length; i++) {
			newAttrs[i] = this.attributes[attrList[i]];
		}
		return restrictCreation(createNew, newAttrs);
	}
	
	public RelationalTuple<T> restrict(int attr, boolean createNew) {
		Object[] newAttrs = new Object[1];
		newAttrs[0] = this.attributes[attr];
		return restrictCreation(createNew, newAttrs);
	}

	@SuppressWarnings("unchecked")
	private RelationalTuple<T> restrictCreation(boolean createNew,
			Object[] newAttrs) {
		if(createNew){
			RelationalTuple<T> newTuple = new RelationalTuple<T>(newAttrs.length);
			newTuple.setAttributes(newAttrs);
			newTuple.setMetadata((T)this.getMetadata().clone());
			return newTuple;
		}
		else{
			this.attributes = newAttrs;
			return this;
		}
	}
	
	
	/**
	 * Sets the attribute values of this tuple to the specified
	 * values in attributes.
	 * @param attributes The new attribute values for this tuple.
	 */
	public void setAttributes(Object[] attributes){
		this.attributes = attributes;
	}

	
	// -----------------------------------------------------------------
	// Vergleichsmethoden
	// -----------------------------------------------------------------
	@Override
	@SuppressWarnings("unchecked")
	public final boolean equals(Object o) {
		return this.compareTo((RelationalTuple) o) == 0;
	}

	/**
	 * Liefert 0 wenn die beiden Attributlisten gleich sind ansonsten das erste
	 * Element an denen sich die Attributlisten unterscheiden. Die
	 * Sortierreihenfolge ist implizit durch die Position in der Liste gegeben
	 * wenn das aktuelle Objekt kleiner ist ist der R�ckgabewert negativ
	 * ansonsten positiv Es wird maximal die kleinere Anzahl der Felder
	 * verglichen
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final int compareTo(RelationalTuple c) {
		int min = c.getAttributeCount();
		if (min > this.attributes.length) {
			min = this.attributes.length;
		}
		int compare = 0;
		int i = 0;
		for (i = 0; i < min && compare == 0; i++) {
			try {
				compare = ((Comparable) this.attributes[i]).compareTo(c
						.getAttribute(i));
			} catch (NullPointerException e) {
				System.out.println("Exception: " + this + " " + c + " " + i);
				System.out.println("this " + this);
				System.out.println("c " + c);
				System.out.println("this.attributes[i] " + this.attributes[i]);
				System.out.println("c.getAttribute(i) " + c.getAttribute(i));
				throw new NullPointerException();
			}
		}
		if (compare < 0) {
			compare = (-1) * i;
		}
		if (compare > 0) {
			compare = i;
		}
		return compare;
	}

	// -----------------------------------------------------------------
	// Ausgabe
	// -----------------------------------------------------------------

	@Override
	public final String toString() {
		StringBuffer retBuff = new StringBuffer();
		if (attributes.length > 0){
			retBuff.append(this.attributes[0] == null ? "" : this.attributes[0]);
		}else{
			retBuff.append("null");
		}
		for (int i = 1; i < this.attributes.length; ++i) {
			Object curAttribute = this.attributes[i];
			retBuff.append("|");
			retBuff.append(curAttribute == null ? "" : curAttribute.toString());
		}
		retBuff.append(" | sz="+(memSize==-1?"(-)":memSize));
		retBuff.append(" | META | " + getMetadata());
		return retBuff.toString();
	}
	
	public int memSize(boolean calcNew){
		if (memSize == -1 || calcNew){
			memSize = calcSize();
		}
		return memSize;
	}
	
	private int calcBaseTypeSize(Object attObject){
		if (attObject == null) return 0;
		if (attObject instanceof Integer) return Integer.SIZE/8;
		if (attObject instanceof Double) return Double.SIZE/8;
		if (attObject instanceof Long) return Long.SIZE/8;
		if (attObject instanceof String) return ((String)attObject).length()*2 // Unicode!
												+Integer.SIZE/8; // F�r die L�ngeninformation (evtl. anders machen?)
		if (attObject instanceof RelationalTuple<?>) return ((RelationalTuple<?>)attObject).memSize(true);
		
		throw new IllegalArgumentException("Illegal Relation Attribute Type "+attObject);		
		
	}
	
	private int calcSize(){
		int size = 0;
		for (Object attObject: attributes){
			size = size + calcSize(attObject);		
		}
		return size;
	}
	
	@SuppressWarnings("unchecked")
	private int calcSize(Object attObject){
		int size = 0;
		if (attObject instanceof Collection){
			for (Object e: ((Collection)attObject)){
				size += calcSize(e);
			}
			
		}else{
			size += calcBaseTypeSize(attObject);
		}
		return size;
	}

	// -----------------------------------------------------------------
	// Konstruktoren
	// -----------------------------------------------------------------
	
	/**
	 * Allows subclasses to call the implicit super constructor. To not allow
	 * other classes to use the constructor it is protected.
	 */
	protected RelationalTuple(){
	}
	
	/**
	 * Erzeugt ein neues Object, anhand der Zeile und des Trennzeichens
	 * 
	 * @param line
	 *            enthaelt die konkatenierten Attribute
	 * @param delimiter
	 *            enthaelt das Trennzeichen
	 * @param noOfAttribs
	 *            enthaelt die Anzahl der Attribute (Effizienzgr�nde)
	 */
//	public RelationalTuple(SDFAttributeList schema, String line, char delimiter) {
////		this.schema = schema;
//		this.attributes = splittLineToAttributes(line, delimiter, schema);
//	}

	/**
	 * Erzeugt ein neues leeres Object, zur Erzeugung von Zwischenergebnissen
	 * 
	 * @param attributeCount
	 *            enthaelt die Anzahl der Attribute die das Objekt speichern
	 *            koennen soll
	 */
//	public RelationalTuple(SDFAttributeList schema) {
//		if (schema.size() == 0) {
//			throw new IllegalArgumentException("attribute count has to be > 0");
//		}
//
//		this.schema = schema;
//		this.attributes = new Object[schema.size()];
//		//calcSize();
//	}

	/**
	 * Erzeugt ein leeres Tuple ohne Schemainformationen
	 * 
	 * @param attributeCount
	 *            Anzahl der Attribute des Tuples
	 */
	public RelationalTuple(int attributeCount) {
		this.attributes = new Object[attributeCount];
	}

	public RelationalTuple(RelationalTuple<T> copy) {
		super(copy);
		int attributeLength = copy.attributes.length;
		this.attributes = new Object[attributeLength];
		System.arraycopy(copy.attributes, 0, this.attributes, 0,
				attributeLength);
	}

	

	/**
	 * Erzeugt ein neues Tuple mit Attributen und ohne Schemainformationen
	 * 
	 * @param attributes
	 *            Attributbelegung des neuen Tuples
	 */
	public RelationalTuple(Object[] attributes) {
	    this.attributes = (Object[])attributes.clone();
		//calcSize();
	}

	@Override
	public final int hashCode() {
		int ret = 0;
		for (Object o : this.attributes) {
			ret += o.hashCode();
		}
		return ret;
	}


	public Object[] getAttributes() {
		return attributes;
	}

}