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

package de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SDFGridDatatype extends SDFDatatype {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3817051878200646814L;

	public SDFGridDatatype(String URI) {
		super(URI);
	}

	public SDFGridDatatype(SDFDatatype sdfDatatype) {
		super(sdfDatatype);
		// TODO Auto-generated constructor stub
	}

	public SDFGridDatatype(String datatypeName, KindOfDatatype type,
			SDFSchema schema) {
		super(datatypeName, type, schema);
	}

	public static final SDFDatatype GRID = new SDFDatatype("Grid",
			SDFDatatype.KindOfDatatype.BEAN, new SDFSchema("",
					new SDFAttribute(null, "origin",
							SDFSpatialDatatype.SPATIAL_POINT),
					new SDFAttribute(null, "cellsize", SDFDatatype.DOUBLE),
					new SDFAttribute(null, "grid", SDFDatatype.MATRIX_BYTE)));

	public boolean isGrid() {
		return this.getURI().equals(GRID.getURI());
	}

	/**
	 * This method checks whether this type can be casted into
	 * <code>other</code>.
	 * 
	 * @param other
	 * @return True, if this type can be casted into <code>other</code>
	 */
	public boolean compatibleTo(SDFDatatype other) {
		if (other instanceof SDFGridDatatype) {
			SDFGridDatatype otherSpatial = (SDFGridDatatype) other;
			if (this.isGrid() && otherSpatial.isGrid()) {
				return true;
			}
		}
		return super.compatibleTo(other);
	}

}