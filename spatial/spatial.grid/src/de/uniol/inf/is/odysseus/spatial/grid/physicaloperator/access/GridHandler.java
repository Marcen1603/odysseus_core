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

package de.uniol.inf.is.odysseus.spatial.grid.physicaloperator.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.spatial.grid.model.CartesianGrid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GridHandler extends AbstractDataHandler<CartesianGrid> {
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add("Grid");
	}

	@Override
	public IDataHandler<CartesianGrid> getInstance(SDFSchema schema) {
		return new GridHandler();
	}

	@Override
	public CartesianGrid readData(ObjectInputStream stream) throws IOException {
		int x = stream.readInt();
		int y = stream.readInt();
		short width = stream.readShort();
		short height = stream.readShort();
		short future = stream.readShort();
		int cellsize = stream.readInt() / 10;
		CartesianGrid grid = new CartesianGrid(new Coordinate(x, y), width,
				height, cellsize);

		// stream.readFully(grid.get(), 0, width * height);
		return grid;
	}

	@Override
	public CartesianGrid readData(ByteBuffer buffer) {
		int x = buffer.getInt();
		int y = buffer.getInt();
		short width = buffer.getShort();
		short height = buffer.getShort();
		@SuppressWarnings("unused")
		short future = buffer.getShort();
		int cellsize = buffer.getInt() / 10;
		CartesianGrid grid = new CartesianGrid(new Coordinate(x, y), width,
				height, cellsize);
		// grid.getBuffer().put(buffer);
		return grid;
	}

	@Override
	public CartesianGrid readData(String string) {
		return readData(ByteBuffer.wrap(string.getBytes()));
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		CartesianGrid grid = (CartesianGrid) data;
		// X Position
		buffer.putInt((int) grid.origin.x);
		// Y Position
		buffer.putInt((int) grid.origin.y);
		// Grid Width
		buffer.putShort((short) grid.width);
		// Grid Depth
		buffer.putShort((short) grid.height);
		// Grid Height (not supported yet)
		buffer.putShort((short) 1);
		// Cell Size (in mm)
		buffer.putInt((int) grid.cellsize * 10);
		// buffer.put(grid.get());
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

	@Override
	public int memSize(Object attribute) {
		CartesianGrid grid = (CartesianGrid) attribute;
		return (Integer.SIZE / 8) * 3 + (Short.SIZE / 8) * 3
				+ grid.getBuffer().capacity();
	}

}
