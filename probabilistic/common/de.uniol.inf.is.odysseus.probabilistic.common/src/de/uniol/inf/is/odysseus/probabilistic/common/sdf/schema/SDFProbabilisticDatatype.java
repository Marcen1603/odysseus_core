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
package de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * Probabilistic SDF data types.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class SDFProbabilisticDatatype extends SDFDatatype {
    /**
	 * 
	 */
    private static final long serialVersionUID = 2867228296513432602L;

    /**
     * Constructs a new probabilistic data type from the given URI.
     * 
     * @param uri
     *            The URI
     */
    public SDFProbabilisticDatatype(final String uri) {
        super(uri, true);
    }

    /**
     * Constructs a new probabilistic data type from the given data type.
     * 
     * @param sdfDatatype
     *            The data type
     */
    public SDFProbabilisticDatatype(final SDFDatatype sdfDatatype) {
        super(sdfDatatype);
    }

    /**
     * Constructs a new probabilistic data type with the given name, type, and
     * schema.
     * 
     * @param name
     *            The data type name
     * @param type
     *            The type
     * @param schema
     *            The schema
     */
    public SDFProbabilisticDatatype(final String name, final KindOfDatatype type, final SDFSchema schema) {
        super(name, type, schema, true);
    }

    /**
     * Constructs a new probabilistic data type with the given name, type, and
     * sub type.
     * 
     * @param name
     *            The data type name
     * @param type
     *            The type
     * @param subType
     *            The sub type
     */
    public SDFProbabilisticDatatype(final String name, final KindOfDatatype type, final SDFDatatype subType) {
        super(name, type, subType, true);
    }

    /** Probabilistic tuple. */
    public static final SDFDatatype PROBABILISTIC_TUPLE = new SDFProbabilisticDatatype("ProbabilisticTuple");
    /** Probabilistic double datatype. */
    public static final SDFDatatype PROBABILISTIC_DOUBLE = new SDFProbabilisticDatatype("ProbabilisticDouble");
    /** Probabilistic boolean datatype used for predicates. */
    public static final SDFDatatype PROBABILISTIC_RESULT = new SDFProbabilisticDatatype("ProbabilisticResult");

    /** Probabilistic result used in probabilistic continuous predicates. */
    // public static final SDFDatatype PROBABILISTIC_CONTINUOUS_PREDICATE_RESULT
    // = new SDFProbabilisticDatatype("ProbabilisticContinuousPredicateResult");

    /** Probabilistic continuous double vector datatype. */
    public static final SDFDatatype VECTOR_PROBABILISTIC_DOUBLE = new SDFProbabilisticDatatype("VectorProbabilisticDouble");

    /** Probabilistic numbers. */
    public static final SDFDatatype[] PROBABILISTIC_NUMBERS = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE };

    /**
     * Checks whether the data type is a probabilistic data type.
     * 
     * @return <code>true</code> iff the data type is either discrete
     *         probabilistic or continuous probabilistic
     */
    public final boolean isProbabilistic() {
        return this.getURI().equalsIgnoreCase(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI());
    }

    /**
     * Checks whether the data type is a numeric data type.
     * 
     * @return <code>true</code> if the data type is numeric
     */
    @Override
    public final boolean isNumeric() {
        return false;
        // return (super.isNumeric() ||
        // this.getURI().equals(PROBABILISTIC_LONG.getURI()) ||
        // this.getURI().equals(PROBABILISTIC_INTEGER.getURI()) ||
        // this.getURI().equals(PROBABILISTIC_DOUBLE.getURI()) ||
        // this.getURI().equals(PROBABILISTIC_FLOAT.getURI())
        // || this.getURI().equals(PROBABILISTIC_SHORT.getURI()) ||
        // this.getURI().equals(PROBABILISTIC_BYTE.getURI()) ||
        // this.getURI().equals(PROBABILISTIC_CONTINUOUS_LONG.getURI()) ||
        // this.getURI().equals(PROBABILISTIC_CONTINUOUS_INTEGER.getURI())
        // || this.getURI().equals(PROBABILISTIC_CONTINUOUS_DOUBLE.getURI()) ||
        // this.getURI().equals(PROBABILISTIC_CONTINUOUS_FLOAT.getURI()) ||
        // this.getURI().equals(PROBABILISTIC_CONTINUOUS_SHORT.getURI()) ||
        // this.getURI().equals(PROBABILISTIC_CONTINUOUS_BYTE.getURI()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isDouble()
     */
    @Override
    public final boolean isDouble() {
        return super.isDouble() || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#compatibleTo(de.
     * uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype)
     */
    @Override
    public final boolean compatibleTo(final SDFDatatype other) {
        if (super.compatibleTo(other)) {
            return true;
        }
        if (other instanceof SDFProbabilisticDatatype) {
            return true;
        }
        return false;
    }

    /**
     * Gets the list of available data types.
     * 
     * @return List of datatypes
     */
    public static List<SDFDatatype> getTypes() {
        final List<SDFDatatype> types = new ArrayList<>();
        types.addAll(SDFDatatype.getTypes());
        types.add(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE);
        types.add(SDFProbabilisticDatatype.PROBABILISTIC_RESULT);
        types.add(SDFProbabilisticDatatype.VECTOR_PROBABILISTIC_DOUBLE);

        return types;
    }
}
