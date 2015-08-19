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
package cc.kuka.odysseus.statistic.common;

import java.util.ArrayList;
import java.util.List;

import cc.kuka.odysseus.statistic.common.sdf.schema.SDFStatisticDatatype;
import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class StatisticDatatypeProvider implements IDatatypeProvider {
    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<SDFDatatype> getDatatypes() {
        final List<SDFDatatype> ret = new ArrayList<>();
        ret.add(SDFStatisticDatatype.DAGOSTINOPEARSONOMNIBUSTEST_PARTIAL_AGGREGATE);
        ret.add(SDFStatisticDatatype.JARQUEBERATEST_PARTIAL_AGGREGATE);
        ret.add(SDFStatisticDatatype.POPULATIONKURTOSIS_PARTIAL_AGGREGATE);
        ret.add(SDFStatisticDatatype.POPULATIONSKEWNESS_PARTIAL_AGGREGATE);
        ret.add(SDFStatisticDatatype.POPULATIONSTANDARDDEVIATION_PARTIAL_AGGREGATE);
        ret.add(SDFStatisticDatatype.SAMPLEKURTOSIS_PARTIAL_AGGREGATE);
        ret.add(SDFStatisticDatatype.SAMPLESKEWNESS_PARTIAL_AGGREGATE);
        ret.add(SDFStatisticDatatype.SAMPLESTANDARDDEVIATION_PARTIAL_AGGREGATE);

        return ret;
    }
}
