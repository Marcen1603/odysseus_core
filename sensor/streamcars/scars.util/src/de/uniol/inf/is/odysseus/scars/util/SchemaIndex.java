package de.uniol.inf.is.odysseus.scars.util;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * Repräsentiert einen Indexeintrag in einem Schemapfad. Der Indexeintrag
 * speichert den SDFAttribute, worin sich der Indexeintrag verweist. Über
 * <code>getAttribute()</code> lässt sich dieser abrufen. Sie wird innerhalb der
 * SchemaIndexPath-Klasse verwendet.
 * 
 * Von dieser Klasse sollte nicht abgeleitet werden.
 * 
 * @author Timo Michelsen
 * 
 */
public class SchemaIndex {

	private int index;
	private SDFAttribute attribute;
	private boolean isList = false;

	SchemaIndex(int index, SDFAttribute attribute) {
		this.index = index;
		this.attribute = attribute;
		String dataType = attribute.getDatatype().getQualName();
		this.isList = dataType.equals("List") ? true : false;
	}

	SchemaIndex(SchemaIndex index) {
		this.index = index.index;
		this.attribute = index.attribute;
		this.isList = index.isList;
	}

	/**
	 * Liefert den Index als <code>int</code>-Wert. Ist äquivalent zu
	 * <code>getIndex()</code>.
	 * 
	 * @return Index als <code>int</code>.
	 */
	public int toInt() {
		return index;
	}

	/**
	 * Liefert den Index. Ist äquivalent zu <code>toInt()</code>.
	 * 
	 * @return Index als <code>int</code>.
	 */
	public int getIndex() {
		return toInt();
	}

	/**
	 * Liefert das Attribut, worauf dieser SchemaIndex-Eintrag zeigt.
	 * 
	 * @return Attribut
	 */
	public SDFAttribute getAttribute() {
		return attribute;
	}

	/**
	 * Gibt zurück, ob es sich bei dem Attribut, worauf dieser Index zeigt, um
	 * eine Liste handelt.
	 * 
	 * @return <code>true</code>, falls Attribut eine Liste, <code>false</code>
	 *         sonst.
	 */
	public boolean isList() {
		return isList;
	}

	@Override
	public String toString() {
		return String.valueOf(index);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof SchemaIndex))
			return false;

		SchemaIndex idx = (SchemaIndex) obj;

		return idx.isList == this.isList && idx.index == this.index && idx.attribute.equals(this.attribute);
	}

	/**
	 * Liefert eine tiefe Kopie der aktuellen Instanz.
	 * 
	 * @return Tiefe Kopie der aktuellen Instanz.
	 */
	@Override
	public SchemaIndex clone() {
		return new SchemaIndex(this);
	}
}
