/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.debs2013.spatial;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

///TODO In GrandChallenge-Package auslagern?
/// Funktioniert nur für GrandChallenge
/**
 * @author Philipp Rudolph, Andreas Harre, Jan Sören Schwarz
 */
public class ToLineString extends AbstractFunction<Geometry> {

	private static final long serialVersionUID = 7202373953195273323L;

	public ToLineString() {
		super("ToLineString",4,accTypes,SDFSpatialDatatype.SPATIAL_LINE_STRING);
	}
	
	
	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] {
		 SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER };

    public static final SDFDatatype[][] accTypes = new SDFDatatype[][]{accTypes1,accTypes1,accTypes1,accTypes1};

	@Override
	public Geometry getValue() {
		GeometryFactory gf = new GeometryFactory();
		Coordinate[] coordinates = new Coordinate[2];
		coordinates[0] = new Coordinate(getNumericalInputValue(0), getNumericalInputValue(1));
		coordinates[1] = new Coordinate(getNumericalInputValue(2), getNumericalInputValue(3));
		return gf.createLineString(coordinates);
	 }

}
