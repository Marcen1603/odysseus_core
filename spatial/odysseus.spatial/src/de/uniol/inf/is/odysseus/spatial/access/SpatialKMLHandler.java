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
package de.uniol.inf.is.odysseus.spatial.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerException;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Andr� Bolles, Alexander Funk, Marco Grawunder
 * 
 */
public class SpatialKMLHandler extends AbstractDataHandler<Object> {
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add("SpatialKML");
	}

	private KMLReader reader;

	private SpatialKMLHandler() throws ParserConfigurationException, SAXException {
		this.reader = new KMLReader();
	}

	@Override
	public IDataHandler<Object> getInstance(SDFSchema schema) {
		try {
			return new SpatialKMLHandler();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object readData(ObjectInputStream inputStream) throws IOException {
		throw new RuntimeException("Method not supported!");
	}

	@Override
	public Object readData(ByteBuffer buffer) {
		throw new RuntimeException("Method not supported!");
	}

	@Override
	public Object readData(String string) {
		try {
			return reader.read(string);
		} catch (Exception e) {
			throw new DataHandlerException(e);
		} 
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		throw new RuntimeException("Method not supported!");
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return types;
	}

	@Override
	public int memSize(Object attribute) {
		int size = 0;

		/*
		 * The GeometryCollection handles the MultiPoint, MultiLine, and
		 * MultiPolygon as well.
		 */
		if (attribute instanceof GeometryCollection) {
			size += getGeometryCollectionMemSize((GeometryCollection) attribute);
		}

		if (attribute instanceof Polygon) {
			size += getPolygonMemSize((Polygon) attribute);
		}

		if (attribute instanceof LineString) {
			size += getLineStringMemSize((LineString) attribute);
		}

		if (attribute instanceof Point) {
			size += getPointMemSize();
		}

		return size;
	}

	private int getGeometryCollectionMemSize(GeometryCollection collection) {
		int size = 5;
		size += 4;
		for (int i = 0; i < collection.getNumGeometries(); i++) {
			Geometry geometry = collection.getGeometryN(i);
			size += memSize(geometry);
		}
		return size;
	}

	private static int getPointMemSize() {
		return 5 + 2 * 16;
	}

	private static int getLineStringMemSize(LineString lineString) {
		return 5 + getCoordinateSequenceMemSize(lineString
				.getCoordinateSequence());
	}

	private static int getPolygonMemSize(Polygon polygon) {
		int size = 5;
		size += 4;
		size += getCoordinateSequenceMemSize(polygon.getExteriorRing()
				.getCoordinateSequence());

		for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
			size += getCoordinateSequenceMemSize(polygon.getInteriorRingN(i)
					.getCoordinateSequence());
		}

		return size;
	}

	private static int getCoordinateSequenceMemSize(
			CoordinateSequence coordinateSequence) {
		return 4 + coordinateSequence.size() * 16;
	}

}
