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

import java.util.LinkedHashSet;
import java.util.Random;

import com.inductiveautomation.opcua.stack.core.types.builtin.DateTime;
import com.xafero.kitea.collections.Observables;
import com.xafero.kitea.collections.impl.ObservableSet;
import com.xafero.turjumaan.server.java.api.Description;
import com.xafero.turjumaan.server.java.api.Embed;

/**
 * The node containing a copy of all data variables.
 */
@Description("A place for some data")
public class Data {

	/** The random. */
	private final Random rnd;

	/** The sinks. */
	private final ObservableSet<Sink> sinks;

	/**
	 * Instantiates a new Data object.
	 */
	public Data() {
		rnd = new Random(DateTime.now().getUtcTime());
		sinks = Observables.decorate(new LinkedHashSet<Sink>());
	}

	/**
	 * Gets the random.
	 *
	 * @return the random
	 */
	@Embed
	@Description("A random number generator")
	public Random getRandom() {
		return rnd;
	}

	/**
	 * Gets the sinks.
	 *
	 * @return the sinks
	 */
	@Embed
	@Description("All data-producing sinks")
	public ObservableSet<Sink> getSinks() {
		return sinks;
	}
}