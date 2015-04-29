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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
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
public class DateHandler extends AbstractDataHandler<Date> {
	static protected List<String> types = new ArrayList<>();
	static{
		types.add(SDFDatatype.DATE.getURI());
	}

	@Override
	public IDataHandler<Date> getInstance(SDFSchema schema) {
		return new DateHandler();
	}
	
	@Override
	public Date readData(ByteBuffer buffer) {
		long l = buffer.getLong();
		//System.out.println("read Long Data: "+l);
		return new Date(l);
	}
	
	@Override
	public Date readData(String string) {
        if ((string == null) || ("null".equalsIgnoreCase(string))) {
            return null;
        }
		return new Date(Long.parseLong(string));
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		//System.out.println("write Long Data "+((Number)data).longValue());
		buffer.putLong(((Date)data).getTime());
		//buffer.putLong(((Number)data).longValue());
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
		return Date.class;
	}
	
	
}
