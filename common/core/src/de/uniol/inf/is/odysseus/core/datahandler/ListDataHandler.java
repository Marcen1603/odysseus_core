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
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.ConversionOptions;
import de.uniol.inf.is.odysseus.core.conversion.CSVParser;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * @author Andr� Bolles
 *
 */
public class ListDataHandler extends AbstractDataHandler<List<?>> {

	protected static Logger LOG = LoggerFactory.getLogger(ListDataHandler.class);

	static protected List<String> types = new ArrayList<String>();

	static {
		types.add("MULTI_VALUE"); // ??
		for (SDFDatatype d : SDFDatatype.getLists()) {
			if(!d.equals(SDFDatatype.LIST_BYTE_BASE64)) {
				// there exists a special data handler for that, which handles the base64 encoding
				types.add(d.getURI());
			}
		}
	}

	IDataHandler<?> handler = null;
	final private boolean nullMode;

	public ListDataHandler() {
		// Needed for declarative service!
		nullMode = false;
	}

	public ListDataHandler(SDFSchema subType) {

		if (subType.size() < 1){
			throw new IllegalArgumentException("Subtype for List data handler not correctly set");
		}

		SDFDatatype listType = subType.getAttribute(0).getDatatype().getSubType();

		if (listType == null){
			throw new IllegalArgumentException("Missing subtype for List data handler");
		}

		if (listType.isTuple()) {

			if (listType.getSchema() != null) {
				this.handler = getDataHandler(SDFDatatype.TUPLE, listType.getSchema());
			}else if (subType.getAttribute(0).getDatatype().getSchema() != null){
				this.handler = getDataHandler(SDFDatatype.TUPLE, subType.getAttribute(0).getDatatype().getSchema());
			}else{
				throw new IllegalArgumentException("ListDataHandler cannot be initialized with TUPLE without schema");
			}

		} else if (listType.isListValue()) {
			// Create subtypeschema
			if (listType.getSubType() != null){
				List<SDFAttribute> attribs = new ArrayList<>();
				SDFDatatype type = new SDFDatatype("",SDFDatatype.KindOfDatatype.LIST,listType.getSubType());
				attribs.add(new SDFAttribute("", "", type));

				SDFSchema newSubSchema = SDFSchemaFactory.createNewTupleSchema("__", attribs);

				this.handler = getDataHandler(SDFDatatype.LIST, newSubSchema);
			}else{
				throw new IllegalArgumentException("ListDataHandler cannot be initialized with LIST without a subtype");
			}

		} else {
			// Basis type
			this.handler = getDataHandler(listType, subType);
		}

//		// hmmm ... this should be more generic !!
//		if (subType.getAttribute(0).getDatatype().isTuple()
//				|| subType.getAttribute(0).getDatatype().getSubType().isTuple()) {
//			this.handler = DataHandlerRegistry.getDataHandler("TUPLE",
//					subType.getAttribute(0).getDatatype().getSchema());
//		} else {
//			this.handler = DataHandlerRegistry.getDataHandler(subType.getAttribute(0).getAttributeName(), subType);
//		}
//
//		// Is needed for handling of KeyValueObject
//		if (this.handler == null && subType.getAttribute(0).getDatatype().getSubType() != null) {
//			SDFDatatype localSubType = subType.getAttribute(0).getDatatype().getSubType();
//			if (localSubType.isTuple()) {
//				this.handler = DataHandlerRegistry.getDataHandler(SDFDatatype.TUPLE.toString(),
//						subType.getAttribute(0).getDatatype().getSchema());
//			} else {
//				this.handler = DataHandlerRegistry
//						.getDataHandler(subType.getAttribute(0).getDatatype().getSubType().toString(), subType);
//			}
//		}

		nullMode = false;
	}
	
	@Override
	public void setConversionOptions(ConversionOptions conversionOptions) {
		super.setConversionOptions(conversionOptions);
		if (this.handler != null) {
			this.handler.setConversionOptions(conversionOptions);
		}
	}

	@Override
	public IDataHandler<List<?>> getInstance(SDFSchema schema) {
		return new ListDataHandler(schema);
	}

	@Override
	public List<?> readData(String string) {
		if (string == null) {
			return null;
		}
		ArrayList<Object> returnValues = new ArrayList<Object>();

		String trimmedString = string.trim();

		if (trimmedString.startsWith("[") && trimmedString.endsWith("]")) {
			// there could be lists of lists so we need the special csv parser,
			// inside this list there
			// can be string with [] if they are enclosed in "
			List<String> vals = CSVParser.parseCSV(trimmedString.substring(1, trimmedString.length() - 1), '"', ',',
					true);
			for (String value : vals) {
				Object val = this.handler.readData(value);
				returnValues.add(val);
			}
		} else {
			String[] lines = string.split("\n");
			int size = lines.length;
			for (int i = 0; i < size; i++) {
				Object value = this.handler.readData(lines[i]);
				returnValues.add(value);
			}
		}
		// It could be that the list contains of one element with value null
		// this in an empty list and should be treated as empty list
		if (returnValues.size() == 1 && returnValues.get(0)==null){
			returnValues.clear();
		}
		return returnValues;
	}

	@Override
	public List<?> readData(Iterator<String> input) {
		ArrayList<Object> values = new ArrayList<Object>();
		while (input.hasNext()) {
			Object value = this.handler.readData(input.next());
			values.add(value);
		}
		return values;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler
	 * #readData(java.nio.ByteBuffer)
	 */
	@Override
	public List<?> readData(ByteBuffer buffer) {
		ArrayList<Object> values = new ArrayList<Object>();
		int size = buffer.getInt();
		for (int i = 0; i < size; i++) {
			byte type = -1;
			if (nullMode) {
				type = buffer.get();
			}
			if (!nullMode || type != 0) {
				Object value = this.handler.readData(buffer);
				values.add(value);
			} else {
				values.add(null);
			}
		}
		return values;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler
	 * #writeData(java.nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	@SuppressWarnings({ "rawtypes" })
	public void writeData(ByteBuffer buffer, Object data) {
		List values = (List) data;
		buffer.putInt(values.size());
		for (Object v : values) {
			if (nullMode) {
				if (v == null) {
					buffer.put((byte) 0);
				} else {
					buffer.put((byte) 1);
				}
			}
			if (!nullMode || (nullMode && v != null)) {
				this.handler.writeData(buffer, v);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.access.
	 * AbstractDataHandler#getSupportedDataTypes()
	 */
	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

	@Override
	public int memSize(Object data) {
		int size = 0;
		List<?> values = (List<?>) data;
		for (Object v : values) {
			size += this.handler.memSize(v);
		}
		// Marker for null or not null values
		if (nullMode) {
			size += values.size();
		}
		return size;
	}

	@Override
	public Class<?> createsType() {
		return List.class;
	}

}
