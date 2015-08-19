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
public class IntervalIntegerHandler extends AbstractDataHandler<Interval<Integer>> {
    static protected List<String> types = new ArrayList<>();
    static {
        IntervalIntegerHandler.types.add(SDFIntervalDatatype.INTERVAL_INTEGER.getURI());
    }

    public IntervalIntegerHandler() {
        super();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IDataHandler<Interval<Integer>> getInstance(final SDFSchema schema) {
        return new IntervalIntegerHandler();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Interval<Integer> readData(final String string) {
        final String[] values = string.split(":");
        return new Interval<>(new Integer(Integer.parseInt(values[0])), new Integer(Integer.parseInt(values[1])));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Interval<Integer> readData(final ByteBuffer buffer) {
        return new Interval<>(new Integer(buffer.getInt()), new Integer(buffer.getInt()));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        @SuppressWarnings("unchecked")
        final Interval<Integer> value = (Interval<Integer>) data;
        buffer.putInt(value.inf().intValue());
        buffer.putInt(value.sup().intValue());
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    final public List<String> getSupportedDataTypes() {
        return IntervalIntegerHandler.types;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int memSize(final Object attribute) {
        return (2 * Integer.SIZE) / 8;
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
