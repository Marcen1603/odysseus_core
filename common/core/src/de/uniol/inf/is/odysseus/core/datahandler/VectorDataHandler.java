/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.datahandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.primitives.Doubles;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class VectorDataHandler extends AbstractDataHandler<double[]> {
    static protected List<String> types = new ArrayList<String>();
    static {
        types.add(SDFDatatype.VECTOR_DOUBLE.getURI());
    }
    private IDataHandler<Double> handler = new DoubleHandler();

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] readData(ByteBuffer buffer) {
        int size = buffer.getInt();
        double[] value = new double[size];
        for (int i = 0; i < size; i++) {
            value[i] = this.handler.readData(buffer);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] readData(Iterator<String> input) {
    	List<Double> list = new ArrayList<Double>();
        while (input.hasNext()) {
            list.add(this.handler.readData(input.next()));
        }
        return Doubles.toArray(list);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public double[] readData(String string) {
        if (string != null && string.length() > 0) {
            String[] lines = string.split(",");
            int size = lines.length;
            double[] value = new double[size];
            for (int i = 0; i < size; i++) {
                value[i] = this.handler.readData(lines[i]);
            }
            return value;
        }
        else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeData(ByteBuffer buffer, Object data) {
        double[] value = (double[]) data;
        buffer.putInt(value.length);
        for (double v : value) {
            this.handler.writeData(buffer, v);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int memSize(Object data) {
        int size = 0;
        double[] value = (double[]) data;
        for (double v : value) {
            size += this.handler.memSize(v);
        }
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> createsType() {
        return double[].class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected IDataHandler<double[]> getInstance(SDFSchema schema) {
        return new VectorDataHandler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getSupportedDataTypes() {
        return types;
    }
}
