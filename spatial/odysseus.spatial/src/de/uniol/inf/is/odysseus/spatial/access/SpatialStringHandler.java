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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

import de.uniol.inf.is.odysseus.physicaloperator.access.StringHandler;

/**
 * @author Andr� Bolles
 * @deprecated Use SpatialByteHandler instead if this class shall be
 * used decomment OSGI-INF/SpatialStringHandler.xml and comment
 * OSGI-INF/SpatialByteHandler.xml
 */
public class SpatialStringHandler extends StringHandler{

	WKTReader reader;
	WKTWriter writer;
	
	static protected List<String> types = new ArrayList<String>();
	
	static{
		types.add("SpatialGeometry");
		types.add("SpatialGeometryCollection");

		types.add("SpatialPoint");
		types.add("SpatialLineString");
		types.add("SpatialPolygon");
		
		types.add("SpatialMultiPoint");
		types.add("SpatialMultiLineString");
		types.add("SpatialMutliPolygon");
	}
	
	public SpatialStringHandler(){
		this.reader = new WKTReader();
		this.writer = new WKTWriter();
	}
	
	@Override
	final public Object readData() throws IOException {
		String wktString = (String)super.readData();
		try {
			return this.reader.read(wktString);
		} catch (ParseException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Object readData(ByteBuffer b) {
		String wktString = (String)super.readData(b);
		try {
			return this.reader.read(wktString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		String wktString = this.writer.writeFormatted((Geometry)data);
		super.writeData(buffer, wktString);
	}
	
	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

}
