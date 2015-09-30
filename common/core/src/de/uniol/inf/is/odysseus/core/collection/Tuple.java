/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.collection;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.ICSVToString;
import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * Klasse repraesentiert ein Tupel im relationalen Modell und dient als
 * Austauschobjekt der relationalen Planoperatoren Kann mit Hilfe einer Oracle
 * Loader Zeile initialisiert werden
 * 
 * @author Marco Grawunder, Jonas Jacobi
 */
public class Tuple<T extends IMetaAttribute> extends AbstractStreamObject<T>
		implements Serializable, Comparable<Tuple<?>>, ICSVToString {

	private static final long serialVersionUID = 7119095568322125441L;

    private static final Logger LOG = LoggerFactory
			.getLogger(Tuple.class);
	
	protected Object[] attributes;

	private boolean containsNull = false;
	private boolean valueChanged = true;

	private boolean requiresDeepClone;
   /// private Cloner cloner;

    private static List<Class<?>> immutables = new ArrayList<>();
    static {
        immutables.add(String.class);
        immutables.add(Integer.class);
        immutables.add(Long.class);
        immutables.add(Boolean.class);
        immutables.add(Float.class);
        immutables.add(Double.class);
        immutables.add(Character.class);
        immutables.add(Byte.class);
        immutables.add(Short.class);
    }
    // private Cloner cloner;

	/**
	 * Allows subclasses to call the implicit super constructor. To not allow
	 * other classes to use the constructor it is protected.
	 */
	protected Tuple() {
		requiresDeepClone = false;
	///	cloner = null;
	}

	/**
	 * Erzeugt ein leeres Tuple ohne Schemainformationen
	 * 
	 * @param attributeCount
	 *            Anzahl der Attribute des Tuples
	 * @param requiresDeepClone
	 *            if true, each copy of this tuple will call clone on each
	 *            attribute
	 */
	public Tuple(int attributeCount, boolean requiresDeepClone) {
		this.attributes = new Object[attributeCount];
		this.requiresDeepClone = requiresDeepClone;
		//if (this.requiresDeepClone) {
			//this.cloner = new Cloner();
		//} else {
			//this.cloner = null;
		//}
	}

	public Tuple(Tuple<T> copy) {
		this(copy, null, copy.requiresDeepClone);
	}

	/**
	 * Creates a new Tuple without copying the attributes from the other tuple
	 * but using the given Attributes
	 * 
	 * @param copy
	 * @param newAttributes
	 * @param requiresDeepClone
	 */
	protected Tuple(Tuple<T> copy, Object[] newAttributes,
			boolean requiresDeepClone) {
		super(copy);
		this.requiresDeepClone = requiresDeepClone;
		//if (this.requiresDeepClone) {
			//this.cloner = new Cloner();
		//} else {
			//this.cloner = null;
		//}
		if (newAttributes != null) {
			this.attributes = newAttributes;
		} else {
			if (!requiresDeepClone) {
				int attributeLength = copy.attributes.length;
				this.attributes = new Object[attributeLength];
				System.arraycopy(copy.attributes, 0, this.attributes, 0,
						attributeLength);
			} else {
				this.attributes = cloneAttributes(copy.attributes);
			}
		}
		this.valueChanged = copy.valueChanged;
		this.containsNull = copy.containsNull;
	}

	/**
	 * Creates a new Tuple with out attributes
	 * 
	 * @param copy
	 * @param requiresDeepClone
	 */
	protected Tuple(Tuple<T> copy, boolean requiresDeepClone) {
		super(copy);
		this.requiresDeepClone = requiresDeepClone;
		//if (this.requiresDeepClone) {
			//this.cloner = new Cloner();
		//} else {
			//this.cloner = null;
		//}
		this.valueChanged = copy.valueChanged;
		this.containsNull = copy.containsNull;
	}

	/**
	 * Erzeugt ein neues Tuple mit Attributen und ohne Schemainformationen
	 * 
	 * @param attributes
	 *            Attributbelegung des neuen Tuples
	 * @param requiresDeepClone
	 *            if true, each copy of this tuple will call clone on each
	 *            attribute
	 */
	public Tuple(Object[] attributes, boolean requiresDeepClone) {
		this.attributes = attributes.clone();
		this.requiresDeepClone = requiresDeepClone;
		// calcSize();
		//if (this.requiresDeepClone) {
			//this.cloner = new Cloner();
		//} else {
			//this.cloner = null;
		//}
	}

	/**
	 * Erzeugt ein neues Tuple mit einem Attribut
	 * 
	 * @param attribute
	 *            Attributbelegung des neuen Tuples
	 * @param requiresDeepClone
	 *            if true, each copy of this tuple will call clone on each
	 *            attribute
	 */
	public Tuple(Object attribute, boolean requiresDeepClone) {
		this.attributes = new Object[1];
		attributes[0] = attribute;
		this.requiresDeepClone = requiresDeepClone;
		// calcSize();
		//if (this.requiresDeepClone) {
			//this.cloner = new Cloner();
		//} else {
			//this.cloner = null;
		//}
	}

	// -----------------------------------------------------------------
	// static Hilfsmethoden
	// -----------------------------------------------------------------
	@Override
	public Tuple<T> clone() {
		Tuple<T> t = new Tuple<T>(this);
		return t;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractStreamObject<T> newInstance() {
        return new Tuple<>();
    }

    public static <T extends IMetaAttribute> Tuple<T> createEmptyTupleWithMeta(int size, IStreamObject<T> from, boolean requiresDeepClone){
		Tuple<T> outputVal = new Tuple<T>(size, requiresDeepClone);
		@SuppressWarnings("unchecked")
		T meta = (T) from.getMetadata().clone();
		outputVal.setMetadata(meta);
		if (from.getMetadataMap() != null) {
			for (Entry<String, Object> entry : from.getMetadataMap()
					.entrySet()) {
				outputVal.setMetadata(entry.getKey(), entry.getValue());
			}
		}
		return outputVal;
    }
    
    /**
     * Creates a clone of all attribute values.
     * Handles primitive types, matrix/vectors, list, and complex types.
     * Currently also supports unknown object by using Cloner lib to perform a
     * deep clone. This behavior will be removed in future versions and prints
     * a warning about not supporting IClone interface.
     * 
     * @param attr
     *            The attribute array
     * @return clone attribute array
     */
    private static Object[] cloneAttributes(Object[] attr) {
        Object[] clone = new Object[attr.length];
        for (int i = 0; i < clone.length; i++) {
            // null needs to clone
        	if (attr[i] == null){
            	continue;
            }
        	// TODO Optimize: Collect positions of primitive datatypes and
            // perform System arraycopy on them (CKu)
        	
            if (immutables.contains(attr[i].getClass())) {
                clone[i] = attr[i];
            }
            // Handle Matrix/Vector datatype.
            // Assumption: Matrix/Vector contains only immutable types
            else if (attr[i].getClass().isArray()) {
                int length = Array.getLength(attr[i]);
                clone[i] = Array.newInstance(attr[i].getClass().getComponentType(), length);
                System.arraycopy(attr[i], 0, clone[i], 0, length);
            }
            // Handle List datatype
            else if ((attr[i].getClass() == ArrayList.class)) {
                clone[i] = cloneList((List<?>) attr[i]);
            }
            // Handle the rest
            else {
                try {
                    clone[i] = ((IClone) attr[i]).clone();
                }
                catch (ClassCastException e) {
                    LOG.error(String.format("Instance of %s does not implement IClone interface", attr[i].getClass()));
//                    Cloner cloner = new Cloner();
//                    clone[i] = cloner.deepClone(attr[i]);
                }
            }
        }
        return clone;
    }

    /**
     * Creates a clone of a List attribute.
     * 
     * @param list
     *            The list
     * @return cloned list
     */
    private static List<?> cloneList(List<?> list) {
        List<Object> clone = new ArrayList<>(list.size());
        for (Object element : list) {
            if (immutables.contains(element.getClass())) {
                clone.add(element);
            }
            else if (element.getClass().isArray()) {
                int length = Array.getLength(element);
                Object array = Array.newInstance(element.getClass().getComponentType(), length);
                System.arraycopy(element, 0, array, 0, length);
                clone.add(array);
            }
            else {
                try {
                    clone.add(((IClone) element).clone());
                }
                catch (Throwable t) {
                   LOG.error(String.format("Instance of %s does not implement IClone interface", element.getClass()));
//                    Cloner cloner = new Cloner();
//                    clone.add(cloner.deepClone(element));
                }
            }
        }
        return clone;
    }

	// -----------------------------------------------------------------
	// Attributzugriffsmethoden
	// -----------------------------------------------------------------

	@SuppressWarnings("unchecked")
	public final <K> K getAttribute(int pos) {
		if (pos < 0 || pos > attributes.length)
			return null;
		return (K) this.attributes[pos];
	}

	@SuppressWarnings("unchecked")
	public final void addAttributeValue(int pos, Object value) {
		if (this.attributes[pos] != null
				&& !(this.attributes[pos] instanceof Collection)) {
			throw new RuntimeException(
					"Cannot add value to non collection type");
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

	// Temp commented out ... who needs this??
//	public final void setAttribute(int pos, CharBuffer value) {
//		String stringValue = new String(value.array(), 0, value.position());
//		setAttribute(pos, stringValue);
//	}

	public final int size() {
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
	 * Falls das aktuelle Tuple keine Schemainformationen enthaelt, wird das
	 * neue auch ohne erzeugt. Ansonsten enthaelt das neue Tuple auch die
	 * passenden Schemainformationen.
	 * 
	 * @param attrList
	 *            erzeugt ein neues Objekt das die Attribute der Positionen aus
	 *            attrList enth�lt
	 * @param createNew
	 *            if true, a new tuple will be created, if false the original
	 *            one will be modified
	 */
	public Tuple<T> restrict(int[] attrList, boolean createNew) {
		Object[] newAttrs = null;
		if (attrList != null) {
			newAttrs = new Object[attrList.length];

			for (int i = 0; i < attrList.length; i++) {
				newAttrs[i] = this.attributes[attrList[i]];
			}
		}
		return restrictCreation(createNew, newAttrs);
	}

	public Tuple<T> restrict(int attr, boolean createNew) {
		Object[] newAttrs = new Object[1];
		newAttrs[0] = this.attributes[attr];
		return restrictCreation(createNew, newAttrs);
	}

	/**
	 * Creates a new instance from the current tuple and appends the given
	 * attribute object to it
	 * 
	 * @param object
	 *            the object to append
	 * @return a new created and extended copy
	 */
	public Tuple<T> append(Object object) {
		return this.append(object, true);
	}

	/**
	 * Creates a new instance from the current tuple if the createNew param is
	 * true or uses the current instance and appends the given attribute object
	 * 
	 * @param object
	 *            the object to append
	 * @param createNew
	 *            indicates if create a copy
	 * @return the extended tuple
	 */
	public Tuple<T> append(Object object, boolean createNew) {
		Object[] newAttrs = null;
		if (this.attributes != null) {
			newAttrs = Arrays.copyOf(this.attributes,
					this.attributes.length + 1);
			newAttrs[this.attributes.length] = object;
		} else {
			newAttrs = new Object[1];
			newAttrs[0] = object;
		}
		if (createNew) {
			Tuple<T> newTuple = new Tuple<T>(this, newAttrs, requiresDeepClone);
			return newTuple;
		}
		this.attributes = newAttrs;
		return this;
	}

	public Tuple<T> appendList(List<?> objects, boolean createNew) {
		Object[] newAttrs = null;
		if (this.attributes != null) {
			newAttrs = Arrays.copyOf(this.attributes, this.attributes.length
					+ objects.size());
			for (int i = 0; i < objects.size(); i++) {
				newAttrs[this.attributes.length + i] = objects.get(i);
			}
		} else {
			newAttrs = new Object[objects.size()];
			for (int i = 0; i < objects.size(); i++) {
				newAttrs[i] = objects.get(i);
			}
		}
		if (createNew) {
			Tuple<T> newTuple = new Tuple<T>(this, newAttrs, requiresDeepClone);
			return newTuple;
		}
		this.attributes = newAttrs;
		return this;
	}

	private Tuple<T> restrictCreation(boolean createNew, Object[] newAttrs) {
		if (createNew) {
			if (newAttrs != null) {
				Tuple<T> newTuple = new Tuple<T>(this, newAttrs,
						requiresDeepClone);
				return newTuple;
			} else {
				Tuple<T> newTuple = new Tuple<>(this, requiresDeepClone);
				return newTuple;
			}
		}
		this.attributes = newAttrs;
		this.valueChanged = true;
		return this;
	}

	// /**
	// * Sets the attribute values of this tuple to the specified values in
	// * attributes.
	// *
	// * @param attributes
	// * The new attribute values for this tuple.
	// */
	// public void setAttributes(Object[] attributes) {
	// this.attributes = attributes;
	// this.valueChanged = true;
	// }

	public Object[] getAttributes() {
		return attributes;
	}

	public boolean containsNull() {
		if (this.valueChanged) {
			for (Object o : this.attributes) {
				if (o == null) {
					this.containsNull = true;
					break;
				}
			}
		}
		return this.containsNull;
	}

	public void setRequiresDeepClone(boolean requiresDeepClone) {
		this.requiresDeepClone = requiresDeepClone;
        //if (this.requiresDeepClone) {
            //this.cloner = new Cloner();
        //}
        //else {
            //this.cloner = null;
        //}
	}

	public boolean requiresDeepClone() {
		return this.requiresDeepClone;
	}

	@Override
	protected IStreamObject<T> process_merge(IStreamObject<T> left,
			IStreamObject<T> right, Order order) {
		if (order == Order.LeftRight) {
			return processMergeInternal((Tuple<T>) left, (Tuple<T>) right);
		} else {
			return processMergeInternal((Tuple<T>) right, (Tuple<T>) left);
		}
	}

	private IStreamObject<T> processMergeInternal(Tuple<T> left, Tuple<T> right) {
		Object[] newAttributes = mergeAttributes(
				left != null ? left.getAttributes() : null,
				right != null ? right.getAttributes() : null);
		Tuple<T> r = new Tuple<T>(newAttributes,
				((left != null) && (left.requiresDeepClone()))
						|| ((right != null) && (right.requiresDeepClone())));
		return r;
	}

	private Object[] mergeAttributes(Object[] leftAttributes,
			Object[] rightAttributes) {
		Object[] newAttributes = new Object[leftAttributes.length
				+ rightAttributes.length];
		System.arraycopy(leftAttributes, 0, newAttributes, 0,
				leftAttributes.length);
		System.arraycopy(rightAttributes, 0, newAttributes,
				leftAttributes.length, rightAttributes.length);
		return newAttributes;
	}

	// -----------------------------------------------------------------
	// Vergleichsmethoden
	// -----------------------------------------------------------------
	@Override
	public final boolean equals(Object o) {
		if (!(o instanceof Tuple)) {
			return false;
		}
		Tuple<?> t = (Tuple<?>) o;
		if (this.attributes.length != t.attributes.length) {
			return false;
		}
		for (int i = 0; i < attributes.length; i++) {
			// test if attributes are not null and equal
			// or both null (order is imporantant!)
			if (this.attributes[i] != null) {
				if (!this.attributes[i].equals(t.attributes[i])) {
					return false;
				}
			} else {
				if (t.attributes[i] != null) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Like normal equals-method but has a tolerance for double and float
	 * comparisons.
	 * 
	 * @param o
	 * @return
	 */
	@Override
	public final boolean equalsTolerance(Object o, double tolerance) {
		if (!(o instanceof Tuple)) {
			return false;
		}
		Tuple<?> t = (Tuple<?>) o;
		if (this.attributes.length != t.attributes.length) {
			return false;
		}

		for (int i = 0; i < attributes.length; i++) {
			Object attr = this.attributes[i];
			Object theirAttr = t.attributes[i];
			// test if attributes are not null and equal
			// or both null (order is imporantant!)
			if (attr != null) {
				if (attr instanceof Double && theirAttr instanceof Double) {
					if (Math.abs((Double) attr - (Double) theirAttr) > tolerance) {
						return false;
					}
				} else if (attr instanceof Float && theirAttr instanceof Float) {
					if (Math.abs((Float) attr - (Float) theirAttr) > tolerance) {
						return false;
					}
				} else {
					if (!attr.equals(theirAttr)) {
						return false;
					}
				}
			} else {
				if (theirAttr != null) {
					return false;
				}
			}
		}
		return true;
	}

	static public boolean equalsAt(Tuple<?> left, Tuple<?> right,
			int[] comparePositions) {
		for (int i : comparePositions) {
			Object a = left.getAttribute(i);
			Object b = right.getAttribute(i);
			if (a == null && b == null){
				continue;
			}
			if (!a.equals(b)) {
				return false;
			}
		}
		return true;
	}
	
	static public boolean equalsAt(Tuple<?> left, Tuple<?> right,
			List<Pair<Integer, Integer>> comparePositions) {
		for (Pair<Integer,Integer> i : comparePositions) {
			Object a;
			Object b;
			if (i.getE1() < 0){
				a = left.getAttribute(i.getE2());
				b = right.getAttribute(i.getE2());
			}else{
				a = left.getMetadata().getValue(i.getE1(), i.getE2());
				b = right.getMetadata().getValue(i.getE1(), i.getE2());
			}
			if (a == null && b == null){
				continue;
			}
			if (!a.equals(b)) {
				return false;
			}				

		}
		
		return true;
	}

	/**
	 * Liefert 0 wenn die beiden Attributlisten gleich sind ansonsten das erste
	 * Element an denen sich die Attributlisten unterscheiden. Die
	 * Sortierreihenfolge ist implizit durch die Position in der Liste gegeben
	 * wenn das aktuelle Objekt kleiner ist ist der R�ckgabewert negativ
	 * ansonsten positiv Es wird maximal die kleinere Anzahl der Felder
	 * verglichen. Tupel mit NULL-Werten sind ungleich.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final int compareTo(Tuple<?> c) {
		int min = c.size();
		if (min > this.attributes.length) {
			min = this.attributes.length;
		}
		int compare = 0;
		int i = 0;
		for (i = 0; i < min && compare == 0; i++) {
			// try {
			if (this.attributes[i] == null || c.getAttribute(i) == null) {
				compare = i;
			} else if (this.attributes[i] instanceof Comparable<?>) {
				compare = ((Comparable<Object>) this.attributes[i]).compareTo(c
						.getAttribute(i));
			}
			// } catch (NullPointerException e) {
			// System.out.println("Exception: " + this + " " + c + " " + i);
			// System.out.println("this " + this);
			// System.out.println("c " + c);
			// System.out.println("this.attributes[i] " + this.attributes[i]);
			// System.out.println("c.getAttribute(i) " + c.getAttribute(i));
			// throw new NullPointerException();
			// }
		}
		if (compare < 0) {
			compare = (-1) * i;
		}
		if (compare > 0) {
			compare = i;
		}
		return compare;
	}

	/**
	 * Liefert 0 wenn die beiden Attributlisten gleich sind ansonsten das erste
	 * Element an denen sich die Attributlisten unterscheiden. Die
	 * Sortierreihenfolge ist implizit durch die Position in der Liste gegeben
	 * wenn das aktuelle Objekt kleiner ist ist der R�ckgabewert negativ
	 * ansonsten positiv Es wird maximal die kleinere Anzahl der Felder
	 * verglichen. Tupel mit NULL-Werten sind ungleich.
	 */
	@SuppressWarnings("unchecked")
	public final int compareTo(Tuple<?> c, int[] restricted) {
		int compare = 0;
		int i = 0;
		for (int j = 0; j < restricted.length && compare == 0; j++) {
			i = restricted[j];
			// try {
			if (this.attributes[i] == null || c.getAttribute(i) == null) {
				compare = i;
			} else if (this.attributes[i] instanceof Comparable<?>) {
				compare = ((Comparable<Object>) this.attributes[i]).compareTo(c
						.getAttribute(i));
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
	public String toString() {
		StringBuffer retBuff = new StringBuffer();
		if (attributes.length > 0) {
			retBuff.append(this.attributes[0] == null ? "" : this.attributes[0]);
		} else {
			retBuff.append("null");
		}
		for (int i = 1; i < this.attributes.length; ++i) {
			Object curAttribute = this.attributes[i];
			retBuff.append("|");
			retBuff.append(curAttribute == null ? "<NULL>" : curAttribute
					.toString());
		}
		if (getMetadata() != null) {
			retBuff.append(" | META | " + getMetadata());
		}
		return retBuff.toString();
	}

	public String toString(final int[] gRestrict) {
		StringBuffer value = new StringBuffer();
		for (int pos : gRestrict) {
			value.append(String.valueOf(getAttribute(pos))).append(" ");
		}
		return value.toString();
	}

	@Override
	public String csvToString(WriteOptions options) {
		// TODO Auto-generated method stub
		
		StringBuffer retBuff = new StringBuffer();
		if (attributes.length > 0) {
			for (int i = 0; i < this.attributes.length; ++i) {
				Object curAttribute = this.attributes[i];
				if (i > 0) {
					retBuff.append(options.getDelimiter());
				}
				if (curAttribute == null) {
					retBuff.append(options.getNullValueString());
				} else {
					if (curAttribute instanceof Number) {
						if ((curAttribute instanceof Double || curAttribute instanceof Float)
								&& options.hasFloatingFormatter()) {
							retBuff.append(options.getFloatingFormatter()
									.format(curAttribute));
						} else if (!((curAttribute instanceof Double || curAttribute instanceof Float))
								&& options.hasFloatingFormatter()) {
							retBuff.append(options.getNumberFormatter().format(curAttribute));
						} else {
							retBuff.append(curAttribute);
						}
					} else if (curAttribute instanceof double[]) {
						double[] values = (double[]) curAttribute;
						for (int iValue = 0; iValue < values.length; iValue++) {
							if (iValue > 0) {
								retBuff.append(options.getDelimiter());
							}
							if (options.hasFloatingFormatter()) {
								retBuff.append(options.getFloatingFormatter()
										.format(values[iValue]));
							} else {
								retBuff.append(values[iValue]);
							}
						}
					} else if (curAttribute instanceof BitVector){
						//BitVector v = (BitVector) curAttribute;
						if (options.hasNumberFormatter())
						{
							long valAsNum = Long.parseLong(curAttribute.toString());
							retBuff.append(options.getNumberFormatter().format(valAsNum));
						}
						else
							retBuff.append(curAttribute);
					} else {
						if (options.hasTextSeperator()
								&& curAttribute instanceof String) {
							retBuff.append(options.getTextSeperator())
									.append(curAttribute.toString())
									.append(options.getTextSeperator());
						} else {
							retBuff.append(curAttribute.toString());
						}
					}
				}
			}
		} else {
			retBuff.append("null");
		}

		if (options.isWithMetadata()) {
			retBuff.append(options.getDelimiter()).append(
					getMetadata().csvToString(options));
			if (getMetadataMap() != null && getMetadataMap().size() > 0) {
				for (Entry<String, Object> e : getMetadataMap().entrySet()) {
					if (e.getValue() == null) {
						// add empty value
						retBuff.append(options.getDelimiter());
					} else if (e.getValue() instanceof IMetaAttribute) {
						IMetaAttribute m = (IMetaAttribute) e.getValue();
						retBuff.append(options.getDelimiter()).append(
								m.csvToString(options));
					} else {
						String value = e.getValue().toString();
						if (value.indexOf(options.getDelimiter()) != -1) {
							retBuff.append(options.getDelimiter()).append(options.getTextSeperator())
									.append(e.getValue()).append(options.getTextSeperator());
						} else {
							retBuff.append(options.getDelimiter()).append(e.getValue());
						}
					}
				}
			}
		}
		return retBuff.toString();

	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < attributes.length; i++) {
			ret.append(delimiter);
		}
		ret.append(getMetadata().getCSVHeader(delimiter));
		return ret.toString();
	}

	@Override
	public final int hashCode() {
		int ret = 0;
		for (int i = 0; i < this.attributes.length; i++) {
			Object o = this.attributes[i];
			if (o != null) {
				ret = 31 * ret + o.hashCode();
			}
		}
		return ret;
	}

	/**
	 * Returns a hash code for the values of the specified attributes. Note:
	 * this method generates a hash code only for the given attributes, not for
	 * the hole tuple
	 * 
	 * @param attributeNumbers
	 *            the position of the attributes in the tuple
	 * @return the generated hash code for the given attributes
	 */
	@Override
	public final int restrictedHashCode(int[] attributeNumbers) {
		int ret = 0;
		for (int i = 0; i < attributeNumbers.length; i++) {
			Object o = this.attributes[attributeNumbers[i]];
			if (o != null) {
				ret = 31 * ret + o.hashCode();
			}
		}
		return ret;
	}

}