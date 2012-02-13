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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Die <code>SchemaHelper</code>-Klasse bietet unterschiedliche Methoden zur
 * Verarbeitung (objektrelationaler) Schemata. Damit können über einen gegebenen
 * Namen Schemaindexpfade erstellt, nach bestimmten Attributen gesucht und
 * Zeitstempelattribute leicht gefunden werden.
 * <p>
 * Das zu betrachtende Schema wird über den Konstruktor angegeben. Sollen
 * mehrere Schemata verwendet werden, so müssen mehrere Instanzen dieser Klasse
 * erstellt werden. Das nachträgliche ändern des verwendeten Schemas in einer
 * Instanz ist nicht möglich.
 * 
 * @author Timo Michelsen
 * 
 */
public class SchemaHelper {

	/**
	 * Trenner im Attributnamen zwischen Quellenbezeichnung und Attributnamen
	 */
	public static final String SOURCE_SEPARATOR = ".";

	/**
	 * Trenner im Attributenamen zwischen den einzelnen Hierarchieattributen
	 * (für objektrelationale Schemata relevant)
	 */
	public static final String ATTRIBUTE_SEPARATOR = ":";

	private SDFSchema schema;
	private Map<String, SchemaIndexPath> paths = new HashMap<String, SchemaIndexPath>();

	private String sourceName = null;
	private String startTimestampAttribute = null;
	private String endTimestampAttribute = null;

	/**
	 * Konstruktur, welcher eine neue <code>SchemaHelper</code>-Instanz
	 * erstellt. Zur Grundlage der weiteren Verarbeitung wird das angegebene
	 * Schema verwendet. Erste Informationen werden gesammelt.
	 * 
	 * @param schema
	 *            Das zu verarbeitende Schema. Darf nicht <code>null</code>
	 *            sein.
	 * 
	 * @throws IllegalArgumentException
	 *             Wenn <code>schema</code> <code>null</code> ist.
	 */
	public SchemaHelper(SDFSchema schema) {
		if (schema == null)
			throw new IllegalArgumentException("schema is null");

		this.schema = schema;
		calculateAllPaths(schema, new ArrayList<SchemaIndex>(), null);
	}

	/**
	 * Konstruktor, welcher eine Kopie der angegeben Instanz wird.
	 * 
	 * @param other
	 *            Instanz, wovon eine Kopie erzeugt werden soll.
	 */
	private SchemaHelper(SchemaHelper other) {
		this.schema = other.schema.clone();
		calculateAllPaths(schema, new ArrayList<SchemaIndex>(), null);
	}

	/**
	 * Liefert das Schema zurück, welches mit dieser Instanz der
	 * <code>SchemaHelper</code>-Klasse verarbeitet wird.
	 * 
	 * @return Schema
	 */
	public SDFSchema getSchema() {
		return schema;
	}

	/**
	 * Liefert einen vollständigen <code>SchemaIndexPath</code> des angegebenen
	 * Attributnamens zurück. Dieser gibt an, wie im Schema navigiert werden
	 * muss, um das angegeben Attribut zu finden. Dabei ist es nicht zwingend
	 * erforderlich, dass die Quelle mit angegeben werden muss.
	 * 
	 * @param fullAttributeName
	 *            Vollständiger Attributname, dessen Indexpfad zurückgegeben
	 *            werden soll. Quellenname ist optional. Darf nicht
	 *            <code>null</code> oder leer sein.
	 * 
	 * @return Schemaindexpfad des angegeben Attributnamens.
	 * 
	 * @throws IllegalArgumentException
	 *             Wenn <code>fullAttributeName</code> <code>null</code> oder
	 *             leer ist.
	 */
	public SchemaIndexPath getSchemaIndexPath(String fullAttributeName) {
		if (fullAttributeName == null || fullAttributeName.isEmpty())
			throw new IllegalArgumentException("fullAttributeName is null or empty");

		// Quellennamen angegegeben?
		String toFind = "";

		if (fullAttributeName.contains(SOURCE_SEPARATOR)) {
			String[] parts = fullAttributeName.split("\\" + SOURCE_SEPARATOR);
			toFind = parts[1];
		} else {
			toFind = fullAttributeName;
		}
		SchemaIndexPath p = paths.get(toFind);
		if (p != null)
			return p;

		throw new IllegalArgumentException("cannot find schemaindexpath for " + toFind);
	}

