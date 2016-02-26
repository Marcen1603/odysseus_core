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
package cc.kuka.odysseus.polynomial.datahandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import cc.kuka.odysseus.polynomial.datatype.Polynomial;
import cc.kuka.odysseus.polynomial.sdf.schema.SDFPolynomialDatatype;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.DoubleHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class PolynomialDataHandler extends AbstractDataHandler<Polynomial> {
    static protected List<String> types = new ArrayList<>();
    static {
        PolynomialDataHandler.types.add(SDFPolynomialDatatype.POLYNOMIAL.getURI());
    }
    private final IDataHandler<Double> doubleHandler = new DoubleHandler();

    /**
     *
     * Class constructor.
     *
     */
    public PolynomialDataHandler() {
        super();

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IDataHandler<Polynomial> getInstance(final SDFSchema schema) {
        return new PolynomialDataHandler();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Polynomial readData(final String string) {
        if ((string != null) && (string.length() > 0)) {
            final String[] coefficientsArray = string.split(";");
            final int size = coefficientsArray.length;
            final double[] coefficients = new double[size];
            for (int i = 0; i < size; i++) {
                coefficients[i] = this.doubleHandler.readData(coefficientsArray[i]).doubleValue();
            }
            return new Polynomial(coefficients);
        }
        return null;

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Polynomial readData(final ByteBuffer buffer) {
        final double[] coefficients = new double[buffer.getInt()];
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = this.doubleHandler.readData(buffer).doubleValue();
        }
        return new Polynomial(coefficients);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        final Polynomial value = (Polynomial) data;
        buffer.putInt(value.degree() + 1);
        for (int i = 0; i <= value.degree(); i++) {
            this.doubleHandler.writeData(buffer, new Double(value.coefficient(i)));
        }

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    final public List<String> getSupportedDataTypes() {
        return PolynomialDataHandler.types;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int memSize(final Object data) {
        final Polynomial value = (Polynomial) data;
        return (Integer.SIZE + ((value.degree() + 1) * Double.SIZE)) / 8;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Class<?> createsType() {
        return Polynomial.class;
    }

}
