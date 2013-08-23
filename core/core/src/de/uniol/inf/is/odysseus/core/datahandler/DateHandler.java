/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


/**
 * Used for getting DATE attributes out of a streams.
 * Handles these attributes as simple long values, because of
 * faster processing.
 * 
 * @author Andre Bolles
 *
 */
public class DateHandler extends AbstractDataHandler<Long> {
	static protected List<String> types = new ArrayList<String>();
	static{
		types.add(SDFDatatype.DATE.getURI());
	}

	@Override
	public IDataHandler<Long> getInstance(SDFSchema schema) {
		return new DateHandler();
	}
	
	@Override
	public Long readData(ObjectInputStream inputStream) throws IOException {
		return inputStream.readLong();
	}
	
	@Override
	public Long readData(ByteBuffer buffer) {
		long l = buffer.getLong();
		//System.out.println("read Long Data: "+l);
		return l;
	}
	
	@Override
	public Long readData(String string) {
		return Long.parseLong(string);
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		//System.out.println("write Long Data "+((Number)data).longValue());
		buffer.putLong(((Number)data).longValue());
	}

	
	@Override
	final public List<String> getSupportedDataTypes() {
		return types;
	}
	
	@Override
	public int memSize(Object attribute) {
		return Long.SIZE / 8;
	}
	
	@Override
	public Class<?> createsType() {
		return Long.class;
	}
	
	
}
