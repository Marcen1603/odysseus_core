/*******************************************************************************
 * Copyright 2016 Georg Berendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.server.opcua.wrappers;

import java.util.LinkedHashMap;

import com.xafero.kitea.collections.Observables;
import com.xafero.kitea.collections.impl.ObservableMap;
import com.xafero.turjumaan.server.java.api.Description;
import com.xafero.turjumaan.server.java.api.Embed;
import com.xafero.turjumaan.server.java.api.Format;
import com.xafero.turjumaan.server.java.api.NotCacheable;
import com.xafero.turjumaan.server.java.api.ResponseFormat;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.server.opcua.access.OPCUATransportHandler;

/**
 * One sink of Odysseus.
 */
@Description("A sink from Odysseus")
public class Sink {

	/** The handler. */
	private final OPCUATransportHandler<?> handler;

	/** The values. */
	private final ObservableMap<String, DataItem> values;

	/**
	 * Instantiates a new sink.
	 *
	 * @param handler
	 *            the handler
	 */
	public Sink(OPCUATransportHandler<?> handler) {
		this.handler = handler;
		this.values = Observables.decorate(new LinkedHashMap<String, DataItem>());
	}

	/**
	 * Gets the options.
	 *
	 * @return the options
	 */
	@NotCacheable
	@ResponseFormat(Format.JSON)
	@Description("The options for this sink")
	public OptionMap getOptions() {
		return handler.getOptionsMap();
	}

	/**
	 * Gets the pattern.
	 *
	 * @return the pattern
	 */
	@NotCacheable
	@Description("The exchange pattern of this sink")
	public ITransportExchangePattern getPattern() {
		return handler.getExchangePattern();
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@NotCacheable
	@Description("The name of the handler")
	public String getName() {
		return handler.getName();
	}

	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	@Embed
	@Description("Latest received values")
	public ObservableMap<String, DataItem> getValues() {
		return values;
	}
}