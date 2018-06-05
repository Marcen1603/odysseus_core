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
package de.uniol.inf.is.odysseus.generator.valuegenerator.spatial;

import java.util.Objects;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractMultiValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class WaypointGenerator extends AbstractMultiValueGenerator {
	private final int start;
	private int current;
	private final Double[][] waypoints;

	/**
	 * @param errorModel
	 */
	public WaypointGenerator(final IErrorModel errorModel,
			final Double[][] waypoints) {
		this(errorModel, waypoints, 0);
	}

	public WaypointGenerator(final IErrorModel errorModel,
			final Double[][] waypoints, final int start) {
		super(errorModel);
		Objects.requireNonNull(waypoints);
		this.start = start;
		this.waypoints = waypoints;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int dimension() {
		return 2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double[] generateValue() {
		if (current >= waypoints.length) {
			current = 0;
		}
		if ((waypoints.length == 0) && (waypoints[0].length != 2)) {
			return null;
		}
		final double[] value = new double[] { this.waypoints[current][0],
				this.waypoints[current][1] };
		this.current++;
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initGenerator() {
		this.current = this.start;
	}

}
