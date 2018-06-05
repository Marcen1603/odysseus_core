/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
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

package cc.kuka.odysseus.signal.common.sdf.schema;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SDFSignalDatatype extends SDFDatatype {

    /**
     *
     */
    private static final long serialVersionUID = -5730367361215517862L;

    /**
     * Constructs a new signal data type from the given URI.
     *
     * @param uri
     *            The URI
     */
    public SDFSignalDatatype(final String uri) {
        super(uri, true);
    }

    /**
     * Constructs a new signal data type from the given data type.
     *
     * @param sdfDatatype
     *            The data type
     */
    public SDFSignalDatatype(final SDFDatatype sdfDatatype) {
        super(sdfDatatype);
    }

    /**
     * Constructs a new signal data type with the given name, type, and
     * schema.
     *
     * @param name
     *            The data type name
     * @param type
     *            The type
     * @param schema
     *            The schema
     */
    public SDFSignalDatatype(final String name, final KindOfDatatype type, final SDFSchema schema) {
        super(name, type, schema, true);
    }

    /**
     * Constructs a new signal data type with the given name, type, and
     * sub type.
     *
     * @param name
     *            The data type name
     * @param type
     *            The type
     * @param subType
     *            The sub type
     */
    public SDFSignalDatatype(final String name, final KindOfDatatype type, final SDFDatatype subType) {
        super(name, type, subType, true);
    }

    /** Complex. */
    public static final SDFDatatype COMPLEX = new SDFSignalDatatype("Complex");
    public static final SDFDatatype LIST_COMPLEX = new SDFDatatype("List_Complex", SDFDatatype.KindOfDatatype.LIST, SDFSignalDatatype.COMPLEX);
    public static final SDFDatatype DTW_PARTIAL_AGGREGATE = new SDFDatatype("DTWPartialAggregate");
    public static final SDFDatatype FFT_PARTIAL_AGGREGATE = new SDFDatatype("FFTPartialAggregate");
    public static final SDFDatatype SPECTRAL_CENTROID_PARTIAL_AGGREGATE = new SDFDatatype("SpectralCentroidPartialAggregate");

    public static final SDFDatatype[] types = new SDFDatatype[] { SDFSignalDatatype.COMPLEX, SDFSignalDatatype.LIST_COMPLEX, SDFSignalDatatype.DTW_PARTIAL_AGGREGATE,
            SDFSignalDatatype.FFT_PARTIAL_AGGREGATE, SDFSignalDatatype.SPECTRAL_CENTROID_PARTIAL_AGGREGATE };

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final boolean isNumeric() {
        return false;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isPartialAggregate() {
        return ((super.isPartialAggregate()) || (this.getURI().equals(SDFSignalDatatype.DTW_PARTIAL_AGGREGATE.getURI())) || (this.getURI().equals(SDFSignalDatatype.FFT_PARTIAL_AGGREGATE.getURI())) || (this
                .getURI().equals(SDFSignalDatatype.SPECTRAL_CENTROID_PARTIAL_AGGREGATE.getURI())));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final boolean compatibleTo(final SDFDatatype other) {
        if (super.compatibleTo(other)) {
            return true;
        }
        if (other instanceof SDFSignalDatatype) {
            if ((this.getURI().equals(SDFSignalDatatype.COMPLEX.getURI())) && (other.getURI().equals(SDFSignalDatatype.COMPLEX.getURI()))) {
                return true;
            }
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
        types.add(SDFSignalDatatype.COMPLEX);
        types.add(SDFSignalDatatype.LIST_COMPLEX);
        types.add(SDFSignalDatatype.DTW_PARTIAL_AGGREGATE);
        types.add(SDFSignalDatatype.FFT_PARTIAL_AGGREGATE);
        types.add(SDFSignalDatatype.SPECTRAL_CENTROID_PARTIAL_AGGREGATE);
        return types;
    }

}
