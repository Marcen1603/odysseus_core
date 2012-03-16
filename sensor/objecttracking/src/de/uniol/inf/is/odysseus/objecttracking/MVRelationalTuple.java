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
package de.uniol.inf.is.odysseus.objecttracking;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

/**
 * A class for multivariate stream processing.
 * 
 * @author Andre Bolles
 * 
 * @deprecated use MVTuple
 */
@Deprecated
public class MVRelationalTuple<T extends IProbability> extends MVTuple<T> {
    private static final long serialVersionUID = -8921538607877809462L;

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
    public MVRelationalTuple(SDFSchema schema, String line, char delimiter) {
        super(schema, line, delimiter);
    }

    /**
     * Erzeugt ein neues leeres Object, zur Erzeugung von Zwischenergebnissen
     * 
     * @param attributeCount
     *            enthaelt die Anzahl der Attribute die das Objekt speichern
     *            koennen soll
     */
    public MVRelationalTuple(SDFSchema schema) {
        super(schema);
    }

    /**
     * Erzeugt ein leeres Tuple ohne Schemainformationen
     * 
     * @param attributeCount
     *            Anzahl der Attribute des Tuples
     */
    public MVRelationalTuple(int attributeCount) {
        super(attributeCount);
    }

    public MVRelationalTuple(MVRelationalTuple<?> copy) {
        super(copy);
    }


}
