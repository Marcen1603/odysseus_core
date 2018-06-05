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
package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Stephan Jansen <stephan.jansen@offis.de>
 */
public class FromWKT extends AbstractFunction<GeometryWrapper> {

	private static final long serialVersionUID = -8850032331081355095L;

	public FromWKT() {
		super("FromWKT", 1, accTypes, SDFSpatialDatatype.SPATIAL_GEOMETRY);
	}

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.STRING } };

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getValue()
	 */
	@Override
	public GeometryWrapper getValue() {
		Geometry g = null;
		GeometryWrapper wrapper;
		try {
			// See if we have more info in here
			String wkt = (String) this.getInputValue(0);
			int srid = 0;

			// SRID must be first if there's also an ID
			if (wkt.contains("SRID")) {
				int split = wkt.indexOf(";");
				srid = Integer.parseInt(wkt.substring(5, split));
				wkt = wkt.substring(split + 1);
			}

			// ID (not part of EWKT but necessary to have the id of that geo-object, if we
			// later put this into a geoJSON object automatically)
			int id = -1;
			if (wkt.contains("ID")) {
				int split = wkt.indexOf(";");
				id = Integer.parseInt(wkt.substring(3, split));
				wkt = wkt.substring(split + 1);
			}

			g = new WKTReader().read(wkt);
			g.setSRID(srid);
			
			wrapper = new GeometryWrapper(g);
			wrapper.setId(id);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		return wrapper;

	}

}
