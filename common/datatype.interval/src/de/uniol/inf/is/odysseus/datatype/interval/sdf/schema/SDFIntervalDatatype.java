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
package de.uniol.inf.is.odysseus.datatype.interval.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SDFIntervalDatatype extends SDFDatatype {
    /**
     *
     */
    private static final long serialVersionUID = -8647938282128195112L;

    public SDFIntervalDatatype(final String URI) {
        super(URI, true);
    }

    public SDFIntervalDatatype(final SDFDatatype sdfDatatype) {
        super(sdfDatatype);
    }

    public SDFIntervalDatatype(final String datatypeName, final KindOfDatatype type, final SDFSchema schema) {
        super(datatypeName, type, schema, true);
    }

    public SDFIntervalDatatype(final String datatypeName, final KindOfDatatype type, final SDFDatatype subType) {
        super(datatypeName, type, subType, true);
    }

    public static final SDFDatatype INTERVAL_DOUBLE = new SDFIntervalDatatype("Interval_Double", SDFDatatype.KindOfDatatype.GENERIC, SDFDatatype.DOUBLE);
    public static final SDFDatatype INTERVAL_FLOAT = new SDFIntervalDatatype("Interval_Float", SDFDatatype.KindOfDatatype.GENERIC, SDFDatatype.FLOAT);
    public static final SDFDatatype INTERVAL_LONG = new SDFIntervalDatatype("Interval_Long", SDFDatatype.KindOfDatatype.GENERIC, SDFDatatype.LONG);
    public static final SDFDatatype INTERVAL_INTEGER = new SDFIntervalDatatype("Interval_Integer", SDFDatatype.KindOfDatatype.GENERIC, SDFDatatype.INTEGER);
    public static final SDFDatatype INTERVAL_SHORT = new SDFIntervalDatatype("Interval_Short", SDFDatatype.KindOfDatatype.GENERIC, SDFDatatype.SHORT);
    public static final SDFDatatype INTERVAL_BYTE = new SDFIntervalDatatype("Interval_Byte", SDFDatatype.KindOfDatatype.GENERIC, SDFDatatype.BYTE);
    public static final SDFDatatype[] TYPES = new SDFDatatype[] { INTERVAL_DOUBLE, INTERVAL_FLOAT, INTERVAL_LONG, INTERVAL_INTEGER, INTERVAL_SHORT, INTERVAL_BYTE };

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    public boolean isDouble() {
        return this.getURI().equals(SDFIntervalDatatype.INTERVAL_DOUBLE.getURI());
    }

    @Override
    public boolean isInteger() {
        return this.getURI().equals(SDFIntervalDatatype.INTERVAL_INTEGER.getURI());
    }

    @Override
    public boolean isLong() {
        return this.getURI().equals(SDFIntervalDatatype.INTERVAL_LONG.getURI());
    }

    @Override
    public boolean isFloat() {
        return this.getURI().equals(SDFIntervalDatatype.INTERVAL_FLOAT.getURI());
    }

    @Override
    public boolean isShort() {
        return this.getURI().equals(SDFIntervalDatatype.INTERVAL_SHORT.getURI());
    }

    @Override
    public boolean isByte() {
        return this.getURI().equals(SDFIntervalDatatype.INTERVAL_BYTE.getURI());
    }

    @Override
    public boolean compatibleTo(final SDFDatatype other) {
        if (other instanceof SDFIntervalDatatype) {
            return true;
        }
        return super.compatibleTo(other);
    }

}
