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
package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ToCartesianCoordinate extends AbstractFunction<Coordinate> {

	private static final long serialVersionUID = 2881839260208999355L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER },
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER } };

	public ToCartesianCoordinate() {
		super("ToCartesianCoordinate",2,accTypes,SDFSpatialDatatype.SPATIAL_COORDINATE);
	}

	@Override
	public Coordinate getValue() {
		double x = this.getNumericalInputValue(0);
		double y = this.getNumericalInputValue(1);
		return new Coordinate(x, y);
	}

}
