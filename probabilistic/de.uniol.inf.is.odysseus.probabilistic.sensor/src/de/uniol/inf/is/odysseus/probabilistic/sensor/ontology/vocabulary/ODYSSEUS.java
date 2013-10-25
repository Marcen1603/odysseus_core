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
package de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public final class ODYSSEUS {
    /**
     * <p>
     * The RDF model that holds the vocabulary terms
     * </p>
     */
    private static Model m_model = ModelFactory.createDefaultModel();

    /**
     * <p>
     * The namespace of the vocabulary as a string ({@value})
     * </p>
     */
    public static final String NS = "http://odysseus.uni-oldenburg.de/odysseus#";

    /**
     * <p>
     * The namespace of the vocabulary as a string
     * </p>
     * 
     * @see #NS
     */
    public static String getURI() {
        return ODYSSEUS.NS;
    }

    /**
     * <p>
     * The namespace of the vocabulary as a resource
     * </p>
     */
    public static final Resource NAMESPACE = ODYSSEUS.m_model.createResource(ODYSSEUS.NS);

    // Vocabulary properties
    // /////////////////////////
    /**
     * has Measurement Property Min Value:
     */
    public static final Property hasMeasurementPropertyMinValue = ODYSSEUS.m_model.createProperty(ODYSSEUS.NS + "hasMeasurementPropertyMinValue");
    /**
     * has Measurement Property Max Value:
     */
    public static final Property hasMeasurementPropertyMaxValue = ODYSSEUS.m_model.createProperty(ODYSSEUS.NS + "hasMeasurementPropertyMaxValue");

    private ODYSSEUS() {
    }
}
