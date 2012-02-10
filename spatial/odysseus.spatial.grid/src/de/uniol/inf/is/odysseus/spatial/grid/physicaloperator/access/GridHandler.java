package de.uniol.inf.is.odysseus.spatial.grid.physicaloperator.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.physicaloperator.access.AbstractAtomicDataHandler;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;

public class GridHandler extends AbstractAtomicDataHandler {
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add("Grid");
	}

	@Override
	public Object readData() throws IOException {
		ObjectInputStream stream = getStream();
		int x = stream.readInt();
		int y = stream.readInt();
		short width = stream.readShort();
		short depth = stream.readShort();
		short height = stream.readShort();
		int cellsize = stream.readInt() / 10;
		Grid grid = new Grid(new Coordinate(x, y), width * cellsize, depth
				* cellsize, cellsize);
		stream.readFully(grid.get(), 0, width * depth * height);
		return null;
	}

	@Override
	public Object readData(ByteBuffer buffer) {
		int x = buffer.getInt();
		int y = buffer.getInt();
		short width = buffer.getShort();
		short depth = buffer.getShort();
		@SuppressWarnings("unused")
		short height = buffer.getShort();
		int cellsize = buffer.getInt() / 10;
		Grid grid = new Grid(new Coordinate(x, y), width * cellsize, depth
				* cellsize, cellsize);
		grid.getBuffer().put(buffer);
		return grid;
	}

	@Override
	public Object readData(String string) {
		return readData(ByteBuffer.wrap(string.getBytes()));
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		Grid grid = (Grid) data;
		// X Position
		buffer.putInt((int) grid.origin.x);
		// Y Position
		buffer.putInt((int) grid.origin.y);
		// Grid Width
		buffer.putShort((short) grid.width);
		// Grid Depth
		buffer.putShort((short) grid.depth);
		// Grid Height (not supported yet)
		buffer.putShort((short) 1);
		// Cell Size (in mm)
		buffer.putInt((int) grid.cellsize * 10);
		buffer.put(grid.get());
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

}
