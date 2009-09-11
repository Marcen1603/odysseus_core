package de.uniol.inf.is.odysseus.relational.base;

import java.io.Serializable;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeConstraint;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

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

	protected SDFAttributeList schema;

	/**
	 * @uml.property name="delimiter"
	 */
	protected char delimiter = '|';

	protected int memSize = -1;

	// -----------------------------------------------------------------
	// static Hilfsmethoden
	// -----------------------------------------------------------------
	public RelationalTuple<T> clone() {
		return new RelationalTuple<T>(this);
	}

	/**
	 * Splittet die Zeile anhand des Trennzeichens in ein Array von Strings mit
	 * den jeweiligen Attributen auf
	 * 
	 * @param line
	 *            enthaelt die konkatenierten Attribute
	 * @param delimiter
	 *            enthaelt das Trennzeichen
	 * @param noOfAttribs
	 *            enthaelt die Anzahl der Attribute
	 * @returns Array mit den Attributen
	 */
	protected final static Object[] splittLineToAttributes(final String line,
			final char delimiter, final SDFAttributeList schema) {
		String[] attributes = line.split(Pattern.quote(new String(
				new char[] { delimiter })));
		// Pattern p = Pattern.compile("(.*)[(" + delimiter + ".*)*");
		// Matcher m = p.matcher(line);
		int count = attributes.length;
		if (count != schema.size()) {
			throw new IllegalArgumentException(
					"invalid number of attributes: got " + count + " expected "
							+ schema.size());
		}
		//
		Object[] tokens = new Object[attributes.length];
		for (int i = 0; i < attributes.length; ++i) {
			tokens[i] = convertAttribute(attributes[i], schema.get(i));
		}
		return tokens;
	}

	// -----------------------------------------------------------------
	// Attributzugriffsmethoden
	// -----------------------------------------------------------------

	private final static Object convertAttribute(String stringValue,
			SDFAttribute attribute) {
		if (SDFDatatypes.isString(attribute.getDatatype())) {
			return stringValue;
		}

		if (attribute.getDatatype().getURI(false) == "Integer") {
			return Integer.parseInt(stringValue);
		}
		if (attribute.getDatatype().getURI(false) == "Double") {
			return Double.parseDouble(stringValue);
		}
		// TODO richtig machen mit den datentypen
		if (SDFDatatypes.isNumerical(attribute.getDatatype())) {
			Iterator<SDFDatatypeConstraint> i = attribute.getDtConstraints()
					.iterator();
			while (i.hasNext()) {
				SDFDatatypeConstraint constraint = i.next();
				if (SDFDatatypeConstraints.isInteger(constraint)) {
					return Integer.parseInt(stringValue);
				}
				if (SDFDatatypeConstraints.isRational(constraint)) {
					return Double.parseDouble(stringValue);
				}
			}

			throw new IllegalArgumentException(
					"missing datatype constraint for numerical attribute (integer/rational)");
		}

		throw new IllegalArgumentException("attributes of type "
				+ attribute.getDatatype() + " can't be used with "
				+ RelationalTuple.class);
	}

	@SuppressWarnings("unchecked")
	public final <K> K getAttribute(int pos) {
		return (K) this.attributes[pos];
	}

	public final void setAttribute(int pos, Object value) {
		if (this.schema != null) {
			if (value == null) {
				// TODO kann man ueberpruefen, ob null werte erlaubt sind?
				this.attributes[pos] = value;
			} else {
				if (value instanceof Number || value instanceof String) {
					checkDataType(value, schema.get(pos));
					this.attributes[pos] = value;
				} else {
					throw new IllegalArgumentException(RelationalTuple.class
							.getSimpleName()
							+ " only allows String and Number types");
				}
			}
		} else {
			this.attributes[pos] = value;
		}
		//calcSize();
	}

	public final void setUnchecked() {
		this.schema = null;
	}

	public final void setChecked(SDFAttributeList schema) {
		this.schema = schema;
		// TODO evtl hier schonmal die aktuellen attribute checken
	}

	public final void setAttribute(int pos, String value) {
		if (this.schema != null) {
			checkDataType(value, schema.get(pos));
		}
		this.attributes[pos] = value;
		//calcSize();
	}
	
	public final void setAttribute(int pos, CharBuffer value){
		String stringValue = new String(value.array(), 0, value.position());
		setAttribute(pos, stringValue);	
	}
	
	
	public final void setAttribute(int pos, Integer value) {
		if (this.schema != null) {
			checkDataType(value, schema.get(pos));
		}
		this.attributes[pos] = value;
		//calcSize();
	}

	public final void setAttribute(int pos, Double value) {
		if (this.schema != null) {
			checkDataType(value, schema.get(pos));
		}
		this.attributes[pos] = value;
		//calcSize();
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
	 *            attrList enthï¿½lt
	 */
	public RelationalTuple<T> restrict(int[] attrList,
			SDFAttributeList overwriteSchema) {
		RelationalTuple<T> newAttrList = null;

		SDFAttributeList newSchema = overwriteSchema;
		if (overwriteSchema == null) {
			// Schema anpassen
			if (schema != null) {
				newSchema = new SDFAttributeList();
				for (int i : attrList) {
					newSchema.add(getSchema().get(i));
				}
			}
		}

		if (this.schema == null && overwriteSchema == null) {
			newAttrList = new RelationalTuple<T>(attrList.length);
		} else {
			newAttrList = new RelationalTuple<T>(newSchema);
		}

		newAttrList.setMetadata(this.getMetadata());

		for (int i = 0; i < attrList.length; i++) {
			Object curAttribute = this.attributes[attrList[i]];
			newAttrList.setAttribute(i, curAttribute);
		}

		return newAttrList;
	}

	
	// -----------------------------------------------------------------
	// Vergleichsmethoden
	// -----------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public final boolean equals(Object o) {
		return this.compareTo((RelationalTuple) o) == 0;
	}

	/**
	 * Liefert 0 wenn die beiden Attributlisten gleich sind ansonsten das erste
	 * Element an denen sich die Attributlisten unterscheiden. Die
	 * Sortierreihenfolge ist implizit durch die Position in der Liste gegeben
	 * wenn das aktuelle Objekt kleiner ist ist der Rï¿½ckgabewert negativ
	 * ansonsten positiv Es wird maximal die kleinere Anzahl der Felder
	 * verglichen
	 */
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

	public final String toString() {
		StringBuffer retBuff = new StringBuffer();
		if (attributes.length > 0){
			retBuff.append(this.attributes[0] == null ? "" : this.attributes[0]);
		}else{
			retBuff.append("null");
		}
		for (int i = 1; i < this.attributes.length; ++i) {
			Object curAttribute = this.attributes[i];
			retBuff.append(delimiter);
			retBuff.append(curAttribute == null ? "" : curAttribute.toString());
		}
		retBuff.append(" size = "+(memSize==-1?" unavailable ":memSize));
		retBuff.append(" {META " + getMetadata() + "}");
		return retBuff.toString();
	}
	
	public int memSize(boolean calcNew){
		if (calcNew){
			calcSize();
		}
		return memSize;
	}
	
	private void calcSize(){
		memSize = 0;
		for (Object attObject: attributes){
			if (attObject == null) continue;
			if (attObject instanceof Integer){
				memSize+=Integer.SIZE/8;
			}else if (attObject instanceof Double){
				memSize+=Double.SIZE/8;
			}else if (attObject instanceof Long){
				memSize+=Long.SIZE/8;
			}else if (attObject instanceof String){
				memSize+=((String)attObject).length()*2 // Unicode!
					+Integer.SIZE/8; // Für die Längeninformation (evtl. anders machen?)
			}else{
				throw new IllegalArgumentException("Illegal Relation Attribute Type "+attObject);
			}
		}
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
	 *            enthaelt die Anzahl der Attribute (Effizienzgrï¿½nde)
	 */
	public RelationalTuple(SDFAttributeList schema, String line, char delimiter) {
		this.schema = schema;
		this.attributes = splittLineToAttributes(line, delimiter, schema);
	}

	/**
	 * Erzeugt ein neues leeres Object, zur Erzeugung von Zwischenergebnissen
	 * 
	 * @param attributeCount
	 *            enthaelt die Anzahl der Attribute die das Objekt speichern
	 *            koennen soll
	 */
	public RelationalTuple(SDFAttributeList schema) {
		if (schema.size() == 0) {
			throw new IllegalArgumentException("attribute count has to be > 0");
		}

		this.schema = schema;
		this.attributes = new Object[schema.size()];
		//calcSize();
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
		System.arraycopy(copy.attributes, 0, this.attributes, 0,
				attributeLength);
		this.schema = copy.schema;
		this.delimiter = copy.delimiter;
		//calcSize();
	}

	/**
	 * Erzeugt ein neues Tuple und ueberprueft die uebergebenen Attribute
	 * mittels des uebergebenen Schemas
	 * 
	 * @param schema
	 *            Schema des neuen Tuples
	 * @param attributes
	 *            Attributbelegungen des neuen Tuples
	 */
	public RelationalTuple(SDFAttributeList schema, Object... attributes) {
		if (schema.size() != attributes.length) {
			throw new IllegalArgumentException("listsize doesn't match schema");
		}

		for (int i = 0; i < schema.size(); ++i) {
			if (!checkDataType(attributes[i], schema.get(i))) {
				throw new IllegalArgumentException("attribute " + i
						+ " has an invalid type");
			}
		}

		this.schema = schema;
		this.attributes = attributes.clone();
		//calcSize();
	}
	

	/**
	 * Erzeugt ein neues Tuple mit Attributen und ohne Schemainformationen
	 * 
	 * @param attributes
	 *            Attributbelegung des neuen Tuples
	 */
	public RelationalTuple(Object[] attributes) {
		this.attributes = attributes.clone();
		//calcSize();
	}

	private final boolean checkDataType(Object object, SDFAttribute attribute) {
		if (object instanceof String) {
			return SDFDatatypes.isString(attribute.getDatatype());
		}

		// Should not be useful anymore, since datatype Date exists
		// and other non numerical datatypes may be added in future
		// development. So it would be better to check every
		// datatype separately
//		if (!SDFDatatypes.isNumerical(attribute.getDatatype())) {
//			return false;
//		}

		if (object instanceof Integer) {
			Iterator<SDFDatatypeConstraint> i = attribute.getDtConstraints()
					.iterator();
			while (i.hasNext()) {
				SDFDatatypeConstraint constraint = i.next();
				if (SDFDatatypeConstraints.isInteger(constraint)) {
					return true;
				}
			}
			return false;
		}
		if (object instanceof Double) {
			Iterator<SDFDatatypeConstraint> i = attribute.getDtConstraints()
					.iterator();
			while (i.hasNext()) {
				SDFDatatypeConstraint constraint = i.next();
				if (SDFDatatypeConstraints.isRational(constraint)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	public final int hashCode() {
		int ret = 0;
		for (Object o : this.attributes) {
			ret += o.hashCode();
		}
		return ret;
	}

	public SDFAttributeList getSchema() {
		return schema;
	}

	public Object[] getAttributes() {
		return attributes;
	}

}