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
package cc.kuka.odysseus.polynomial.sdf.schema;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SDFPolynomialDatatype extends SDFDatatype {

    /**
     *
     */
    private static final long serialVersionUID = -8315292659308635783L;

    public static final SDFDatatype POLYNOMIAL = new SDFPolynomialDatatype("Polynomial");
    public static final SDFDatatype POLYNOMIALREGRESSION_PARTIAL_AGGREGATE = new SDFDatatype("PolynomialRegressionPartialAggregate");

    /**
     *
     * Class constructor.
     *
     * @param URI
     */
    public SDFPolynomialDatatype(final String URI) {
        super(URI, true);
    }

    /**
     *
     * Class constructor.
     *
     * @param sdfDatatype
     */
    public SDFPolynomialDatatype(final SDFDatatype sdfDatatype) {
        super(sdfDatatype);
    }

    /**
     *
     * Class constructor.
     *
     * @param datatypeName
     * @param type
     * @param schema
     */
    public SDFPolynomialDatatype(final String datatypeName, final KindOfDatatype type, final SDFSchema schema) {
        super(datatypeName, type, schema, true);
    }

    /**
     *
     * Class constructor.
     *
     * @param datatypeName
     * @param type
     * @param subType
     */
    public SDFPolynomialDatatype(final String datatypeName, final KindOfDatatype type, final SDFDatatype subType) {
        super(datatypeName, type, subType, true);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isNumeric() {
        return false;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean isPartialAggregate() {
        return ((super.isPartialAggregate()) || (this.getURI().equals(POLYNOMIALREGRESSION_PARTIAL_AGGREGATE.getURI())));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean compatibleTo(final SDFDatatype other) {
        if (other instanceof SDFPolynomialDatatype) {
            return true;
        }
        return super.compatibleTo(other);
    }

    /**
     * Gets the list of available data types.
     *
     * @return List of datatypes
     */
    public static List<SDFDatatype> getTypes() {
        final List<SDFDatatype> types = new ArrayList<>();
        types.addAll(SDFDatatype.getTypes());
        types.add(POLYNOMIAL);
        types.add(POLYNOMIALREGRESSION_PARTIAL_AGGREGATE);
        return types;
    }
}
