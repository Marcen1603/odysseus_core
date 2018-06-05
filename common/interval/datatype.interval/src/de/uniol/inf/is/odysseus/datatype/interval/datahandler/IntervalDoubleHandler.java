/*
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.datatype.interval.datahandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.datatype.interval.datatype.IntervalDouble;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class IntervalDoubleHandler extends AbstractDataHandler<IntervalDouble> {
	static protected List<String> types = new ArrayList<String>();
	static {
		IntervalDoubleHandler.types.add("IntervalDouble");
	}

	@Override
	public IDataHandler<IntervalDouble> getInstance(final SDFSchema schema) {
		return new IntervalDoubleHandler();
	}

	public IntervalDoubleHandler() {
		super(null);
	}

	@Override
	public IntervalDouble readData(final String string) {
		final String[] values = string.split(":");
		return new IntervalDouble(Double.parseDouble(values[0]),
				Double.parseDouble(values[1]));
	}

	@Override
	public IntervalDouble readData(final ByteBuffer buffer) {
		return new IntervalDouble(buffer.getDouble(), buffer.getDouble());
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		final IntervalDouble value = (IntervalDouble) data;
		buffer.putDouble(value.inf());
		buffer.putDouble(value.sup());
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return IntervalDoubleHandler.types;
	}

	@Override
	public int memSize(final Object attribute) {
		return (2 * Double.SIZE) / 8;
	}
	
	@Override
	public Class<?> createsType() {
		return IntervalDouble.class;
	}

}
