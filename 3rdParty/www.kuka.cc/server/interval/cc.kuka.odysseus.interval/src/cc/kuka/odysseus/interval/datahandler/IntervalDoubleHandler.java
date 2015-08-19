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
package cc.kuka.odysseus.interval.datahandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import cc.kuka.odysseus.interval.datatype.Interval;
import cc.kuka.odysseus.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class IntervalDoubleHandler extends AbstractDataHandler<Interval<Double>> {
    static protected List<String> types = new ArrayList<>();
    static {
        IntervalDoubleHandler.types.add(SDFIntervalDatatype.INTERVAL_DOUBLE.getURI());
    }

    public IntervalDoubleHandler() {
        super();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IDataHandler<Interval<Double>> getInstance(final SDFSchema schema) {
        return new IntervalDoubleHandler();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Interval<Double> readData(final String string) {
        final String[] values = string.split(":");
        return new Interval<>(new Double(Double.parseDouble(values[0])), new Double(Double.parseDouble(values[1])));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Interval<Double> readData(final ByteBuffer buffer) {
        return new Interval<>(new Double(buffer.getDouble()), new Double(buffer.getDouble()));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        @SuppressWarnings("unchecked")
        final Interval<Double> value = (Interval<Double>) data;
        buffer.putDouble(value.inf().doubleValue());
        buffer.putDouble(value.sup().doubleValue());
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    final public List<String> getSupportedDataTypes() {
        return IntervalDoubleHandler.types;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int memSize(final Object attribute) {
        return (2 * Double.SIZE) / 8;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Class<?> createsType() {
        return Interval.class;
    }

}
