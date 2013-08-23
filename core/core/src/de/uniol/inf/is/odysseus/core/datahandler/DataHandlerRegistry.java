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

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Andr� Bolles, Marco Grawunder
 * 
 */
public class DataHandlerRegistry {

	static Logger logger = LoggerFactory.getLogger(DataHandlerRegistry.class);

	public DataHandlerRegistry() {
		logger.debug("Created new DataHandler registry");
	}

	/**
	 * HashMap from datatype to data handler
	 */
	private static HashMap<String, IDataHandler<?>> dataHandlers = new HashMap<String, IDataHandler<?>>();

	public static void registerDataHandler(IDataHandler<?> handler) {
		String errMsg = "";
		logger.debug("Register DataHandler " + handler + " for Datatypes "
				+ handler.getSupportedDataTypes());
		for (String type : handler.getSupportedDataTypes()) {
			if (dataHandlers.containsKey(type.toLowerCase())) {
				errMsg += "Data handler for " + type + " already registered.\n";
			} else {
				dataHandlers.put(type.toLowerCase(), handler);
			}
		}

		if (errMsg != "") {
			throw new IllegalArgumentException(errMsg);
		}
	}

	public static void removeDataHandler(IDataHandler<?> handler) {
		for (String type : handler.getSupportedDataTypes()) {
			if (dataHandlers.containsKey(type.toLowerCase())) {
				dataHandlers.remove(type.toLowerCase());
			}
		}
	}

	public static IDataHandler<?> getDataHandler(String dataType,
			SDFSchema schema) {
		IDataHandler<?> ret = dataHandlers.get(dataType.toLowerCase());
		if (ret != null) {
			ret = ret.createInstance(schema);
		}
		return ret;
	}

	public static boolean containsDataHandler(String dataType) {
		return dataHandlers.containsKey(dataType.toLowerCase());
	}

	public static IDataHandler<?> getDataHandler(String dataType,
			List<String> schema) {
		IDataHandler<?> ret = dataHandlers.get(dataType.toLowerCase());
		if (ret != null) {
			ret = ret.createInstance(schema);
		}
		return ret;
	}
	
	public static ImmutableList<String> getHandlerNames() {
		return ImmutableList.copyOf(dataHandlers.keySet());
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Class<? extends IStreamObject> getCreatedType(String dhandlerText) {
		Class<? extends IStreamObject> type = null;
		if (dhandlerText != null){
			IDataHandler<?> dh = getDataHandler(dhandlerText, (SDFSchema)null);
			try {
					type =  (Class<? extends IStreamObject>) dh.createsType();
			} catch (ClassCastException e) {
				throw new IllegalArgumentException(dhandlerText+" cannot be used as data handler!");
			}
		}		
		return type;
	}
	
	/**
	 * This method should only be used, if the data handler are used in a non OSGi enviroment!
	 * In other cases there should be declarative services that register new data handler!!
	 */
	public static void initDefaultHandler(){
		registerDataHandler(new BooleanHandler());
		registerDataHandler(new ByteDataHandler());
		registerDataHandler(new ShortDataHandler());
		registerDataHandler(new DateHandler());
		registerDataHandler(new DoubleHandler());
		registerDataHandler(new IntegerHandler());
		registerDataHandler(new ListDataHandler());
		registerDataHandler(new LongHandler());
		registerDataHandler(new ObjectDataHandler<>());
		registerDataHandler(new StringHandler());
		registerDataHandler(new TupleDataHandler());
	}
}
