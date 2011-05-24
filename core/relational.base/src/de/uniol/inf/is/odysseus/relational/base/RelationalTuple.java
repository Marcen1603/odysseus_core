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
package de.uniol.inf.is.odysseus.relational.base;

import java.io.Serializable;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import de.uniol.inf.is.odysseus.ICSVToString;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.util.Primes;

/**
 * Klasse repraesentiert ein Tupel im relationalen Modell und dient als
 * Austauschobjekt der relationalen Planoperatoren Kann mit Hilfe einer Oracle
 * Loader Zeile initialisiert werden
 * 
 * @author Marco Grawunder, Jonas Jacobi
 */
public class RelationalTuple<T extends IMetaAttribute> extends MetaAttributeContainer<T> implements Serializable, Comparable<RelationalTuple<?>>, ICSVToString {

	private static final long serialVersionUID = 7119095568322125441L;

	protected Object[] attributes;
	protected int memSize = -1;
	
	private boolean containsNull = false;
	private boolean valueChanged = true;

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
		if (pos < 0) return null;
		return (K) this.attributes[pos];
	}

	@SuppressWarnings("unchecked")
	public final void addAttributeValue(int pos, Object value) {
		if (this.attributes[pos] != null && !(this.attributes[pos] instanceof Collection)) {
			throw new RuntimeException("Cannot add value to non collection type");
		}
		if (this.attributes[pos] == null) {
			this.attributes[pos] = new ArrayList<Object>();
		}
		((Collection<Object>) this.attributes[pos]).add(value);
		this.valueChanged = true;
	}

	public final void setAttribute(int pos, Object value) {
		// if( this.attributes[pos] != null ) {
		// if( !this.attributes[pos].getClass().equals(value.getClass()))
		// throw new
		// IllegalArgumentException("Typ eines Attributes änderte sich! Von " +
		// this.attributes[pos].getClass().getName() + " zu " +
		// value.getClass().getName() );
		// }
		this.attributes[pos] = value;
		this.valueChanged = true;
		// calcSize();
	}

	public final void setAttribute(int pos, CharBuffer value) {
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
	 * @param createNew
	 *            if true, a new tuple will be created, if false the original
	 *            one will be modified
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
	
	/**
	 * Creates a new instance from the current tuple and 
	 * appends the given attribute object to it
	 * @param object the object to append
	 * @return a new created and extended copy
	 */
	public RelationalTuple<T> append(Object object){
		return this.append(object, true);
	}
	
	/**
	 * Creates a new instance from the current tuple if the 
	 * createNew param is true or uses the current instance 
	 * and appends the given attribute object
	 * @param object the object to append
	 * @param createNew indicates if create a copy
	 * @return the extended tuple
	 */
	@SuppressWarnings("unchecked")
	public RelationalTuple<T> append(Object object, boolean createNew){
		Object[] newAttrs = Arrays.copyOf(this.attributes, this.attributes.length+1);		
		newAttrs[this.attributes.length] = object;
		
		if(createNew){
			RelationalTuple<T> newTuple = new RelationalTuple<T>(newAttrs.length);
			newTuple.setAttributes(newAttrs);			
			newTuple.setMetadata((T) this.getMetadata().clone());			
			return newTuple;			
		}
		else
		{
			this.attributes = newAttrs;
			return this;
		}
	}
	
	@SuppressWarnings("unchecked")
	private RelationalTuple<T> restrictCreation(boolean createNew, Object[] newAttrs) {
		if (createNew) {
			RelationalTuple<T> newTuple = new RelationalTuple<T>(newAttrs.length);
			newTuple.setAttributes(newAttrs);
			newTuple.setMetadata((T) this.getMetadata().clone());
			return newTuple;
		} else {
			this.attributes = newAttrs;
			this.valueChanged = true;
			return this;
		}
	}

	/**
	 * Sets the attribute values of this tuple to the specified values in
	 * attributes.
	 * 
	 * @param attributes
	 *            The new attribute values for this tuple.
	 */
	public void setAttributes(Object[] attributes) {
		this.attributes = attributes;
		this.valueChanged = true;
	}

	// -----------------------------------------------------------------
	// Vergleichsmethoden
	// -----------------------------------------------------------------
	@Override
	public final boolean equals(Object o) {
		if (o instanceof RelationalTuple) {
			return this.compareTo((RelationalTuple<?>) o) == 0;
		} else {
			return false;
		}
	}

	/**
	 * Liefert 0 wenn die beiden Attributlisten gleich sind ansonsten das erste
	 * Element an denen sich die Attributlisten unterscheiden. Die
	 * Sortierreihenfolge ist implizit durch die Position in der Liste gegeben
	 * wenn das aktuelle Objekt kleiner ist ist der R�ckgabewert negativ
	 * ansonsten positiv Es wird maximal die kleinere Anzahl der Felder
	 * verglichen. Tupel mit NULL-Werten sind ungleich.
	 */	
	@Override
	public final int compareTo(RelationalTuple<?> c) {
		int min = c.getAttributeCount();
		if (min > this.attributes.length) {
			min = this.attributes.length;
		}
		int compare = 0;
		int i = 0;
		for (i = 0; i < min && compare == 0; i++) {
//			try {
			if(this.attributes[i] == null || c.getAttribute(i) == null){
				compare = i;
			}
			else if (this.attributes[i] instanceof Comparable<?>) {
				compare = ((Comparable<Object>) this.attributes[i]).compareTo(c.getAttribute(i));
			}
//			} catch (NullPointerException e) {
//				System.out.println("Exception: " + this + " " + c + " " + i);
//				System.out.println("this " + this);
//				System.out.println("c " + c);
//				System.out.println("this.attributes[i] " + this.attributes[i]);
//				System.out.println("c.getAttribute(i) " + c.getAttribute(i));
//				throw new NullPointerException();
//			}
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
		if (attributes.length > 0) {
			retBuff.append(this.attributes[0] == null ? "" : this.attributes[0]);
		} else {
			retBuff.append("null");
		}
		for (int i = 1; i < this.attributes.length; ++i) {
			Object curAttribute = this.attributes[i];
			retBuff.append("|");
			retBuff.append(curAttribute == null ? "" : curAttribute.toString());
		}
		retBuff.append(" | sz=" + (memSize == -1 ? "(-)" : memSize));
		retBuff.append(" | META | " + getMetadata());
		return retBuff.toString();
	}
	
	@Override
	public final String csvToString() {
		return this.csvToString(true);
	}
	
	public final String csvToString(boolean withMetadata){
		StringBuffer retBuff = new StringBuffer();
		if (attributes.length > 0) {
			retBuff.append(this.attributes[0] == null ? "" : this.attributes[0]);
		} else {
			retBuff.append("null");
		}
		for (int i = 1; i < this.attributes.length; ++i) {
			Object curAttribute = this.attributes[i];
			retBuff.append(";");
			retBuff.append(curAttribute == null ? "" : curAttribute.toString());
		}
		if(withMetadata){
			retBuff.append(";").append(getMetadata().csvToString());
		}
		return retBuff.toString();
	}
	
	@Override
	public String getCSVHeader() {
		StringBuffer ret = new StringBuffer();
		for (int i=0;i<attributes.length;i++){
			ret.append(";");
		}
		ret.append(getMetadata().getCSVHeader());
		return ret.toString();
	}

	public int memSize(boolean calcNew) {
		if (this.valueChanged|| calcNew) {
			memSize = calcSize();
		}
		return memSize;
	}

	private int calcBaseTypeSize(Object attObject) {
		if (attObject == null)
			return 0;
		if (attObject instanceof Integer)
			return Integer.SIZE / 8;
		if (attObject instanceof Double)
			return Double.SIZE / 8;
		if (attObject instanceof Long)
			return Long.SIZE / 8;
		if (attObject instanceof String)
			return ((String) attObject).length() * 2 // Unicode!
					+ Integer.SIZE / 8; // F�r die L�ngeninformation (evtl.
										// anders machen?)
		if (attObject instanceof RelationalTuple<?>)
			return ((RelationalTuple<?>) attObject).memSize(true);

		throw new IllegalArgumentException("Illegal Relation Attribute Type " + attObject);

	}

	private int calcSize() {
		int size = 0;
		for (Object attObject : attributes) {
			size = size + calcSize(attObject);
		}
		return size;
	}

	private int calcSize(Object attObject) {
		int size = 0;
		if (attObject instanceof Collection) {
			for (Object e : ((Collection<?>) attObject)) {
				size += calcSize(e);
			}

		} else {
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
	protected RelationalTuple() {
	}

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
		this.valueChanged = copy.valueChanged;
		this.containsNull = copy.containsNull;
		System.arraycopy(copy.attributes, 0, this.attributes, 0, attributeLength);
	}

	/**
	 * Erzeugt ein neues Tuple mit Attributen und ohne Schemainformationen
	 * 
	 * @param attributes
	 *            Attributbelegung des neuen Tuples
	 */
	public RelationalTuple(Object[] attributes) {
		this.attributes = (Object[]) attributes.clone();
		// calcSize();
	}

	@Override
	public final int hashCode() {
		int ret = 0;
		for (int i = 0; i<this.attributes.length; i++) {
			Object o = this.attributes[i];
			if(o != null){
				ret += o.hashCode() * Primes.PRIMES[i%Primes.size()];
			}
		}
		return ret;
	}

	public Object[] getAttributes() {
		return attributes;
	}

	public boolean containsNull(){
		if(this.valueChanged){
			for(Object o: this.attributes){
				if(o == null){
					this.containsNull = true;
					break;
				}
			}
		}
		return this.containsNull;
	}
}