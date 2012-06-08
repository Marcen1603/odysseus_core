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

package de.uniol.inf.is.odysseus.spatial.grid.model;

import java.nio.DoubleBuffer;
import java.util.Arrays;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Grid implements IClone {
	public final Coordinate origin;

	public final int width;
	public final int height;
	public final double cellsize;
	private final DoubleBuffer buffer;

	public Grid(Coordinate origin, int width, int height,
			double cellsize) {
		this.origin = origin;
		this.cellsize = cellsize;
		this.width = width;
		this.height = height;
		this.buffer = DoubleBuffer.allocate(this.width * this.height);
	}

	public Grid(Coordinate origin, int width, int height,
			double cellsize, DoubleBuffer buffer) {
		this(new Coordinate(origin.x, origin.y), width, height, cellsize);
		buffer.rewind();
		this.buffer.put(buffer);

	}

	public double get(int x, int y) {
		return this.buffer.get(y * this.width + x);
	}

	public double get(double x, double y) {
		int gridX = (int) (x / cellsize);
		int gridY = (int) (y / cellsize);
		return get(gridX, gridY);
	}

	public DoubleBuffer getBuffer() {
		this.buffer.rewind();
		return this.buffer;
	}

	public void set(int x, int y, double value) {
		this.buffer.put(y * this.width + x, value);
	}

	public void set(double x, double y, double value) {
		int gridX = (int) (x / cellsize);
		int gridY = (int) (y / cellsize);
		set(gridX, gridY, value);
	}

	public void setBuffer(DoubleBuffer value) {
		this.buffer.clear();
		this.buffer.put(value);
	}

	public void fill(double value) {
		Arrays.fill(this.buffer.array(), value);
	}

	@Override
	public Grid clone() {
		Grid grid = new Grid(this.origin, this.width,
				this.height, this.cellsize, this.buffer);
		return grid;
	}

	@Override
	public String toString() {
		return "{Origin: " + origin + ", Width: " + width + " Depth: " + height
				+ " CellSize: " + this.cellsize + "}";
	}
}
