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
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class FloatDataHandler extends AbstractDataHandler<Float> {
    static protected List<String> types = new ArrayList<String>();
    static {
        types.add(SDFDatatype.FLOAT.getURI());
    }

    @Override
    public IDataHandler<Float> getInstance(SDFSchema schema) {
        return new FloatDataHandler();
    }

    public FloatDataHandler() {
        super(null);
    }

    @Override
    public Float readData(String string) {
        if (string != null && string.length() > 0) {
            return Float.parseFloat(string);
        }
        else {
            return null;
        }
    }

    @Override
    public Float readData(ByteBuffer buffer) {
        return buffer.getFloat();
    }

    @Override
    public void writeData(List<String> output, Object data) {
        output.add(((Number) data).toString());
    }

    @Override
    public void writeData(ByteBuffer buffer, Object data) {
        buffer.putFloat(((Number) data).floatValue());
    }

    @Override
    final public List<String> getSupportedDataTypes() {
        return types;
    }

    @Override
    public int memSize(Object attribute) {
        return Float.SIZE / 8;
    }

    @Override
    public Class<?> createsType() {
        return Float.class;
    }

}
