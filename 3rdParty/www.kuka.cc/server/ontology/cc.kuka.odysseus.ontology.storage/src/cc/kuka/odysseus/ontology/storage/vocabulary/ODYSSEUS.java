/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.ontology.storage.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
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
     * has Min Value:
     */
    public static final Property hasMinValue = ODYSSEUS.m_model.createProperty(ODYSSEUS.NS + "hasMinValue");
    /**
     * has Max Value:
     */
    public static final Property hasMaxValue = ODYSSEUS.m_model.createProperty(ODYSSEUS.NS + "hasMaxValue");
    /**
     * has Expression:
     */
    public static final Property hasExpression = ODYSSEUS.m_model.createProperty(ODYSSEUS.NS + "hasExpression");

    private ODYSSEUS() {
    }
}
