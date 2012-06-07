/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.spatial.grid.aggregation;

import com.googlecode.javacv.cpp.opencv_core;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.spatial.grid.model.CartesianGrid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GridPartialAggregate<T> implements IPartialAggregate<T> {

	private double count;
	private final CartesianGrid grid;

	public GridPartialAggregate(final CartesianGrid grid) {
		this.count = 1.0;
		this.grid = grid;
	}

	public GridPartialAggregate(
			final GridPartialAggregate<T> gridPartialAggregate) {
		this.grid = gridPartialAggregate.grid.clone();
		this.count = gridPartialAggregate.count;
	}

	@Override
	public GridPartialAggregate<T> clone() {
		return new GridPartialAggregate<T>(this);
	}

	public void evaluate() {
		opencv_core.cvConvertScale(this.grid.getImage(), this.grid.getImage(),
				1.0 / this.count, 0);
	}

	public CartesianGrid getGrid() {
		return this.grid;
	}

	public void merge(final CartesianGrid grid) {
		this.count++;
		opencv_core.cvAdd(this.grid.getImage(), grid.getImage(),
				this.grid.getImage(), null);
		grid.release();
	}

	@Override
	public String toString() {
		final StringBuffer ret = new StringBuffer("GridPartialAggregate (")
				.append(this.hashCode()).append(")").append(this.grid);
		return ret.toString();
	}

}
