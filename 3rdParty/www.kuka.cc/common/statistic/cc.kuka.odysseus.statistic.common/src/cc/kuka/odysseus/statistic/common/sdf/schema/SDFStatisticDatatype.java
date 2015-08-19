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
package cc.kuka.odysseus.statistic.common.sdf.schema;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SDFStatisticDatatype extends SDFDatatype {

    /**
     *
     */
    private static final long serialVersionUID = 3779876729373723271L;

    /**
     * Constructs a new statistic data type from the given URI.
     *
     * @param uri
     *            The URI
     */
    public SDFStatisticDatatype(final String uri) {
        super(uri, true);
    }

    /**
     * Constructs a new statistic data type from the given data type.
     *
     * @param sdfDatatype
     *            The data type
     */
    public SDFStatisticDatatype(final SDFDatatype sdfDatatype) {
        super(sdfDatatype);
    }

    /**
     * Constructs a new statistic data type with the given name, type, and
     * schema.
     *
     * @param name
     *            The data type name
     * @param type
     *            The type
     * @param schema
     *            The schema
     */
    public SDFStatisticDatatype(final String name, final KindOfDatatype type, final SDFSchema schema) {
        super(name, type, schema, true);
    }

    /**
     * Constructs a new statistic data type with the given name, type, and
     * sub type.
     *
     * @param name
     *            The data type name
     * @param type
     *            The type
     * @param subType
     *            The sub type
     */
    public SDFStatisticDatatype(final String name, final KindOfDatatype type, final SDFDatatype subType) {
        super(name, type, subType, true);
    }

    /** Complex. */
    public static final SDFDatatype POPULATIONSKEWNESS_PARTIAL_AGGREGATE = new SDFDatatype("PopulationSkewnessPartialAggregate");
    public static final SDFDatatype SAMPLESKEWNESS_PARTIAL_AGGREGATE = new SDFDatatype("SampleSkewnessPartialAggregate");
    public static final SDFDatatype POPULATIONKURTOSIS_PARTIAL_AGGREGATE = new SDFDatatype("PopulationKurtosisPartialAggregate");
    public static final SDFDatatype SAMPLEKURTOSIS_PARTIAL_AGGREGATE = new SDFDatatype("SampleKurtosisPartialAggregate");
    public static final SDFDatatype JARQUEBERATEST_PARTIAL_AGGREGATE = new SDFDatatype("JarqueBeraTestPartialAggregate");
    public static final SDFDatatype DAGOSTINOPEARSONOMNIBUSTEST_PARTIAL_AGGREGATE = new SDFDatatype("DAgostinoPearsonOmnibusTestPartialAggregate");
    public static final SDFDatatype POPULATIONSTANDARDDEVIATION_PARTIAL_AGGREGATE = new SDFDatatype("PopulationStandardDeviationPartialAggregate");
    public static final SDFDatatype SAMPLESTANDARDDEVIATION_PARTIAL_AGGREGATE = new SDFDatatype("SampleStandardDeviationPartialAggregate");

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
        return ((super.isPartialAggregate()) || (this.getURI().equals(SDFStatisticDatatype.POPULATIONSKEWNESS_PARTIAL_AGGREGATE.getURI()))
                || (this.getURI().equals(SDFStatisticDatatype.SAMPLESKEWNESS_PARTIAL_AGGREGATE.getURI())) || (this.getURI().equals(SDFStatisticDatatype.POPULATIONKURTOSIS_PARTIAL_AGGREGATE.getURI()))
                || (this.getURI().equals(SDFStatisticDatatype.SAMPLEKURTOSIS_PARTIAL_AGGREGATE.getURI())) || (this.getURI().equals(SDFStatisticDatatype.JARQUEBERATEST_PARTIAL_AGGREGATE.getURI()))
                || (this.getURI().equals(SDFStatisticDatatype.DAGOSTINOPEARSONOMNIBUSTEST_PARTIAL_AGGREGATE.getURI()))
                || (this.getURI().equals(SDFStatisticDatatype.POPULATIONSTANDARDDEVIATION_PARTIAL_AGGREGATE.getURI())) || (this.getURI()
                        .equals(SDFStatisticDatatype.SAMPLESTANDARDDEVIATION_PARTIAL_AGGREGATE.getURI())));
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
        types.add(SDFStatisticDatatype.POPULATIONSKEWNESS_PARTIAL_AGGREGATE);
        types.add(SDFStatisticDatatype.SAMPLESKEWNESS_PARTIAL_AGGREGATE);
        types.add(SDFStatisticDatatype.POPULATIONKURTOSIS_PARTIAL_AGGREGATE);
        types.add(SDFStatisticDatatype.SAMPLEKURTOSIS_PARTIAL_AGGREGATE);
        types.add(SDFStatisticDatatype.JARQUEBERATEST_PARTIAL_AGGREGATE);
        types.add(SDFStatisticDatatype.DAGOSTINOPEARSONOMNIBUSTEST_PARTIAL_AGGREGATE);
        types.add(SDFStatisticDatatype.POPULATIONSTANDARDDEVIATION_PARTIAL_AGGREGATE);
        types.add(SDFStatisticDatatype.SAMPLESTANDARDDEVIATION_PARTIAL_AGGREGATE);
        return types;
    }

}
