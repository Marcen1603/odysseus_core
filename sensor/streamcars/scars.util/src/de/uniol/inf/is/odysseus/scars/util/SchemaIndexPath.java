package de.uniol.inf.is.odysseus.scars.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * Repräsentiert einen Pfad innerhalb eines Schemas. Dieser wurde so
 * implementiert, dass sich dieser nach der Instanziierung nicht ändern kann.
 * <p>
 * Nutzer können nicht selbständig neue Instanzen erzeugen. Dies geschieht über
 * TupleIterator oder SchemaIndexPath. Dort werden sie verwendet.
 * 
 * @author Timo Michelsen
 * 
 */
public class SchemaIndexPath {

	private List<SchemaIndex> indices;
	private boolean hasListInside = false;
	private SDFAttribute to;

	SchemaIndexPath(List<SchemaIndex> indices, SDFAttribute attributeTo) {
		this.indices = indices;
		for (SchemaIndex i : indices) {
			if (i.isList()) {
				hasListInside = true;
				break;
			}
		}
		this.to = attributeTo;
	}

	SchemaIndexPath(SchemaIndexPath other) {
		this.indices = new ArrayList<SchemaIndex>();
		for (SchemaIndex idx : other.indices) {
			indices.add(idx.clone());
		}
		this.hasListInside = other.hasListInside;
		this.to = other.to.clone();
	}

	/**
	 * Liefert die aktuelle Länge des Pfades.
	 * 
	 * @return Läge des Pfades
	 */
	public int getLength() {
		return indices.size();
	}

	/**
	 * Gibt zurück, ob sich innerhalb des Pfades ein Listenattribut befindet.
	 * 
	 * @return <code>true</code>, wenn sich innerhalb des Pfades ein
	 *         Listenattribut befindet, <code>false</code> sonst.
	 */
	public boolean hasList() {
		return hasListInside;
	}

	/**
	 * Liefert das Attribut, worauf der SchemaIndexPfad zeigt.
	 * 
	 * @return Attribut
	 */
	public SDFAttribute getAttribute() {
		return to;
	}

	/**
	 * Liefert den vollständigen Namen des Attributes, worauf dieser
	 * SchemaIndexPfad zeigt. Die beinhaltet auch den Quellennamen.
	 * 
	 * @return Vollständigen Attributenamen
	 */
	public String getFullAttributeName() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indices.size(); i++) {
			if (i == 0) {
				sb.append(indices.get(i).getAttribute().getSourceName());
				sb.append(SchemaHelper.SOURCE_SEPARATOR);
			}
			if( i > 0 ) 
				sb.append(SchemaHelper.ATTRIBUTE_SEPARATOR);
			
			sb.append(indices.get(i).getAttribute().getAttributeName());
		}
		return sb.toString();
	}

	/**
	 * Liefert eine Liste der Indices, die den SchemaIndexPath ausmachen. Die
	 * Liste kann nicht modifiziert werden.
	 * 
	 * @return Liste der Indizes.
	 */
	public List<SchemaIndex> getSchemaIndices() {
		return Collections.unmodifiableList(indices);
	}

	/**
	 * Liefert eine Liste der Indices als int[]-Array. Modifikationen an dieser
	 * Liste haben keine Auswirkungen auf das SchemaIndexPath.
	 * 
	 * @return int[]-Array des Pfads.
	 */
	public int[] toArray() {
		int[] array = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++)
			array[i] = indices.get(i).toInt();
		return array;
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
	public TupleIndexPath toTupleIndexPath(MVRelationalTuple<?> tuple) {
		List<TupleIndex> list = new ArrayList<TupleIndex>();
		Object parent = tuple;
		for (int i = 0; i < indices.size(); i++) {
			TupleIndex idx = new TupleIndex(((MVRelationalTuple<?>) parent), indices.get(i).toInt(), indices.get(i).getAttribute());
			list.add(idx);

			if (parent instanceof MVRelationalTuple)
				parent = ((MVRelationalTuple<?>) parent).getAttribute(indices.get(i).toInt());
		}
		return new TupleIndexPath(list, this);
	}

	/**
	 * Liefert das SchemaIndex an der angegeben Stelle im SchemaIndexPfad.
	 * 
	 * @param index
	 *            Index im SchemaIndexPfad
	 * @return Index
	 */
	public SchemaIndex getSchemaIndex(int index) {
		return indices.get(index);
	}

	/**
	 * Liefert das letzte Index im SchemaIndexPfad.
	 * 
	 * @return Index des letzten Pfadschrittes.
	 */
	public SchemaIndex getLastSchemaIndex() {
		return getSchemaIndex(indices.size() - 1);
	}

	/**
	 * Liefert einen Stringpfad, welcher durch diesen SchemaIndexPfad gebildet
	 * wird.
	 * 
	 * @return Stringpfad.
	 */
	public String[] toStringPath() {
		String[] path = new String[indices.size()];
		for (int i = 0; i < indices.size(); i++) {
			path[i] = indices.get(i).getAttribute().getAttributeName();
		}
		return path;
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

		SchemaIndexPath idx = (SchemaIndexPath) obj;

		return idx.indices.equals(this.indices) && idx.hasListInside == this.hasListInside && idx.to.equals(this.to);
	}

	/**
	 * Liefert eine eigene neue Instanz des SchemaIndexPath, der genau dem
	 * Original entspricht. Es wird eine Tiefe Kopie erzeugt. Das betrifft unter
	 * Anderem die SchemaIndizes.
	 * 
	 * @return Tiefe Kopie dieser SchemaIndexPath-Instanz.
	 */
	public SchemaIndexPath clone() {
		return new SchemaIndexPath(this);
	}
}
