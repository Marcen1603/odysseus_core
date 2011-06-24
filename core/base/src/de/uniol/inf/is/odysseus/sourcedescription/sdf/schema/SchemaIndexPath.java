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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

	public List<SchemaIndex> indices;
	private boolean hasListInside = false;
	private SDFAttribute to;
	private int[] indicesArray;

	public SchemaIndexPath(List<SchemaIndex> indices, SDFAttribute attributeTo) {
		this.indices = indices;
		for (SchemaIndex i : indices) {
			if (i.isList()) {
				hasListInside = true;
				break;
			}
		}
		this.to = attributeTo;
		this.indicesArray = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++){
			this.indicesArray[i] = indices.get(i).toInt();
		}
		
		for(int i : this.indicesArray){
			if(i == -1){
				@SuppressWarnings("unused")
				int v = 0;
			}
		}
	}

	SchemaIndexPath(SchemaIndexPath other) {
		this.indices = new ArrayList<SchemaIndex>();
		for (SchemaIndex idx : other.indices) {
			indices.add(idx.clone());
		}
		this.hasListInside = other.hasListInside;
		this.to = other.to.clone();
		this.indicesArray = new int[other.indicesArray.length];
		for (int i = 0; i < this.indicesArray.length; i++) {
			this.indicesArray[i] = other.indicesArray[i];
		}
		
		for(int i : this.indicesArray){
			if(i == -1){
				@SuppressWarnings("unused")
				int v = 0;
			}
		}
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
	 * Liefert einen neuen Schemaindexpfad, der dem urprünglichen
	 * gleicht, aber den neuen angegebenen Index erweitert wurde.
	 * 
	 * @param index Neuer Index, der an das neue Schemaindexpfad angehängt werden soll
	 * 
	 * @return Neuer Schemaindexpfad mit zusätzlichem Index
	 */
	public SchemaIndexPath appendClone( int index ) {
		List<SchemaIndex> i = new ArrayList<SchemaIndex>(indices);
		SDFAttribute att = to;
		if( att.getDatatype().isSet()) {// eine Liste?
			SDFAttribute tempAttr = null;
			
			if(att.getDatatype().hasSchema()){
				tempAttr = att.getDatatype().getSubSchema().getAttribute(0);
			}
			else{
				tempAttr = att;
			}
			i.add(new SchemaIndex(index, tempAttr));
//			SDFAttribute att2 = to.getSubattribute(0);
			SDFAttribute att2 = tempAttr;
			return new SchemaIndexPath(i, att2);
		} else {
			// seems that there must be subschema
			i.add(new SchemaIndex(index, att.getDatatype().getSubSchema().getAttribute(index)));
			SDFAttribute att2 = to.getDatatype().getSubSchema().getAttribute(index);
			return new SchemaIndexPath(i, att2);
		}
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
	 * Liefert eine Liste der Indices als int[]-Array. An dieser Liste d�rfen keine
	 * Modifikationen durchgef�hrt werden.
	 * 
	 * @return int[]-Array des Pfads.
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
	@Override
	public SchemaIndexPath clone() {
		return new SchemaIndexPath(this);
	}
}
