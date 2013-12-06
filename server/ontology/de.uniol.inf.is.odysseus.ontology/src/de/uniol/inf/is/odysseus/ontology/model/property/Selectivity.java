/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.ontology.model.property;

import java.net.URI;

import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.SSN;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Selectivity extends MeasurementProperty implements IMeasurementProperty {

    /**
     * Class constructor.
     * 
     * @param uri
     *            The URI
     * @param expression
     *            The expression
     */
    public Selectivity(final URI uri, final String expression) {
        super(uri, SSN.Selectivity, expression);
    }

}