	/**
	 * Liefert das <code>SDFAttribute</code> des angegebenen vollständigen
	 * Attributnamens.
	 * 
	 * @param fullAttributeName
	 *            Vollständiger Attributname, dessen <code>SDFAttribute</code>
	 *            geliefert werden soll. Darf nicht <code>null</code> oder leer
	 *            sein.
	 * 
	 * @return <code>SDFAttribute</code> des angegebenen Attributnamens.
	 */
	public SDFAttribute getAttribute(String fullAttributeName) {
		SchemaIndexPath p = getSchemaIndexPath(fullAttributeName);
		return p.getAttribute();
	}

	/**
	 * Liefert zurück, ob der angegebene vollständige Attributname im Schema
	 * existiert.
	 * 
	 * @param fullAttributeName
	 *            Vollständiger Attributname
	 * @return Liefert <code>true</code>, falls der Attributname im Schema
	 *         vorhanden ist, sonst <code>fale</code>.
	 */
	public boolean existsAttribute(String fullAttributeName) {
		return paths.containsKey(fullAttributeName);
	}

	/**
	 * Liefert eine unveränderliche Liste aller vollständigen Attributnamen, die
	 * im Schema vorhanden sind.
	 * 
	 * @return Unveränderliche Liste aller vollständigen Attributenamen im
	 *         Schema.
	 */
	public Collection<String> getAttributeNames() {
		return Collections.unmodifiableCollection(paths.keySet());
	}

	/**
	 * Liefert eine unveränderliche Liste aller Schemaindexpfade, die im Schema
	 * vorhanden sind.
	 * 
	 * @return Unveränderliche Liste aller Schemaindexpfade im Schema
	 */
	public Collection<SchemaIndexPath> getSchemaIndexPaths() {
		return Collections.unmodifiableCollection(paths.values());
	}

	/**
	 * Liefert den Quellennamen des Schemas.
	 * 
	 * @return Quellenname des Schemas.
	 */
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * Liefert den vollständigen Namen des Attributs, welcher den
	 * Startzeitstempel beinhaltet, falls im Schema vorhanden.
	 * 
	 * @return Vollständigen Namen des Attributs, welcher den Startzeitstempel
	 *         beinhaltet oder <code>null</code>, falls dieser im Schema nicht
	 *         vorhanden ist.
	 */
	public String getStartTimestampFullAttributeName() {
		return startTimestampAttribute;
	}

	/**
	 * Liefert den vollständigen Namen des Attributs, welcher den
	 * Endzeitstempel beinhaltet, falls im Schema vorhanden.
	 * 
	 * @return Vollständigen Namen des Attributs, welcher den Endzeitstempel
	 *         beinhaltet oder <code>null</code>, falls dieser im Schema nicht
	 *         vorhanden ist.
	 */
	public String getEndTimestampFullAttributeName() {
		return endTimestampAttribute;
	}

	//Berechnet alle Pfade im einem Schema und speichert sie in einer <code>Map</code> ab.
	// Dabei wird rekursiv die Tiefe des Schemas berücksichtigt, sodass auch objektrelationale
	// Schemata berücksichtigt werden können.
	private void calculateAllPaths(SDFSchema list, List<SchemaIndex> actualPath, String actualAttributeName) {
		for (int index = 0; index < list.size(); index++) {
			SDFAttribute attribute = list.getAttribute(index);
			if (sourceName == null)
				sourceName = attribute.getSourceName();
			String fullAttributeName = "";
			if (actualAttributeName != null)
				fullAttributeName = actualAttributeName + ATTRIBUTE_SEPARATOR + attribute.getAttributeName();
			else
				fullAttributeName = attribute.getAttributeName();

			// timestamp
			if (attribute.getDatatype().isStartTimestamp()) {
				startTimestampAttribute = fullAttributeName;
			} else if (attribute.getDatatype().isEndTimestamp()) {
				endTimestampAttribute = fullAttributeName;
			}

			actualPath.add(new SchemaIndex(index, attribute));
			paths.put(fullAttributeName, new SchemaIndexPath(copy(actualPath), attribute));

			if(attribute.getDatatype().hasSchema()){
				calculateAllPaths(attribute.getDatatype().getSubSchema(), actualPath, fullAttributeName);
			}

			actualPath.remove(actualPath.size() - 1);
		}
	}

	// Erstellt eine tiefe Kopie einer Liste aus
	// Schemaindizes
	private List<SchemaIndex> copy(List<SchemaIndex> base) {
		List<SchemaIndex> newList = new ArrayList<SchemaIndex>();
		for (int i = 0; i < base.size(); i++)
			newList.add(new SchemaIndex(base.get(i)));
		return newList;
	}

	@Override
	public SchemaHelper clone() {
		return new SchemaHelper(this);
	}

}
